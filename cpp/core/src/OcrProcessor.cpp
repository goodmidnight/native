#include "OcrProcessor.hpp"
#include "ImagePreprocessor.h"
#include <opencv2/imgproc.hpp>

namespace native_scanner {

    cv::Mat OcrProcessor::process(const cv::Mat& image, DocumentType type) {
        cv::Mat gray, processed_image;
        cv::cvtColor(image, gray, cv::COLOR_BGR2GRAY);

        // 1. [신규] 그림자 제거 알고리즘을 이진화 전에 적용합니다.
        // 이를 통해 조명 불균일성으로 인한 정보 유실을 원천적으로 차단합니다.
        ImagePreprocessor::removeShadows(gray, gray);

        // 2. 텍스트 디테일 보존을 위한 노이즈 제거 (medianBlur)
        cv::medianBlur(gray, gray, 3);

        // 3. [신규] Unsharp Mask를 이용한 텍스트 선명화
        // 블러된 이미지와 원본 이미지의 차이를 이용하여 경계선을 더욱 뚜렷하게 만듭니다.
        cv::Mat blurred;
        cv::GaussianBlur(gray, blurred, cv::Size(0, 0), 3);
        cv::addWeighted(gray, 1.5, blurred, -0.5, 0, gray);

        // 4. 적응형 지역 이진화
        int blockSize = 15;
        double C = 5;
        cv::adaptiveThreshold(gray, processed_image, 255, cv::ADAPTIVE_THRESH_GAUSSIAN_C,
                                cv::THRESH_BINARY, blockSize, C);

        return processed_image;
    }
}
