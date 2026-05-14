#ifndef IMAGE_VALIDATOR_HPP
#define IMAGE_VALIDATOR_HPP

#include <opencv2/opencv.hpp>

namespace native_scanner {

    /**
     * @class ImageValidator
     * @brief Provides a set of static methods for quality assurance checks on captured images.
     * This class encapsulates validation logic, such as blur and glare detection, keeping the main pipeline clean.
     */
    class ImageValidator {
    public:
        /**
         * @brief Checks if an image is blurry using the variance of the Laplacian.
         * @param gray_image The input single-channel grayscale image.
         * @param threshold The blurriness threshold. A variance lower than this value is considered blurry.
         * @return True if the image is blurry, false otherwise.
         */
        static bool isBlurry(const cv::Mat& gray_image, double threshold);

        /**
         * @brief Detects glare in an image by calculating the ratio of overexposed pixels.
         * @param gray_image The input single-channel grayscale image.
         * @param threshold The glare ratio threshold. A ratio of white pixels higher than this is considered glare.
         * @return True if glare is detected, false otherwise.
         */
        static bool hasGlare(const cv::Mat& gray_image, double threshold);
    };

}

#endif //IMAGE_VALIDATOR_HPP