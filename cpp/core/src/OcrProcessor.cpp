#include "OcrProcessor.hpp"
#include "ImagePreprocessor.h"
#include <opencv2/imgproc.hpp>

namespace native_scanner {

    cv::Mat OcrProcessor::process(const cv::Mat& image, DocumentType type) {
        cv::Mat gray, processed_image;
        cv::cvtColor(image, gray, cv::COLOR_BGR2GRAY);

        // 1. Apply shadow removal algorithm before binarization.
        // This eliminates information loss caused by uneven illumination at its source.
        ImagePreprocessor::removeShadows(gray, gray);

        // 2. Noise reduction preserving text detail (medianBlur)
        cv::medianBlur(gray, gray, 3);

        // 3. Text sharpening using Unsharp Mask
        // Uses the difference between blurred and original images to enhance edge definition.
        cv::Mat blurred;
        cv::GaussianBlur(gray, blurred, cv::Size(0, 0), 3);
        cv::addWeighted(gray, 1.5, blurred, -0.5, 0, gray);

        // 4. Adaptive local binarization
        int blockSize = 15;
        double C = 5;
        cv::adaptiveThreshold(gray, processed_image, 255, cv::ADAPTIVE_THRESH_GAUSSIAN_C,
                                cv::THRESH_BINARY, blockSize, C);

        return processed_image;
    }
}
