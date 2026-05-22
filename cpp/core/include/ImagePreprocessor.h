#ifndef IMAGE_FILTER_H
#define IMAGE_FILTER_H

#include <opencv2/opencv.hpp>
#include "ScannerType.h"

namespace native_scanner {

    /**
     * @class ImagePreprocessor
     * @brief Prepares raw camera images for optimal geometric edge detection.
     */
    class ImagePreprocessor {
    public:
        /**
         * @brief Enhances contrast in low-light environments without distorting natural colors.
         * @param src Original image.
         * @param dst Contrast-enhanced image.
         */
        static void applyLowLightEnhancement(const cv::Mat& src, cv::Mat& dst);

        /**
         * @brief Applies Gaussian blur to eliminate camera sensor noise and small background textures.
         * @param src Grayscale input image.
         * @param dst Blurred image.
         * @param custom_k_size Specific kernel size to use. If <= 0, it is dynamically calculated.
         */
        static void applyDynamicBlur(const cv::Mat& src, cv::Mat& dst, int custom_k_size = 0);

        /**
         * @brief Extracts edges using the Canny algorithm with mathematically derived thresholds.
         * @param src Blurred grayscale image.
         * @param dst Binary image containing only the edges.
         * @param sigma Sensitivity multiplier for threshold boundaries.
         */
        static void applyAdaptiveCanny(const cv::Mat& src, cv::Mat& dst, float sigma = 0.33f);

        /**
         * @brief Executes the full preprocessing pipeline (Grayscale -> Blur -> Canny).
         */
        static void preprocess(const cv::Mat& src, cv::Mat& dst, float canny_sigma = 0.33f, int custom_blur_size = 0, bool low_light_mode = false, DocumentType type = DocumentType::GENERAL);

        /**
         * @brief Removes shadows from an image by normalizing brightness.
         * @param src Grayscale input image, typically from a warped document.
         * @param dst Grayscale image with shadows suppressed.
         */
        static void removeShadows(const cv::Mat& src, cv::Mat& dst);
    };
}

#endif