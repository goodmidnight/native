#ifndef SCANNER_TYPES_H
#define SCANNER_TYPES_H

#include <opencv2/opencv.hpp>
#include <vector>
#include <string>

namespace native_scanner {

    /**
     * @enum ProcessingMode
     * @brief Defines the visual post-processing strategy based on the user's intent.
     */
    enum class ProcessingMode {
        SCAN, /*!< Enhances visual quality for human reading (whitening, shadow removal) */
        OCR   /*!< Optimizes for machine text recognition (strict binarization) */
    };

    /**
     * @enum DocumentType
     * @brief Specifies the physical type of the document to apply specific domain logic.
     */
    enum class DocumentType {
        GENERAL,       /*!< Standard A4 documents */
        ID_CARD,       /*!< ID cards (requires stricter glare checks and smaller binarization blocks) */
        BUSINESS_CARD, /*!< Business cards */
        RECEIPT        /*!< Receipts (allows extreme aspect ratios and heavier blur for wrinkles) */
    };

    /**
     * @enum CaptureStatus
     * @brief Status codes returned after a high-resolution capture attempt.
     */
    enum class CaptureStatus {
        SUCCESS = 0,
        ERR_NOT_DETECTED = 1,
        ERR_BLURRY = 2,
        ERR_GLARE = 3,
        ERR_UNKNOWN = 4
    };

    /**
     * @struct ScannerConfig
     * @brief Global configuration parameters to tune the scanner engine's behavior and thresholds.
     */
    struct ScannerConfig {
        int target_width = 800;             /*!< Downscale width for real-time preview processing */
        float canny_sigma = 0.33f;          /*!< Sensitivity multiplier for automatic Canny edge detection */
        double min_area_ratio = 0.15;        /*!< Minimum area ratio (0.0~1.0) a document must occupy */
        bool low_light_mode = false;        /*!< Applies CLAHE to boost contrast in dark environments */

        double blur_threshold = 50.0;         /*!< Variance threshold for Laplacian blur detection */
        double glare_threshold_general = 0.08; /*!< Allowed overexposed white pixel ratio for general docs */
        double glare_threshold_id = 0.05;      /*!< Stricter glare ratio for reflective ID cards */
        int blur_kernel_size = 0;              /*!< Custom blur kernel size (0 = auto calculated) */
    };

    /**
     * @struct DocumentFrame
     * @brief Holds the geometric data and state of the document detected in the preview frame.
     */
    struct DocumentFrame {
        std::vector<cv::Point2f> points;    /*!< 4 corner coordinates (TL, TR, BR, BL) */
        bool is_detected = false;           /*!< True if a valid document shape was found */
        float confidence = 0.0f;            /*!< Area ratio of the document relative to the whole frame */
        bool is_stable = false;             /*!< True if coordinates haven't moved significantly for N frames */
    };

    /**
     * @struct CaptureResult
     * @brief The final result payload returned to the client UI after a capture attempt.
     */
    struct CaptureResult {
        cv::Mat image;                                 /*!< The processed, cropped, and enhanced image */
        CaptureStatus status = CaptureStatus::SUCCESS; /*!< Result status code */
        bool is_blurry = false;                        /*!< True if the image failed the sharpness check */
        bool has_glare = false;                        /*!< True if the image failed the reflection check */
        std::string message;                           /*!< Human-readable status or error message */
    };
}

#endif