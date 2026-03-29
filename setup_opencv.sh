#!/bin/bash

PROJECT_ROOT=$(pwd)
NATIVE_DIR="$PROJECT_ROOT/libs/opencv"
OPENCV_VERSION="4.10.0"

ANDROID_SDK_URL="https://github.com/opencv/opencv/releases/download/$OPENCV_VERSION/opencv-$OPENCV_VERSION-android-sdk.zip"
IOS_FRAMEWORK_URL="https://github.com/opencv/opencv/releases/download/$OPENCV_VERSION/opencv-$OPENCV_VERSION-ios-framework.zip"

echo "Starting OpenCV multi-platform setup..."
mkdir -p "$NATIVE_DIR"
cd "$NATIVE_DIR" || exit

# --- Android SDK Setup ---
if [ -d "android" ]; then
    echo "Android: OpenCV SDK already exists."
else
    echo "Downloading Android SDK..."
    curl -L "$ANDROID_SDK_URL" -o opencv_android.zip
    unzip -q opencv_android.zip
    mv OpenCV-android-sdk android
    rm opencv_android.zip
    echo "Android setup completed."
fi

# --- iOS Framework Setup ---
if [ -d "ios" ]; then
    echo "iOS: OpenCV Framework already exists."
else
    echo "Downloading iOS Framework..."
    curl -L "$IOS_FRAMEWORK_URL" -o opencv_ios.zip
    unzip -q opencv_ios.zip
    # Unzipping results in an opencv2.framework directory
    mkdir -p ios
    mv opencv2.framework ios/
    rm opencv_ios.zip
    echo "iOS setup completed."
fi

echo "All platform libraries are ready."
