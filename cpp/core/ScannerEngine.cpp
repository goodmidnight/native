#include "ScannerEngine.hpp"
#include "GeometryUtils.h"
#include "ImageUtils.h"
#include "ImagePreprocessor.h"
#include "ImageValidator.hpp"
#include "ScanProcessor.hpp"
#include "OcrProcessor.hpp"
#include <string>

namespace native_scanner {
    ScannerEngine::ScannerEngine(const ScannerConfig &config) : config_(config) {
        scan_processor_ = std::make_unique<ScanProcessor>();
        ocr_processor_ = std::make_unique<OcrProcessor>();
    }

    void ScannerEngine::updateConfig(const ScannerConfig &config) {
        std::lock_guard<std::mutex> lock(config_mutex_);
        config_ = config;
        if (logger_) logger_(LogLevel::INFO, "Scanner configuration updated.");
    }

    void ScannerEngine::setLogger(LoggerCallback logger) {
        logger_ = std::move(logger);
    }

    DocumentFrame ScannerEngine::detectDocument(const cv::Mat &preview_img, DocumentType type, int rotation_degrees) {
        if (logger_) logger_(LogLevel::INFO, "Starting document detection...");

        ScannerConfig current_config;
        {
            std::lock_guard<std::mutex> lock(config_mutex_);
            current_config = config_;
        }

        cv::Mat process_img;
        cv::Mat rotated_img = ImageUtils::rotateImage(preview_img, rotation_degrees);
        float scale = ImageUtils::downscale(rotated_img, process_img, current_config.target_width);

        cv::Mat dst;
        ImagePreprocessor::preprocess(
            process_img,
            dst,
            current_config.canny_sigma,
            current_config.blur_kernel_size,
            current_config.low_light_mode,
            type
        );

        if (type == DocumentType::RECEIPT) {
            current_config.min_area_ratio = 0.05;
        } else if (type == DocumentType::ID_CARD || type == DocumentType::BUSINESS_CARD) {
            current_config.min_area_ratio = 0.1;
        }

        DocumentFrame frame = GeometryUtils::findLargestArea(dst, current_config.min_area_ratio, type);

        if (frame.is_detected) {
            if (logger_) logger_(LogLevel::DEBUG, "Document detected with confidence: " + std::to_string(frame.confidence));
            if (!prev_points_.empty()) {
                float max_distance = 0.0f;
                for (size_t i = 0; i < 4; ++i) {
                    float dist = static_cast<float>(cv::norm(frame.points[i] - prev_points_[i]));
                    max_distance = std::max(max_distance, dist);
                }

                if (max_distance < 25.0f) { // 안정성 검사 임계값 상향
                    stable_frame_count_++;
                } else {
                    stable_frame_count_ = 0;
                }

                if (stable_frame_count_ >= 2) { // 안정성 카운트 조건 완화
                    frame.is_stable = true;
                    if (logger_) logger_(LogLevel::DEBUG, "Frame is stable.");
                }
            } else {
                stable_frame_count_ = 0;
            }

            frame.points = GeometryUtils::smoothPoints(prev_points_, frame.points, 0.5f); // 스무딩 팩터 증가
            prev_points_ = frame.points;

            float inv_scale = 1.0f / scale;
            frame.points = GeometryUtils::scalePoints(frame.points, inv_scale);
        } else {
             if (logger_) logger_(LogLevel::DEBUG, "No document detected in the current frame.");
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
        if (logger_) logger_(LogLevel::INFO, "Starting document capture...");
        CaptureResult result;

        try {
            if (src.empty()) {
                if (logger_) logger_(LogLevel::ERROR, "Capture failed: Source image is empty.");
                result.status = CaptureStatus::ERR_EMPTY_IMAGE;
                result.message = "Source image is empty.";
                return result;
            }

            if (!detected_frame.is_detected || detected_frame.points.size() != 4) {
                if (logger_) logger_(LogLevel::WARN, "Capture failed: No valid document frame provided.");
                result.status = CaptureStatus::ERR_NOT_DETECTED;
                result.message = "No document detected.";
                return result;
            }

            ScannerConfig current_config;
            {
                std::lock_guard<std::mutex> lock(config_mutex_);
                current_config = config_;
            }

            cv::Mat rotated_src = ImageUtils::rotateImage(src, rotation_degrees);

            float scale_factor = static_cast<float>(rotated_src.cols) / static_cast<float>(preview_size.width);
            std::vector<cv::Point2f> full_res_points = GeometryUtils::scalePoints(detected_frame.points, scale_factor);

            cv::Mat warped = ImageUtils::applyWarp(rotated_src, full_res_points, cv::INTER_CUBIC);

            if (warped.empty()) {
                if (logger_) logger_(LogLevel::ERROR, "Capture failed: Perspective transformation (warp) resulted in an empty image.");
                result.status = CaptureStatus::ERR_WARP_FAILED;
                result.message = "Perspective transformation failed.";
                return result;
            }

            bool srcIsPortrait = rotated_src.rows >= rotated_src.cols;
            bool warpedIsPortrait = warped.rows >= warped.cols;
            if (srcIsPortrait != warpedIsPortrait) {
                cv::rotate(warped, warped, cv::ROTATE_90_CLOCKWISE);
            }

            cv::Mat gray_warped;
            cv::cvtColor(warped, gray_warped, cv::COLOR_BGR2GRAY);

            result.is_blurry = ImageValidator::isBlurry(gray_warped, 20.0);
            result.has_glare = ImageValidator::hasGlare(gray_warped, (type == DocumentType::ID_CARD) ? current_config.glare_threshold_id : current_config.glare_threshold_general);
             if (logger_) {
                logger_(LogLevel::INFO, "Image Quality - Blurry: " + std::string(result.is_blurry ? "Yes" : "No") + ", Glare: " + (result.has_glare ? "Yes" : "No"));
            }

            result.status = CaptureStatus::SUCCESS;
            result.message = "SUCCESS";

            if (logger_) logger_(LogLevel::INFO, "Applying post-processing mode: " + std::string(mode == ProcessingMode::SCAN ? "SCAN" : "OCR"));
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
        } catch (const cv::Exception& e) {
            if (logger_) logger_(LogLevel::ERROR, "An OpenCV exception occurred: " + std::string(e.what()));
            result.status = CaptureStatus::ERR_UNKNOWN;
            result.message = "OpenCV Error: " + std::string(e.what());
        } catch (const std::exception& e) {
            if (logger_) logger_(LogLevel::ERROR, "A standard exception occurred: " + std::string(e.what()));
            result.status = CaptureStatus::ERR_UNKNOWN;
            result.message = "STD Error: " + std::string(e.what());
        } catch (...) {
            if (logger_) logger_(LogLevel::ERROR, "An unknown native exception occurred.");
            result.status = CaptureStatus::ERR_UNKNOWN;
            result.message = "An unknown native error occurred.";
        }

        if (logger_) logger_(LogLevel::INFO, "Capture finished with status: " + std::to_string(static_cast<int>(result.status)));
        return result;
    }
}