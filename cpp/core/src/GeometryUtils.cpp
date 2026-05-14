#include "GeometryUtils.h"

namespace native_scanner {

    std::vector<cv::Point2f> GeometryUtils::scalePoints(const std::vector<cv::Point2f> &pts, float scale_factor) {
        std::vector<cv::Point2f> scaled;
        scaled.reserve(pts.size());
        for (const auto &p: pts) {
            scaled.emplace_back(p.x * scale_factor, p.y * scale_factor);
        }
        return scaled;
    }

    DocumentFrame GeometryUtils::findLargestArea(const cv::Mat &edged, double min_area_ratio) {
        DocumentFrame result;
        std::vector<std::vector<cv::Point>> contours;

        // Find external contours only to optimize speed. Inner text edges are ignored.
        cv::findContours(edged, contours, cv::RETR_EXTERNAL, cv::CHAIN_APPROX_SIMPLE);

        double max_area = 0;
        std::vector<cv::Point> best_approx;
        const double total_area = edged.cols * edged.rows;

        for (const auto &contour: contours) {
            double area = cv::contourArea(contour);

            // Filtering: Ignore contours smaller than the minimum area threshold
            if (area < (total_area * min_area_ratio)) continue;

            std::vector<cv::Point> approx;
            double peri = cv::arcLength(contour, true);

            // Douglas-Peucker algorithm: Approximate the contour to a simpler polygon
            // 2% of the perimeter is a standard empirical value for document detection
            cv::approxPolyDP(contour, approx, 0.02 * peri, true);

            // A valid document MUST have exactly 4 corners and be a convex shape (no inward dents)
            if (approx.size() == 4 && cv::isContourConvex(approx)) {
                if (area > max_area && isValidShape(approx)) {
                    max_area = area;
                    best_approx = approx;
                }
            }
        }

        if (!best_approx.empty()) {
            result.is_detected = true;
            std::vector<cv::Point2f> temp_points;
            for (const auto &p: best_approx) temp_points.emplace_back(p.x, p.y);

            // Ensure points are always ordered (TL, TR, BR, BL) before returning
            result.points = orderPoints(temp_points);
            result.confidence = static_cast<float>(max_area / total_area);
        }

        return result;
    }

    std::vector<cv::Point2f> GeometryUtils::smoothPoints(
        const std::vector<cv::Point2f> &prev,
        const std::vector<cv::Point2f> &curr,
        float alpha) {

        if (prev.empty()) return curr;
        if (curr.empty()) return prev;

        std::vector<cv::Point2f> smoothed;
        smoothed.reserve(4);

        // Apply Exponential Moving Average (EMA) filter:
        // $P_{new} = \alpha \cdot P_{curr} + (1 - \alpha) \cdot P_{prev}$
        for (size_t i = 0; i < 4; ++i) {
            smoothed.push_back(prev[i] * (1.0f - alpha) + curr[i] * alpha);
        }
        return smoothed;
    }

    bool GeometryUtils::isValidShape(const std::vector<cv::Point> &approx) {
        if (approx.size() != 4) return false;

        double max_cos = 0;

        // 1. Angle Check: Ensure all 4 internal angles are close to 90 degrees.
        // Uses the dot product formula to find the cosine of the angle between two vectors.
        for (int i = 0; i < 4; i++) {
            cv::Point v1 = approx[i] - approx[(i + 1) % 4];
            cv::Point v2 = approx[(i + 2) % 4] - approx[(i + 1) % 4];

            double dot = (v1.x * v2.x + v1.y * v2.y);
            double mag1 = std::sqrt(v1.x * v1.x + v1.y * v1.y);
            double mag2 = std::sqrt(v2.x * v2.x + v2.y * v2.y);

            if (mag1 == 0 || mag2 == 0) return false;

            double cosine = std::abs(dot / (mag1 * mag2));
            max_cos = std::max(max_cos, cosine);
        }

        // A maximum cosine of 0.3 allows for angles roughly between 72 and 108 degrees.
        // If the shape is too skewed (e.g., a diamond), it is rejected.
        if (max_cos >= 0.3) return false;

        // 2. Aspect Ratio Check: Prevent extremely thin shapes (e.g., door gaps, table edges)
        // Calculates the average width and height using the distance between opposite corners.
        double d01 = cv::norm(approx[0] - approx[1]);
        double d12 = cv::norm(approx[1] - approx[2]);
        double d23 = cv::norm(approx[2] - approx[3]);
        double d30 = cv::norm(approx[3] - approx[0]);

        double avg_w = (d01 + d23) / 2.0;
        double avg_h = (d12 + d30) / 2.0;

        if (avg_h == 0) return false;

        double aspect_ratio = avg_w / avg_h;

        // Reject shapes that are thinner than a 1:5 ratio (0.2) or wider than 5:1 (5.0).
        // Standard paper (A4) is approx 1:1.414, Receipts can be up to 1:4.
        if (aspect_ratio < 0.2 || aspect_ratio > 5.0) {
            return false;
        }

        return true;
    }

    std::vector<cv::Point2f> GeometryUtils::orderPoints(const std::vector<cv::Point2f> &pts) {
        if (pts.size() != 4) return pts;

        std::vector<cv::Point2f> ordered(4);
        int tl_idx = 0, tr_idx = 0, br_idx = 0, bl_idx = 0;

        float min_sum = FLT_MAX, max_sum = -FLT_MAX;
        float min_diff = FLT_MAX, max_diff = -FLT_MAX;

        // Mathematical sorting approach (O(N) complexity):
        // - Top-Left corner will have the smallest (x + y) value.
        // - Bottom-Right corner will have the largest (x + y) value.
        // - Top-Right corner will have the smallest (y - x) value.
        // - Bottom-Left corner will have the largest (y - x) value.
        for (int i = 0; i < 4; ++i) {
            float sum = pts[i].x + pts[i].y;
            float diff = pts[i].y - pts[i].x;

            if (sum < min_sum) { min_sum = sum; tl_idx = i; }
            if (sum > max_sum) { max_sum = sum; br_idx = i; }
            if (diff < min_diff) { min_diff = diff; tr_idx = i; }
            if (diff > max_diff) { max_diff = diff; bl_idx = i; }
        }

        ordered[0] = pts[tl_idx];
        ordered[1] = pts[tr_idx];
        ordered[2] = pts[br_idx];
        ordered[3] = pts[bl_idx];

        return ordered;
    }
}