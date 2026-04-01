#include "image_postprocessor.h"

namespace native_scanner {

    cv::Mat ImagePostprocessor::processForScan(const cv::Mat &warped, DocumentType type) {
        cv::Mat gray, result;
        // Convert to grayscale as color information is usually unnecessary for document scans and increases size.
        cv::cvtColor(warped, gray, cv::COLOR_BGR2GRAY);

        // Domain Logic: Kernel Size Adjustment
        // Receipts typically have more wrinkles, folds, and uneven lighting compared to standard paper.
        // We increase the median blur kernel size (31) for receipts to aggressively smooth out
        // these uneven shadows, whereas standard documents use a smaller kernel (21).
        int blur_size = (type == DocumentType::RECEIPT) ? 31 : 21;

        cv::Mat dilated, bg_img;

        // Step 1: Background Estimation
        // Dilation expands the bright areas (background paper) and shrinks the dark areas (text).
        // A 7x7 rectangular structuring element ensures most text strokes are completely erased.
        cv::dilate(gray, dilated, cv::getStructuringElement(cv::MORPH_RECT, cv::Size(7, 7)));

        // Median blur removes the remaining text traces and noise, leaving a pure estimation
        // of the paper's baseline illumination and shadows.
        cv::medianBlur(dilated, bg_img, blur_size);

        // Step 2: Foreground Extraction (Shadow Removal)
        cv::Mat diff;
        // Subtracting the estimated background from the original gray image leaves only the text and distinct marks.
        cv::absdiff(gray, bg_img, diff);

        // Invert the colors so the background becomes white (255) and text becomes dark.
        result = 255 - diff;

        // Step 3: Contrast Normalization
        // Stretches the pixel values across the full 0-255 range to maximize contrast,
        // making the background pure white and text pitch black.
        cv::normalize(result, result, 0, 255, cv::NORM_MINMAX, CV_8UC1);

        return result;
    }

    cv::Mat ImagePostprocessor::processForOCR(const cv::Mat &warped, DocumentType type) {
        cv::Mat gray, binary;
        cv::cvtColor(warped, gray, cv::COLOR_BGR2GRAY);

        // Domain Logic: Binarization Block Size
        // ID cards and Business cards often have complex backgrounds, embossed text, or watermarks.
        // A smaller block size (7) calculates the threshold over a tighter local area,
        // preserving fine details better than the standard size (11) which might wash them out.
        int block_size = (type == DocumentType::ID_CARD || type == DocumentType::BUSINESS_CARD) ? 7 : 11;

        // Step 1: Adaptive Gaussian Thresholding
        // Converts the grayscale image to strictly black and white.
        // Gaussian weighting handles uneven lighting across the document better than a global threshold.
        cv::adaptiveThreshold(gray, binary, 255,
                              cv::ADAPTIVE_THRESH_GAUSSIAN_C,
                              cv::THRESH_BINARY, block_size, 2);

        // Step 2: Morphological Opening (Noise Removal)
        // Opening (Erosion followed by Dilation) removes small white noise (salt noise)
        // from the black text areas, preventing OCR engines from mistaking noise for punctuation marks.
        cv::Mat kernel = cv::getStructuringElement(cv::MORPH_RECT, cv::Size(2, 2));
        cv::morphologyEx(binary, binary, cv::MORPH_OPEN, kernel);

        return binary;
    }
}