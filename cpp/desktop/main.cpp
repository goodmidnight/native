/**
 * @file main.cpp
 * @brief Desktop testing environment and GUI for tuning the Native Scanner Engine.
 */

#include <opencv2/opencv.hpp>
#include <iostream>
#include "ScannerEngine.hpp"

using namespace native_scanner;

// Global variables for OpenCV Trackbar callbacks (required due to OpenCV C-style API limitations)
int g_blur_val = 100;
int g_glare_val = 30; // Mapped as 0.03 * 1000 for integer trackbar
int g_canny_val = 33; // Mapped as 0.33 * 100 for integer trackbar
int g_target_w = 800;

int main() {
    ScannerConfig config;
    ScannerEngine engine(config);

    cv::VideoCapture cap(0);
    if (!cap.isOpened()) {
        std::cerr << "Failed to open camera." << std::endl;
        return -1;
    }

    // 1. Initialize GUI Window
    std::string winName = "Scanner Tuning GUI";
    cv::namedWindow(winName);

    // 2. Create Trackbars (Sliders) for real-time parameter tuning
    cv::createTrackbar("Blur Thresh", winName, &g_blur_val, 500);
    cv::createTrackbar("Glare (x1000)", winName, &g_glare_val, 100);
    cv::createTrackbar("Canny (x100)", winName, &g_canny_val, 100);
    cv::createTrackbar("Target Width", winName, &g_target_w, 2000);

    cv::Mat frame;
    while (true) {
        cap >> frame;
        if (frame.empty()) break;

        // 3. Apply GUI slider values to the Engine Configuration
        config.blur_threshold = static_cast<double>(g_blur_val);
        config.glare_threshold_general = g_glare_val / 1000.0;
        config.canny_sigma = g_canny_val / 100.0f;
        config.target_width = std::max(400, g_target_w); // Ensure minimum width to prevent crashes

        engine.updateConfig(config);

        // 4. Execute Real-time Detection & Visualization
        DocumentFrame docFrame = engine.detectDocument(frame, DocumentType::GENERAL, 0);

        cv::Mat display = frame.clone();
        if (docFrame.is_detected) {
            // Draw the detected geometric boundary (Green)
            for (int i = 0; i < 4; i++) {
                cv::line(display, docFrame.points[i], docFrame.points[(i + 1) % 4], cv::Scalar(0, 255, 0), 2);
            }
        }

        // Display current configuration data on the screen
        std::string info = "Blur: " + std::to_string(config.blur_threshold) +
                           " | Glare: " + std::to_string(config.glare_threshold_general);
        cv::putText(display, info, cv::Point(10, 30), cv::FONT_HERSHEY_SIMPLEX, 0.6, cv::Scalar(255, 255, 255), 2);

        cv::imshow(winName, display);

        int key = cv::waitKey(1);
        if (key == 'q' || key == 27) break; // Quit on 'q' or ESC

        // 5. Test High-Resolution Capture Pipeline on 'c' key press
        if (key == 'c' || key == 'C') {
            CaptureResult result = engine.captureDocument(frame, docFrame, frame.size(), ProcessingMode::SCAN, DocumentType::GENERAL, 0);
            if (result.status == CaptureStatus::SUCCESS) {
                cv::imshow("Result", result.image);
            } else {
                std::cout << "Capture Failed: " << result.message << " (Check logic thresholds)" << std::endl;
            }
        }
    }
    return 0;
}