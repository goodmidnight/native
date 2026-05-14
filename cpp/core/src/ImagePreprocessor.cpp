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
        int low_thresh = static_cast<int>(std::max(0.0, (1.0 - sigma) * median));
        int high_thresh = static_cast<int>(std::min(255.0, (1.0 + sigma) * median));

        cv::Canny(src, dst, low_thresh, high_thresh);
    }

    void ImagePreprocessor::preprocess(const cv::Mat &src, cv::Mat &dst, float canny_sigma, int custom_blur_size, bool low_light_mode) {
        cv::Mat gray;
        if (src.channels() == 3 || src.channels() == 4) {
            // 저대비 환경인지 판단하여 CLAHE를 동적으로 적용합니다.
            if (low_light_mode) {
                cv::Mat enhanced;
                applyLowLightEnhancement(src, enhanced);
                cv::cvtColor(enhanced, gray, cv::COLOR_BGR2GRAY);
            } else {
                cv::cvtColor(src, gray, cv::COLOR_BGR2GRAY);
            }
        } else {
            gray = src.clone();
        }

        // 1. 샤프닝(Sharpening) 커널 적용
        // 중심 픽셀 가중치를 높여 흐릿한 테두리의 명암 대비를 강제로 증폭시킵니다.
        cv::Mat sharpening_kernel = (cv::Mat_<float>(3, 3) <<
            0, -1, 0,
           -1,  5, -1,
            0, -1, 0);
        cv::filter2D(gray, gray, gray.depth(), sharpening_kernel);

        // 2. 동적 블러(Blur) 생략 로직 (라플라시안 분산 활용)
        cv::Mat laplacian, mean, stddev;
        cv::Laplacian(gray, laplacian, CV_64F);
        cv::meanStdDev(laplacian, mean, stddev);
        double variance = stddev.at<double>(0) * stddev.at<double>(0);

        cv::Mat blurred;
        if (variance > 150.0) {
            // 충분히 선명할 때만 노이즈 제거용 블러 적용
            applyDynamicBlur(gray, blurred, custom_blur_size);
        } else {
            // 흐릿한 경우 블러 연산을 건너뛰어 추가 픽셀 손실 방지
            blurred = gray;
        }

        // 3. 고도화된 Adaptive Canny 알고리즘 적용
        applyAdaptiveCanny(blurred, dst, canny_sigma);

        // 4. 모폴로지 닫기(Close) 연산
        // 노이즈나 빛 반사로 인해 점선처럼 끊어진 테두리를 하나의 실선 덩어리로 묶어줍니다.
        cv::Mat morph_kernel = cv::getStructuringElement(cv::MORPH_RECT, cv::Size(3, 3));
        cv::morphologyEx(dst, dst, cv::MORPH_CLOSE, morph_kernel);
    }

    void ImagePreprocessor::removeShadows(const cv::Mat &src, cv::Mat &dst) {
        if (src.channels() != 1) {
            // 그림자 제거는 그레이스케일 이미지에 적용해야 최적의 결과를 얻습니다.
            cv::cvtColor(src, dst, cv::COLOR_BGR2GRAY);
        } else {
            dst = src.clone();
        }

        // 1. 이미지의 전반적인 조명 패턴(배경)을 추정합니다.
        // medianBlur를 큰 커널 사이즈로 적용하여 텍스트 같은 작은 디테일을 모두 제거하고,
        // 그림자로 인해 어두워진 넓은 영역만 남깁니다.
        cv::Mat background;
        int kernel_size = static_cast<int>(dst.cols / 8) | 1; // 이미지 너비에 비례하는 동적 커널 사이즈
        cv::medianBlur(dst, background, kernel_size);

        // 2. 원본 이미지와 배경 이미지의 차이를 계산하여 조명을 균일하게 만듭니다.
        // cv::absdiff 대신 255를 더한 후 나누는 방식을 사용하여 더욱 자연스러운 결과를 얻습니다.
        // (이 방식은 조명 불균일성 보정에 널리 사용됩니다)
        cv::Mat result = 255 - (background - dst);

        // 3. 처리된 이미지가 너무 어두워지는 것을 방지하기 위해 밝기를 정규화합니다.
        cv::normalize(result, dst, 0, 255, cv::NORM_MINMAX);
    }
}