#ifndef IMAGE_UTILS_H
#define IMAGE_UTILS_H

#include <opencv2/opencv.hpp>

namespace native_scanner {

    /**
     * @class ImageUtils
     * @brief Provides pure mathematical matrix transformations such as scaling, warping, and rotating.
     */
    class ImageUtils {
    public:
        /**
         * @brief Scales down the image to a specific target width while preserving the aspect ratio.
         * @param src High-resolution source image.
         * @param dst Output downscaled image.
         * @param target_width The desired width in pixels.
         * @return float The applied scale factor (target_width / original_width).
         */
        static float downscale(const cv::Mat& src, cv::Mat& dst, int target_width);

        /**
         * @brief Applies a Perspective Transform to flatten a skewed 4-point polygon into a top-down rectangle.
         * @param src Source image.
         * @param corners The 4 ordered coordinates of the document.
         * @param interpolation The resizing algorithm (e.g., INTER_CUBIC for high-quality final capture).
         * @return cv::Mat The flattened, cropped rectangular image.
         */
        static cv::Mat applyWarp(const cv::Mat& src, const std::vector<cv::Point2f>& corners, int interpolation = cv::INTER_LINEAR);

        /**
         * @brief Rotates the image cleanly by exact right angles based on device sensor data.
         * @param src Source image.
         * @param rotation_degrees The rotation angle (e.g., 90, 180, -90).
         * @return cv::Mat The rotated image.
         */
        static cv::Mat rotateImage(const cv::Mat& src, int rotation_degrees);
    };
}

#endif