#include "ImagePreprocessor.h"
#include <opencv2/imgproc.hpp>
#include <numeric>
#include <algorithm>

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
        double low_thresh = std::max(0.0, (1.0 - sigma) * median);
        double high_thresh = std::min(255.0, (1.0 + sigma) * median);

        cv::Canny(src, dst, low_thresh, high_thresh);
    }

    void ImagePreprocessor::preprocess(const cv::Mat &src, cv::Mat &dst, float canny_sigma, int custom_blur_size, bool low_light_mode, DocumentType type) {
        cv::Mat gray;
        bool applied_low_light = low_light_mode;

        if (src.channels() == 3 || src.channels() == 4) {
            if (applied_low_light) {
                cv::Mat enhanced;
                applyLowLightEnhancement(src, enhanced);
                cv::cvtColor(enhanced, gray, cv::COLOR_BGR2GRAY);
            } else {
                cv::cvtColor(src, gray, cv::COLOR_BGR2GRAY);
            }
        } else {
            gray = src.clone();
        }

        // 1. Auto low-light detection
        double avg_brightness = cv::mean(gray)[0];
        if (!applied_low_light && avg_brightness < 80.0) {
            applied_low_light = true;
            if (src.channels() == 3 || src.channels() == 4) {
                cv::Mat enhanced;
                applyLowLightEnhancement(src, enhanced);
                cv::cvtColor(enhanced, gray, cv::COLOR_BGR2GRAY);
            }
            // Dark environment: dynamically adjust canny_sigma to increase Canny edge detection sensitivity
            canny_sigma *= 0.75f;
        }

        // 2. Document type-specific sharpening filter customization
        cv::Mat sharpening_kernel;
        if (type == DocumentType::ID_CARD || type == DocumentType::BUSINESS_CARD) {
            // ID cards and business cards require stronger sharpening for small text and fine edge contrast
            sharpening_kernel = (cv::Mat_<float>(3, 3) <<
                 0, -1.2f,  0,
              -1.2f,  5.8f, -1.2f,
                 0, -1.2f,  0);
        } else {
            // Standard sharpening for general documents and receipts
            sharpening_kernel = (cv::Mat_<float>(3, 3) <<
                 0, -1.f,  0,
                -1.f,  5.f, -1.f,
                 0, -1.f,  0);
        }
        cv::filter2D(gray, gray, gray.depth(), sharpening_kernel);

        // 3. Dynamic blur skip/threshold customization per document type
        cv::Mat laplacian, mean, stddev;
        cv::Laplacian(gray, laplacian, CV_64F);
        cv::meanStdDev(laplacian, mean, stddev);
        double variance = stddev.at<double>(0) * stddev.at<double>(0);

        cv::Mat blurred;
        double blur_threshold = 150.0;
        if (type == DocumentType::ID_CARD || type == DocumentType::BUSINESS_CARD) {
            // Raise blur threshold for ID cards to prevent fine edges from smearing
            blur_threshold = 220.0;
        }

        if (variance > blur_threshold) {
            // Apply noise-reduction blur only when image is sharp enough
            int k_size = custom_blur_size;
            if ((type == DocumentType::ID_CARD || type == DocumentType::BUSINESS_CARD) && k_size <= 0) {
                k_size = (gray.cols / 300) | 1;
            }
            applyDynamicBlur(gray, blurred, k_size);
        } else {
            // Skip blur when image is already blurry to prevent further pixel loss
            blurred = gray;
        }

        // 4. Apply advanced adaptive Canny algorithm
        applyAdaptiveCanny(blurred, dst, canny_sigma);

        // 5. Morphological close kernel size tuning per document type
        int morph_size = 5;
        if (type == DocumentType::RECEIPT) {
            // Receipts have dense text and paper wrinkles that fragment edges; use 7x7 kernel to strengthen edge connectivity
            morph_size = 7;
        }

        cv::Mat morph_kernel = cv::getStructuringElement(cv::MORPH_RECT, cv::Size(morph_size, morph_size));
        cv::morphologyEx(dst, dst, cv::MORPH_CLOSE, morph_kernel);
    }

    void ImagePreprocessor::removeShadows(const cv::Mat &src, cv::Mat &dst) {
        if (src.channels() != 1) {
            // Shadow removal should be applied to grayscale images for optimal results.
            cv::cvtColor(src, dst, cv::COLOR_BGR2GRAY);
        } else {
            dst = src.clone();
        }

        // 1. Estimate the overall illumination pattern (background) of the image.
        // Apply medianBlur with a large kernel to remove small details like text,
        // leaving only the broad darkened areas caused by shadows.
        cv::Mat background;
        int kernel_size = static_cast<int>(dst.cols / 8) | 1; // Dynamic kernel size proportional to image width
        cv::medianBlur(dst, background, kernel_size);

        // 2. Calculate the difference between the original and background images to normalize illumination.
        // Instead of cv::absdiff, use 255 - (background - original) for more natural results.
        // (This method is widely used for illumination non-uniformity correction)
        cv::Mat result = 255 - (background - dst);

        // 3. Normalize brightness to prevent the processed image from becoming too dark.
        cv::normalize(result, dst, 0, 255, cv::NORM_MINMAX);
    }
}