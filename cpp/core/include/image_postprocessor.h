#ifndef IMAGE_POSTPROCESSOR_H
#define IMAGE_POSTPROCESSOR_H

#include <opencv2/opencv.hpp>
#include "scanner_type.h"

namespace native_scanner {

    /**
     * @class ImagePostprocessor
     * @brief Applies final visual enhancements to the warped document image based on the target use case.
     */
    class ImagePostprocessor {
    public:
        /**
         * @brief [SCAN Mode] Enhances visual quality for human readability and storage.
         * Removes shadows, whitens the background, and preserves text and color integrity.
         * @param warped The flat, cropped image of the document (BGR format).
         * @param type The document type, used to adjust the intensity of wrinkle and shadow removal.
         * @return cv::Mat The visually enhanced, high-contrast document image (Grayscale).
         */
        static cv::Mat processForScan(const cv::Mat& warped, DocumentType type);

        /**
         * @brief [OCR Mode] Optimizes the image for machine text recognition (Optical Character Recognition).
         * Converts the image to strictly binary (black and white) and removes small background artifacts.
         * @param warped The flat, cropped image of the document (BGR format).
         * @param type The document type, used to adjust the binarization block size for specific patterns.
         * @return cv::Mat The binary image ready for OCR engines.
         */
        static cv::Mat processForOCR(const cv::Mat& warped, DocumentType type);
    };
}

#endif