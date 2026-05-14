#include "ImageValidator.hpp"

namespace native_scanner {

    bool ImageValidator::isBlurry(const cv::Mat& gray_image, double threshold) {
        cv::Mat laplacian, mean, stddev;
        cv::Laplacian(gray_image, laplacian, CV_64F);
        cv::meanStdDev(laplacian, mean, stddev);
        double variance = stddev.at<double>(0) * stddev.at<double>(0);
        return variance < threshold;
    }

    bool ImageValidator::hasGlare(const cv::Mat& gray_image, double threshold) {
        cv::Mat glare_mask;
        cv::threshold(gray_image, glare_mask, 245, 255, cv::THRESH_BINARY);
        int glare_pixels = cv::countNonZero(glare_mask);
        double glare_ratio = static_cast<double>(glare_pixels) / (gray_image.rows * gray_image.cols);
        return glare_ratio > threshold;
    }

}
