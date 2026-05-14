#include "ScanProcessor.hpp"
#include <opencv2/photo.hpp>

namespace native_scanner {

    cv::Mat ScanProcessor::process(const cv::Mat& image, DocumentType type) {
        cv::Mat gray, processed_image;
        cv::cvtColor(image, gray, cv::COLOR_BGR2GRAY);

        // Slightly blur to reduce noise before adaptive thresholding
        cv::GaussianBlur(gray, gray, cv::Size(5, 5), 0);

        // Use adaptive thresholding to create a binary image that preserves details
        // This is highly effective for text and line art.
        cv::adaptiveThreshold(
            gray,
            processed_image,
            255,
            cv::ADAPTIVE_THRESH_GAUSSIAN_C,
            cv::THRESH_BINARY,
            15, // Block size
            10  // Constant subtracted from the mean
        );

        return processed_image;
    }

}
