#include "ScannerEngine.hpp"
#include "GeometryUtils.h"
#include "ImageUtils.h"
#include "ImagePreprocessor.h"
#include "ImageValidator.hpp"
#include "ScanProcessor.hpp"
#include "OcrProcessor.hpp"

namespace native_scanner {
    ScannerEngine::ScannerEngine(const ScannerConfig &config) : config_(config) {
        scan_processor_ = std::make_unique<ScanProcessor>();
        ocr_processor_ = std::make_unique<OcrProcessor>();
    }

    void ScannerEngine::updateConfig(const ScannerConfig &config) {
        std::lock_guard<std::mutex> lock(config_mutex_);
        config_ = config;
    }

    DocumentFrame ScannerEngine::detectDocument(const cv::Mat &preview_img, DocumentType type, int rotation_degrees) {
        // Safely copy the current config to prevent race conditions during the frame processing
        ScannerConfig current_config;
        {
            std::lock_guard<std::mutex> lock(config_mutex_);
            current_config = config_;
        }

        cv::Mat process_img;

        // Step 1: Correct the orientation of the input frame based on device sensor rotation
        cv::Mat rotated_img = ImageUtils::rotateImage(preview_img, rotation_degrees);

        // Step 2: Downscale for performance
        // Heavy processing (Canny, Contours) is done on a smaller image to maintain real-time FPS
        float scale = ImageUtils::downscale(rotated_img, process_img, current_config.target_width);

        // Step 3: Apply preprocessing filters
        // Applies CLAHE to improve contrast if the environment is too dark, aiding edge detection.
        if (current_config.low_light_mode) {
            ImagePreprocessor::applyLowLightEnhancement(process_img, process_img);
        }

        cv::Mat dst;
        // Applies dynamic Gaussian blur and adaptive Canny edge detection.
        ImagePreprocessor::preprocess(process_img, dst, current_config.canny_sigma, current_config.blur_kernel_size);

        // Dynamic Threshold Adjustment based on Document Type
        // Receipts are narrower; ID cards are smaller. Adjust the minimum area requirement accordingly.
        if (type == DocumentType::RECEIPT) {
            current_config.min_area_ratio = 0.05;
        } else if (type == DocumentType::ID_CARD || type == DocumentType::BUSINESS_CARD) {
            current_config.min_area_ratio = 0.1;
        }

        // Step 4: Geometry Analysis
        // Finds the largest convex quadrilateral that satisfies the area ratio and aspect ratio constraints.
        DocumentFrame frame = GeometryUtils::findLargestArea(dst, current_config.min_area_ratio);

        if (frame.is_detected) {
            // Step 5: Stability Checker (Auto-Capture Logic)
            // Compares current points with previous points BEFORE scaling up to ensure
            // the distance threshold (15px) behaves consistently across all device resolutions.
            if (!prev_points_.empty()) {
                float max_distance = 0.0f;
                for (size_t i = 0; i < 4; ++i) {
                    float dist = static_cast<float>(cv::norm(frame.points[i] - prev_points_[i]));
                    max_distance = std::max(max_distance, dist);
                }

                // If points moved less than 15 pixels, increment the stability counter
                if (max_distance < 15.0f) {
                    stable_frame_count_++;
                } else {
                    stable_frame_count_ = 0;
                }

                // Trigger auto-capture readiness if stable for 3 consecutive frames
                if (stable_frame_count_ >= 3) {
                    frame.is_stable = true;
                }
            } else {
                stable_frame_count_ = 0;
            }

            // Step 6: Temporal Smoothing (Anti-jittering)
            // Apply Exponential Moving Average (EMA) using previous points to prevent the UI bounding box from shaking.
            frame.points = GeometryUtils::smoothPoints(prev_points_, frame.points, 0.3f);
            prev_points_ = frame.points;

            // Step 7: Restore coordinates back to the original preview resolution
            // The client UI needs coordinates matching the full preview surface size.
            float inv_scale = 1.0f / scale;
            frame.points = GeometryUtils::scalePoints(frame.points, inv_scale);
        } else {
            // Clear history if detection fails, so the next detection starts fresh.
            prev_points_.clear();
            stable_frame_count_ = 0;
        }

        return frame;
    }

   CaptureResult ScannerEngine::captureDocument(
        const cv::Mat &src,
        const DocumentFrame &detected_frame,
        const cv::Size &preview_size,
        ProcessingMode mode,
        DocumentType type,
        int rotation_degrees
    ) {
        CaptureResult result;

        // Fallback: If capture is triggered without a valid geometric frame, abort.
        if (!detected_frame.is_detected || detected_frame.points.size() != 4) {
            result.status = CaptureStatus::ERR_NOT_DETECTED;
            result.message = "No document detected.";
            return result;
        }

        ScannerConfig current_config;
        {
            std::lock_guard<std::mutex> lock(config_mutex_);
            current_config = config_;
        }

        // Step 1: Correct the orientation of the high-res original image
        cv::Mat rotated_src = ImageUtils::rotateImage(src, rotation_degrees);

        // Step 2: Coordinate Scaling
        // The points received from the client UI (preview_size) must be mapped to the actual high-res image (rotated_src).
        float scale_factor = static_cast<float>(rotated_src.cols) / static_cast<float>(preview_size.width);
        std::vector<cv::Point2f> full_res_points = GeometryUtils::scalePoints(detected_frame.points, scale_factor);

        // Step 3: Perspective Warp
        // Transforms the skewed quadrilateral region into a flat, top-down rectangular image using high-quality cubic interpolation.
        cv::Mat warped = ImageUtils::applyWarp(rotated_src, full_res_points, cv::INTER_CUBIC);

        // Step 4: Auto-Rotation Enforcement
        // Forces landscape/portrait orientation based on the logical document type, regardless of how the user held the phone.
        if (type == DocumentType::GENERAL || type == DocumentType::RECEIPT) {
            if (warped.cols > warped.rows) cv::rotate(warped, warped, cv::ROTATE_90_CLOCKWISE); // Force Portrait
        } else if (type == DocumentType::ID_CARD || type == DocumentType::BUSINESS_CARD) {
            if (warped.rows > warped.cols) cv::rotate(warped, warped, cv::ROTATE_90_CLOCKWISE); // Force Landscape
        }


        cv::Mat gray_warped;
        cv::cvtColor(warped, gray_warped, cv::COLOR_BGR2GRAY);

        // Step 5: Quality Assurance using ImageValidator
        // The blur threshold is lowered significantly to account for white margins in documents.
        result.is_blurry = ImageValidator::isBlurry(gray_warped, 20.0);

        double glare_threshold = (type == DocumentType::ID_CARD)
                                 ? current_config.glare_threshold_id
                                 : current_config.glare_threshold_general;
        result.has_glare = ImageValidator::hasGlare(gray_warped, glare_threshold);

        // Set status to SUCCESS to ensure the processed image is always returned,
        // allowing the client UI to decide how to handle the blur/glare flags.
        result.status = CaptureStatus::SUCCESS;
        result.message = "SUCCESS";

        // Step 6: Domain-Specific Post-Processing
        // Delegates the visual enhancement to the post-processor based on the requested mode.
        switch (mode) {
            case ProcessingMode::SCAN:
                result.image = scan_processor_->process(warped, type);
                break;
            case ProcessingMode::OCR:
                result.image = ocr_processor_->process(warped, type);
                break;
            default:
                result.image = warped;
                break;
        }

        return result;
    }
}