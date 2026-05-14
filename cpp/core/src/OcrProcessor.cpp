#include "OcrProcessor.hpp"

namespace native_scanner {

    cv::Mat OcrProcessor::process(const cv::Mat& image, DocumentType type) {
        cv::Mat gray, processed_image;
        cv::cvtColor(image, gray, cv::COLOR_BGR2GRAY);

        // A simple, global threshold is often sufficient for OCR preprocessing,
        // especially if the lighting is decent. Otsu's method automatically finds the optimal threshold value.
        cv::threshold(gray, processed_image, 0, 255, cv::THRESH_BINARY | cv::THRESH_OTSU);

        return processed_image;
    }
}
