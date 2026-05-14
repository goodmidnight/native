#ifndef OCR_PROCESSOR_HPP
#define OCR_PROCESSOR_HPP

#include "IPostprocessor.hpp"

namespace native_scanner {

    /**
     * @class OcrProcessor
     * @brief Implements the post-processing strategy for "OCR" mode.
     * Optimizes the image for Optical Character Recognition engines by creating a sharp, binarized image.
     */
    class OcrProcessor : public IPostprocessor {
    public:
        cv::Mat process(const cv::Mat& image, DocumentType type) override;
    };

}

#endif //OCR_PROCESSOR_HPP