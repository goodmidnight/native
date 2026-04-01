#include "image_utils.h"

namespace native_scanner {

    float ImageUtils::downscale(const cv::Mat& src, cv::Mat& dst, int target_width) {
        // If the image is already smaller than the target, do nothing to avoid upscaling blur.
        if (src.cols <= target_width) {
            src.copyTo(dst);
            return 1.0f;
        }

        float scale = static_cast<float>(target_width) / static_cast<float>(src.cols);

        // INTER_AREA is the mathematically correct interpolation method for shrinking images.
        // It computes the average of the pixels being merged, preventing Moire patterns and jagged edges.
        cv::resize(src, dst, cv::Size(target_width, static_cast<int>(src.rows * scale)),
                   0, 0, cv::INTER_AREA);
        return scale;
    }

    cv::Mat ImageUtils::applyWarp(const cv::Mat& src, const std::vector<cv::Point2f>& corners, int interpolation) {
        // Calculate the physical distances between the corners using the Pythagorean theorem.
        // w1/w2 are the top and bottom widths. h1/h2 are the left and right heights.
        float w1 = std::sqrt(std::pow(corners[2].x - corners[3].x, 2) + std::pow(corners[2].y - corners[3].y, 2));
        float w2 = std::sqrt(std::pow(corners[1].x - corners[0].x, 2) + std::pow(corners[1].y - corners[0].y, 2));
        float h1 = std::sqrt(std::pow(corners[1].y - corners[2].y, 2) + std::pow(corners[1].x - corners[2].x, 2));
        float h2 = std::sqrt(std::pow(corners[0].y - corners[3].y, 2) + std::pow(corners[0].x - corners[3].x, 2));

        // The final canvas size must be large enough to hold the maximum calculated dimensions
        // to prevent squishing or clipping the document.
        float maxWidth = std::max(w1, w2);
        float maxHeight = std::max(h1, h2);

        // Define the perfect rectangular destination coordinates (Top-Left, Top-Right, Bottom-Right, Bottom-Left)
        std::vector<cv::Point2f> dst = {
            {0, 0}, {maxWidth, 0}, {maxWidth, maxHeight}, {0, maxHeight}
        };

        // Compute the 3x3 perspective transformation matrix and apply it
        cv::Mat matrix = cv::getPerspectiveTransform(corners, dst);
        cv::Mat warped;
        cv::warpPerspective(src, warped, matrix, cv::Size(static_cast<int>(maxWidth), static_cast<int>(maxHeight)), interpolation);

        return warped;
    }

    cv::Mat ImageUtils::rotateImage(const cv::Mat& src, int rotation_degrees) {
        cv::Mat dst;

        // Normalize the angle to strictly handle negative or extremely large degrees.
        // e.g., -90 % 360 = -90 -> + 360 = 270 % 360 = 270.
        int angle = ((rotation_degrees % 360) + 360) % 360;

        if (angle == 90) {
            cv::rotate(src, dst, cv::ROTATE_90_CLOCKWISE);
        } else if (angle == 180) {
            cv::rotate(src, dst, cv::ROTATE_180);
        } else if (angle == 270) {
            cv::rotate(src, dst, cv::ROTATE_90_COUNTERCLOCKWISE);
        } else {
            src.copyTo(dst); // 0 degrees (No rotation)
        }

        return dst;
    }
}