#include <memory>
#include "IPostprocessor.hpp"
#ifndef SCANNER_ENGINE_HPP
#define SCANNER_ENGINE_HPP

#include "ScannerType.h"
#include <opencv2/opencv.hpp>
#include <vector>
#include <mutex>

namespace native_scanner {

    /**
     * @class ScannerEngine
     * @brief Core orchestrator for the document scanning pipeline.
     * Handles both real-time preview detection and high-resolution capture processing.
     * Designed to be thread-safe for use in mobile environments (JNI/Objective-C++).
     */
    class ScannerEngine {
    public:
        /**
         * @brief Initializes the scanner engine.
         * @param config The initial configuration parameters (e.g., target width, thresholds).
         */
        explicit ScannerEngine(const ScannerConfig &config = ScannerConfig());

        /**
         * @brief Updates the engine's configuration safely across multiple threads.
         * @param config The new configuration to apply.
         */
        void updateConfig(const ScannerConfig &config);

        /**
         * @brief Registers a callback function for logging.
         * @param logger The function to be called for logging messages.
         */
        void setLogger(LoggerCallback logger);

        /**
         * @brief [Preview Phase] Detects the geometric boundary of a document in real-time.
         * @param preview_img The input image matrix from the camera preview.
         * @param type The type of document being scanned, used to adjust minimum area thresholds.
         * @param rotation_degrees The orientation of the camera sensor to correct the image before processing.
         * @return DocumentFrame containing the 4 corners, confidence score, and stability status.
         */
        DocumentFrame detectDocument(
             const cv::Mat& preview_img,
             DocumentType type = DocumentType::GENERAL,
             int rotation_degrees = 0
         );

        /**
         * @brief [Capture Phase] Crops, warps, validates, and visually enhances the high-resolution original image.
         * @param src The original, unscaled high-resolution image from the camera sensor.
         * @param detected_frame The geometric frame obtained from detectDocument().
         * @param preview_size The size of the UI preview surface, used to calculate the scale factor for full-res extraction.
         * @param mode The post-processing goal (SCAN for whitening, OCR for binarization).
         * @param type The document type to apply specific domain logic and orientation rules.
         * @param rotation_degrees The orientation of the camera sensor to correct the high-res image.
         * @return CaptureResult The final processed image along with quality validation flags (blur, glare) and status.
         */
        CaptureResult captureDocument(
            const cv::Mat& src,
            const DocumentFrame& detected_frame,
            const cv::Size& preview_size,
            ProcessingMode mode,
            DocumentType type,
            int rotation_degrees = 0
        );

    private:
        LoggerCallback logger_ = nullptr;      // Callback for propagating logs to the client
        ScannerConfig config_;                 // Current active configuration
        std::vector<cv::Point2f> prev_points_; // Cached coordinates from the previous frame for EMA smoothing
        std::mutex config_mutex_;              // Ensures thread-safe access to config_
        int stable_frame_count_ = 0;           // Tracks how many consecutive frames the document has been steady

        // Strategy pattern for post-processing
        std::unique_ptr<IPostprocessor> scan_processor_;
        std::unique_ptr<IPostprocessor> ocr_processor_;
    };
}

#endif