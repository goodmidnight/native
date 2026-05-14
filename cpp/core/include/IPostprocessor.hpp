#ifndef I_POSTPROCESSOR_HPP
#define I_POSTPROCESSOR_HPP

#include <opencv2/opencv.hpp>
#include "ScannerType.h"

namespace native_scanner {

    /**
     * @class IPostprocessor
     * @brief Defines the interface for a post-processing strategy.
     * Each concrete implementation will provide a specific visual enhancement algorithm.
     */
    class IPostprocessor {
    public:
        virtual ~IPostprocessor() = default;

        /**
         * @brief Processes the input image and returns the enhanced result.
         * @param image The warped, rectangular document image.
         * @param type The type of document, which can influence processing logic.
         * @return The final, processed image.
         */
        virtual cv::Mat process(const cv::Mat& image, DocumentType type) = 0;
    };

}

#endif //I_POSTPROCESSOR_HPP