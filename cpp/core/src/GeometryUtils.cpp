#include "GeometryUtils.h"
#include <opencv2/imgproc.hpp>

namespace native_scanner {

    std::vector<cv::Point2f> GeometryUtils::scalePoints(const std::vector<cv::Point2f> &pts, float scale_factor) {
        std::vector<cv::Point2f> scaled;
        scaled.reserve(pts.size());
        for (const auto &p: pts) {
            scaled.emplace_back(p.x * scale_factor, p.y * scale_factor);
        }
        return scaled;
    }

    struct ContourCandidate {
        std::vector<cv::Point> contour;
        double area;
        double score;

        ContourCandidate(std::vector<cv::Point> c, double a) : contour(std::move(c)), area(a), score(0.0) {}

        bool operator>(const ContourCandidate& other) const {
            return score > other.score;
        }
    };

    DocumentFrame GeometryUtils::findLargestArea(const cv::Mat &edged, double min_area_ratio, DocumentType type) {
        DocumentFrame result;
        std::vector<std::vector<cv::Point>> contours;
        cv::findContours(edged, contours, cv::RETR_EXTERNAL, cv::CHAIN_APPROX_SIMPLE);

        if (contours.empty()) {
            return result;
        }

        const double total_area = edged.cols * edged.rows;
        const double max_area_ratio = 0.92; // Upper bound to reject false detection of entire preview area
        std::vector<ContourCandidate> candidates;

        for (const auto &contour: contours) {
            double area = cv::contourArea(contour);
            // Filter out contours that are too small or cover most of the screen
            if (area < (total_area * min_area_ratio) || area > (total_area * max_area_ratio)) {
                continue;
            }
            candidates.emplace_back(contour, area);
        }

        if (candidates.empty()) {
            return result;
        }

        cv::Point2f image_center(edged.cols / 2.0f, edged.rows / 2.0f);
        float max_dist_from_center = cv::norm(image_center); // Center to corner (0,0)

        for (auto& candidate : candidates) {
            // 1. Generate convex hull to smooth jagged edges caused by noise
            std::vector<cv::Point> hull;
            cv::convexHull(candidate.contour, hull);

            double peri = cv::arcLength(hull, true);
            std::vector<cv::Point> approx;
            cv::approxPolyDP(hull, approx, 0.025 * peri, true);

            double square_score = 0.0;
            std::vector<cv::Point> final_quad_points = approx;

            if (approx.size() == 4) {
                square_score = 1.0;
            } else if (approx.size() > 4) {
                // 2. When vertex count is 4+ (5~8), extract four extreme corner points to simplify into a quadrilateral
                std::vector<cv::Point> extreme_pts(4);
                int tl_idx = 0, tr_idx = 0, br_idx = 0, bl_idx = 0;
                float min_sum = FLT_MAX, max_sum = -FLT_MAX;
                float min_diff = FLT_MAX, max_diff = -FLT_MAX;

                for (size_t i = 0; i < approx.size(); ++i) {
                    float sum = static_cast<float>(approx[i].x + approx[i].y);
                    float diff = static_cast<float>(approx[i].y - approx[i].x);

                    if (sum < min_sum) { min_sum = sum; tl_idx = i; }
                    if (sum > max_sum) { max_sum = sum; br_idx = i; }
                    if (diff < min_diff) { min_diff = diff; tr_idx = i; }
                    if (diff > max_diff) { max_diff = diff; bl_idx = i; }
                }

                extreme_pts[0] = approx[tl_idx];
                extreme_pts[1] = approx[tr_idx];
                extreme_pts[2] = approx[br_idx];
                extreme_pts[3] = approx[bl_idx];

                // Check for duplicate vertices among the 4 extracted corner points
                bool duplicates = false;
                for (int i = 0; i < 4; ++i) {
                    for (int j = i + 1; j < 4; ++j) {
                        if (extreme_pts[i] == extreme_pts[j]) {
                            duplicates = true;
                            break;
                        }
                    }
                }

                if (!duplicates) {
                    final_quad_points = extreme_pts;
                    // Allow detection even if not a perfect quadrilateral, with a mild penalty (0.75)
                    square_score = 0.75;
                } else {
                    square_score = 0.0;
                }
            }

            // 3. Remove detections touching screen borders (false positive filter)
            if (square_score > 0.0 && final_quad_points.size() == 4) {
                int margin = 3;
                int border_touch_count = 0;
                for (const auto &p : final_quad_points) {
                    if (p.x <= margin || p.x >= edged.cols - margin ||
                        p.y <= margin || p.y >= edged.rows - margin) {
                        border_touch_count++;
                    }
                }
                if (border_touch_count >= 3) {
                    square_score = 0.0; // Falsely detected preview border as an edge
                }
            }

            double convexity_score = (square_score > 0.0 && cv::isContourConvex(final_quad_points)) ? 1.0 : 0.0;
            double area_score = candidate.area / total_area;

            // 4. Centrality Score: Penalize contours far from the image center.
            double centrality_score = 0.0;
            cv::Moments M = cv::moments(candidate.contour);
            if (M.m00 > 0) {
                cv::Point2f contour_center(M.m10 / M.m00, M.m01 / M.m00);
                float dist = cv::norm(image_center - contour_center);
                centrality_score = std::max(0.0, 1.0 - (dist / max_dist_from_center));
            }

            // 5. Adaptive aspect ratio scoring based on ideal ratios per document type
            double aspect_ratio_score = 0.0;
            if (square_score > 0.0 && final_quad_points.size() == 4) {
                double d01 = cv::norm(final_quad_points[0] - final_quad_points[1]);
                double d12 = cv::norm(final_quad_points[1] - final_quad_points[2]);
                double d23 = cv::norm(final_quad_points[2] - final_quad_points[3]);
                double d30 = cv::norm(final_quad_points[3] - final_quad_points[0]);

                double avg_w = (d01 + d23) / 2.0;
                double avg_h = (d12 + d30) / 2.0;

                if (avg_h > 0.0 && avg_w > 0.0) {
                    double norm_ratio = std::max(avg_w, avg_h) / std::min(avg_w, avg_h);

                    double ideal_min = 1.0;
                    double ideal_max = 2.0;

                    if (type == DocumentType::GENERAL) {
                        ideal_min = 1.2; // A4 (1.414)
                        ideal_max = 1.8;
                    } else if (type == DocumentType::ID_CARD || type == DocumentType::BUSINESS_CARD) {
                        ideal_min = 1.4; // Credit card (1.586), business card (1.8)
                        ideal_max = 2.1;
                    } else if (type == DocumentType::RECEIPT) {
                        ideal_min = 1.5; // Allow very narrow and long receipts
                        ideal_max = 6.0;
                    }

                    if (norm_ratio >= ideal_min && norm_ratio <= ideal_max) {
                        aspect_ratio_score = 1.0;
                    } else {
                        // Apply decay formula so score decreases as ratio deviates from ideal range
                        double diff = 0.0;
                        if (norm_ratio < ideal_min) diff = ideal_min - norm_ratio;
                        else diff = norm_ratio - ideal_max;
                        aspect_ratio_score = std::max(0.0, 1.0 - diff);
                    }
                }
            }

            // Weighted composite score: lower weight on raw area, higher on shape and centrality
            candidate.score = (square_score * 0.3) +
                              (convexity_score * 0.15) +
                              (centrality_score * 0.2) +
                              (area_score * 0.15) +
                              (aspect_ratio_score * 0.2);
        }

        std::sort(candidates.begin(), candidates.end(), std::greater<ContourCandidate>());
        ContourCandidate best_candidate = candidates[0];

        // Extract quadrilateral from the top candidate using the same extreme point technique
        std::vector<cv::Point> best_hull;
        cv::convexHull(best_candidate.contour, best_hull);

        std::vector<cv::Point> best_approx;
        double peri = cv::arcLength(best_hull, true);
        cv::approxPolyDP(best_hull, best_approx, 0.025 * peri, true);

        std::vector<cv::Point2f> temp_points;

        if (best_candidate.score > 0.3) {
            if (best_approx.size() == 4) {
                result.is_detected = true;
                for (const auto &p: best_approx) {
                    temp_points.emplace_back(static_cast<float>(p.x), static_cast<float>(p.y));
                }
            } else if (best_approx.size() > 4) {
                std::vector<cv::Point> extreme_pts(4);
                int tl_idx = 0, tr_idx = 0, br_idx = 0, bl_idx = 0;
                float min_sum = FLT_MAX, max_sum = -FLT_MAX;
                float min_diff = FLT_MAX, max_diff = -FLT_MAX;

                for (size_t i = 0; i < best_approx.size(); ++i) {
                    float sum = static_cast<float>(best_approx[i].x + best_approx[i].y);
                    float diff = static_cast<float>(best_approx[i].y - best_approx[i].x);

                    if (sum < min_sum) { min_sum = sum; tl_idx = i; }
                    if (sum > max_sum) { max_sum = sum; br_idx = i; }
                    if (diff < min_diff) { min_diff = diff; tr_idx = i; }
                    if (diff > max_diff) { max_diff = diff; bl_idx = i; }
                }

                extreme_pts[0] = best_approx[tl_idx];
                extreme_pts[1] = best_approx[tr_idx];
                extreme_pts[2] = best_approx[br_idx];
                extreme_pts[3] = best_approx[bl_idx];

                bool duplicates = false;
                for (int i = 0; i < 4; ++i) {
                    for (int j = i + 1; j < 4; ++j) {
                        if (extreme_pts[i] == extreme_pts[j]) {
                            duplicates = true;
                            break;
                        }
                    }
                }

                if (!duplicates) {
                    result.is_detected = true;
                    for (const auto &p: extreme_pts) {
                        temp_points.emplace_back(static_cast<float>(p.x), static_cast<float>(p.y));
                    }
                }
            }

            if (result.is_detected) {
                result.points = orderPoints(temp_points);
                result.confidence = static_cast<float>(best_candidate.score);
            }
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

        for (size_t i = 0; i < 4; ++i) {
            smoothed.push_back(prev[i] * (1.0f - alpha) + curr[i] * alpha);
        }
        return smoothed;
    }

    bool GeometryUtils::isValidShape(const std::vector<cv::Point> &approx) {
        if (approx.size() != 4) return false;

        double max_cos = 0;

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

        if (max_cos >= 0.3) return false;

        double d01 = cv::norm(approx[0] - approx[1]);
        double d12 = cv::norm(approx[1] - approx[2]);
        double d23 = cv::norm(approx[2] - approx[3]);
        double d30 = cv::norm(approx[3] - approx[0]);

        double avg_w = (d01 + d23) / 2.0;
        double avg_h = (d12 + d30) / 2.0;

        if (avg_h == 0) return false;

        double aspect_ratio = avg_w / avg_h;

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