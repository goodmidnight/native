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
        double low_thresh = std::max(0.0, (1.0 - sigma) * median);
        double high_thresh = std::min(255.0, (1.0 + sigma) * median);

        cv::Canny(src, dst, low_thresh, high_thresh);
    }

    void ImagePreprocessor::preprocess(const cv::Mat &src, cv::Mat &dst, float canny_sigma, int custom_blur_size, bool low_light_mode, DocumentType type) {
        cv::Mat gray;
        bool applied_low_light = low_light_mode;

        if (src.channels() == 3 || src.channels() == 4) {
            if (applied_low_light) {
                cv::Mat enhanced;
                applyLowLightEnhancement(src, enhanced);
                cv::cvtColor(enhanced, gray, cv::COLOR_BGR2GRAY);
            } else {
                cv::cvtColor(src, gray, cv::COLOR_BGR2GRAY);
            }
        } else {
            gray = src.clone();
        }

        // 1. 자동 저조도 감지 로직
        double avg_brightness = cv::mean(gray)[0];
        if (!applied_low_light && avg_brightness < 80.0) {
            applied_low_light = true;
            if (src.channels() == 3 || src.channels() == 4) {
                cv::Mat enhanced;
                applyLowLightEnhancement(src, enhanced);
                cv::cvtColor(enhanced, gray, cv::COLOR_BGR2GRAY);
            }
            // 어두운 환경이므로 Canny 에지 검출 감도를 높이기 위해 canny_sigma 임계값 간격을 다이내믹하게 보정
            canny_sigma *= 0.75f;
        }

        // 2. 문서 유형별 샤프닝(Sharpening) 필터 커스터마이징
        cv::Mat sharpening_kernel;
        if (type == DocumentType::ID_CARD || type == DocumentType::BUSINESS_CARD) {
            // 신분증이나 명함은 작은 텍스트와 세밀한 경계선 대비가 더 중요하므로 샤프닝을 다소 강화
            sharpening_kernel = (cv::Mat_<float>(3, 3) <<
                 0, -1.2f,  0,
              -1.2f,  5.8f, -1.2f,
                 0, -1.2f,  0);
        } else {
            // 일반 문서 및 영수증 표준 샤프닝
            sharpening_kernel = (cv::Mat_<float>(3, 3) <<
                 0, -1.f,  0,
                -1.f,  5.f, -1.f,
                 0, -1.f,  0);
        }
        cv::filter2D(gray, gray, gray.depth(), sharpening_kernel);

        // 3. 동적 블러(Blur) 생략/조율 임계값 커스터마이징
        cv::Mat laplacian, mean, stddev;
        cv::Laplacian(gray, laplacian, CV_64F);
        cv::meanStdDev(laplacian, mean, stddev);
        double variance = stddev.at<double>(0) * stddev.at<double>(0);

        cv::Mat blurred;
        double blur_threshold = 150.0;
        if (type == DocumentType::ID_CARD || type == DocumentType::BUSINESS_CARD) {
            // 신분증의 정교한 에지가 번지는 것을 적극 방지하기 위해 블러 적용 기준을 다소 높여 제한적으로 블러링 적용
            blur_threshold = 220.0;
        }

        if (variance > blur_threshold) {
            // 충분히 선명할 때만 노이즈 제거용 블러 적용
            int k_size = custom_blur_size;
            if ((type == DocumentType::ID_CARD || type == DocumentType::BUSINESS_CARD) && k_size <= 0) {
                k_size = (gray.cols / 300) | 1;
            }
            applyDynamicBlur(gray, blurred, k_size);
        } else {
            // 흐릿한 경우 블러 연산을 건너뛰어 추가 픽셀 손실 방지
            blurred = gray;
        }

        // 4. 고도화된 Adaptive Canny 알고리즘 적용
        applyAdaptiveCanny(blurred, dst, canny_sigma);

        // 5. 문서 유형별 모폴로지 닫기(Close) 커널 크기 조율
        int morph_size = 5;
        if (type == DocumentType::RECEIPT) {
            // 영수증은 빽빽한 텍스트 및 용지 주름 등으로 인해 엣지가 파편화되기 쉬우므로 7x7로 상향하여 엣지 연결성 강화
            morph_size = 7;
        }

        cv::Mat morph_kernel = cv::getStructuringElement(cv::MORPH_RECT, cv::Size(morph_size, morph_size));
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