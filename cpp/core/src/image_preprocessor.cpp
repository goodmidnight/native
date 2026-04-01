#include "image_preprocessor.h"
#include <opencv2/imgproc.hpp>
#include <numeric>

namespace native_scanner {

    void ImagePreprocessor::applyLowLightEnhancement(const cv::Mat& src, cv::Mat& dst) {
        cv::Mat lab;
        // Convert to LAB color space. Modifying RGB directly shifts colors (e.g., white becomes yellow).
        cv::cvtColor(src, lab, cv::COLOR_BGR2Lab);

        std::vector<cv::Mat> lab_planes(3);
        cv::split(lab, lab_planes);

        // Apply CLAHE (Contrast Limited Adaptive Histogram Equalization) ONLY to the L (Lightness) channel.
        // This boosts contrast in shadows while preserving the exact A and B color channels.
        cv::Ptr<cv::CLAHE> clahe = cv::createCLAHE();
        clahe->setClipLimit(3.0);
        clahe->apply(lab_planes[0], lab_planes[0]);

        cv::merge(lab_planes, lab);
        cv::cvtColor(lab, dst, cv::COLOR_Lab2BGR);
    }

    void ImagePreprocessor::applyDynamicBlur(const cv::Mat &src, cv::Mat &dst, int custom_k_size) {
        int k_size;

        if (custom_k_size <= 0) {
            // Dynamically calculate kernel size as ~0.5% of the image width.
            // Bitwise OR with 1 (| 1) ensures the result is ALWAYS an odd number, which GaussianBlur requires.
            k_size = (src.cols / 200) | 1;
        } else {
            k_size = custom_k_size | 1;
        }

        cv::GaussianBlur(src, dst, cv::Size(k_size, k_size), 0);
    }

    void ImagePreprocessor::applyAdaptiveCanny(const cv::Mat &src, cv::Mat &dst, float sigma) {
        // 1. Calculate the median brightness of the image.
        // Sorting all pixels to find the median is O(N log N) and too slow for real-time video.
        // Instead, we build a 256-bin histogram (O(N)) and find where the cumulative sum hits 50%.
        int histSize = 256;
        float range[] = {0, 256};
        const float *histRange = {range};
        cv::Mat hist;

        cv::calcHist(&src, 1, nullptr, cv::Mat(), hist, 1, &histSize, &histRange, true, false);

        int totalPixels = src.rows * src.cols;
        int halfPixels = totalPixels / 2;
        int sum = 0;
        int medianVal = -1;

        for (int i = 0; i < histSize; ++i) {
            sum += cvRound(hist.at<float>(i));
            if (sum >= halfPixels) {
                medianVal = i;
                break;
            }
        }

        auto median = static_cast<double>(medianVal);

        // 2. Calculate dynamic thresholds for Canny Edge Detection.
        // Lower bound is (1 - sigma)% of median, Upper bound is (1 + sigma)% of median.
        int low_thresh = static_cast<int>(std::max(0.0, (1.0 - sigma) * median));
        int high_thresh = static_cast<int>(std::min(255.0, (1.0 + sigma) * median));

        cv::Canny(src, dst, low_thresh, high_thresh);
    }

    void ImagePreprocessor::preprocess(const cv::Mat &src, cv::Mat &dst, float canny_sigma, int custom_blur_size) {
        cv::Mat gray, blurred;
        cv::cvtColor(src, gray, cv::COLOR_BGR2GRAY);

        applyDynamicBlur(gray, blurred, custom_blur_size);
        applyAdaptiveCanny(blurred, dst, canny_sigma);
    }
}