#ifndef GEOMETRY_LOGIC_H
#define GEOMETRY_LOGIC_H

#include "ScannerType.h"
#include <opencv2/opencv.hpp>

namespace native_scanner {

    /**
     * @class GeometryUtils
     * @brief Provides mathematical and geometric operations for document shape detection and manipulation.
     */
    class GeometryUtils {
    public:
        /**
         * @brief Scales the coordinates of the points by a given scale factor.
         * Used to map coordinates from a downscaled preview image back to the high-resolution original image.
         * @param pts The vector of 2D points to scale.
         * @param scale_factor The multiplier to apply to both x and y coordinates.
         * @return std::vector<cv::Point2f> The scaled coordinates.
         */
        static std::vector<cv::Point2f> scalePoints(const std::vector<cv::Point2f> &pts, float scale_factor);

        /**
         * @brief Finds the largest valid quadrilateral area representing a document in an edge-detected image.
         * @param edged The preprocessed, binary edge image (e.g., output from Canny).
         * @param min_area_ratio The minimum area the document must occupy relative to the total image size (e.g., 0.2 for 20%).
         * @param type The document format type to calculate customized aspect-ratio scores.
         * @return DocumentFrame containing the detected points, confidence score, and detection status.
         */
        static DocumentFrame findLargestArea(const cv::Mat &edged, double min_area_ratio, DocumentType type = DocumentType::GENERAL);

        /**
         * @brief Applies Exponential Moving Average (EMA) to smooth points across consecutive frames.
         * This reduces UI jittering when tracking the document in a live camera preview.
         * @param prev The coordinates from the previous frame.
         * @param curr The current detected coordinates.
         * @param alpha The smoothing factor (0.0 to 1.0). Lower means smoother but slower response.
         * @return std::vector<cv::Point2f> The stabilized coordinates.
         */
        static std::vector<cv::Point2f> smoothPoints(
            const std::vector<cv::Point2f> &prev,
            const std::vector<cv::Point2f> &curr,
            float alpha = 0.3f
        );

    private:
        /**
         * @brief Validates if the given 4 points form a logical, physical document shape.
         * Checks internal angles (close to 90 degrees) and aspect ratio constraints.
         * @param approx The 4 points representing the approximated polygon.
         * @return true if the shape is a valid document, false otherwise.
         */
        static bool isValidShape(const std::vector<cv::Point> &approx);

        /**
         * @brief Orders 4 unordered points into a strict clockwise sequence: Top-Left, Top-Right, Bottom-Right, Bottom-Left.
         * Essential for correct perspective warping without twisting the image.
         * @param pts The 4 unordered points.
         * @return std::vector<cv::Point2f> The ordered points.
         */
        static std::vector<cv::Point2f> orderPoints(const std::vector<cv::Point2f> &pts);
    };
}

#endif