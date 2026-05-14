#ifndef SCAN_PROCESSOR_HPP
#define SCAN_PROCESSOR_HPP

#include "IPostprocessor.hpp"

namespace native_scanner {

    /**
     * @class ScanProcessor
     * @brief Implements the post-processing strategy for "SCAN" mode.
     * Focuses on enhancing readability by creating a clean, paper-like white background.
     */
    class ScanProcessor : public IPostprocessor {
    public:
        cv::Mat process(const cv::Mat& image, DocumentType type) override;
    };

}

#endif //SCAN_PROCESSOR_HPP