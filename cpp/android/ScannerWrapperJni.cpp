/**
 * @file ScannerWrapperJni.cpp
 * @brief JNI Bridge connecting the Android Java/Kotlin environment with the C++ Scanner Engine.
 */

#include <jni.h>
#include <android/bitmap.h>
#include <android/log.h>
#include <string>
#include <vector>
#include <functional>
#include "ScannerEngine.hpp"

using namespace native_scanner;

#define JNI_METHOD(METHOD_NAME) Java_io_goodmidnight_scanner_jni_NativeScanner_##METHOD_NAME
#define LOG_TAG "NativeScanner"

// ============================================================================
// Global Reference Cache - Eliminates reflection overhead
// ============================================================================
JavaVM* g_vm = nullptr;

jclass g_DocumentFrameClass = nullptr;
jmethodID g_DocumentFrameConstructor = nullptr;
jfieldID g_FrameIsDetectedField = nullptr;
jfieldID g_FramePointsField = nullptr;

jclass g_CaptureResultClass = nullptr;
jmethodID g_CaptureResultConstructor = nullptr;

jclass g_BitmapClass = nullptr;
jmethodID g_BitmapCreateMethod = nullptr;
jobject g_BitmapConfigArgb8888 = nullptr;

// ============================================================================
// JNI Lifecycle Management
// ============================================================================
extern "C" JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    g_vm = vm;
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    // [NOTE] Must match the exact package path where the Kotlin class resides.
    // Use slash (/) as separator if the class is in a different package (e.g., domain.model).
    const char* frameClassName = "io/goodmidnight/scanner/model/DocumentFrame";
    const char* resultClassName = "io/goodmidnight/scanner/model/CaptureResult";

    // 1. DocumentFrame caching and validation
    jclass localFrameCls = env->FindClass(frameClassName);
    if (!localFrameCls) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return JNI_ERR;
    }
    g_DocumentFrameClass = reinterpret_cast<jclass>(env->NewGlobalRef(localFrameCls));
    g_DocumentFrameConstructor = env->GetMethodID(g_DocumentFrameClass, "<init>", "([FZFZ)V");
    g_FrameIsDetectedField = env->GetFieldID(g_DocumentFrameClass, "isDetected", "Z");
    g_FramePointsField = env->GetFieldID(g_DocumentFrameClass, "points", "[F");
    env->DeleteLocalRef(localFrameCls);

    // 2. CaptureResult caching and validation
    jclass localResultCls = env->FindClass(resultClassName);
    if (!localResultCls) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return JNI_ERR;
    }
    g_CaptureResultClass = reinterpret_cast<jclass>(env->NewGlobalRef(localResultCls));
    g_CaptureResultConstructor = env->GetMethodID(g_CaptureResultClass, "<init>", "(Landroid/graphics/Bitmap;IZZLjava/lang/String;)V");
    env->DeleteLocalRef(localResultCls);

    // 3. Android Bitmap and Config caching
    jclass localBitmapCls = env->FindClass("android/graphics/Bitmap");
    if (!localBitmapCls) {
        env->ExceptionClear();
        return JNI_ERR;
    }
    g_BitmapClass = reinterpret_cast<jclass>(env->NewGlobalRef(localBitmapCls));
    g_BitmapCreateMethod = env->GetStaticMethodID(g_BitmapClass, "createBitmap", "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    env->DeleteLocalRef(localBitmapCls);

    jclass localConfigCls = env->FindClass("android/graphics/Bitmap$Config");
    jfieldID argb8888Fid = env->GetStaticFieldID(localConfigCls, "ARGB_8888", "Landroid/graphics/Bitmap$Config;");
    jobject localConfigObj = env->GetStaticObjectField(localConfigCls, argb8888Fid);
    g_BitmapConfigArgb8888 = env->NewGlobalRef(localConfigObj);
    env->DeleteLocalRef(localConfigCls);
    env->DeleteLocalRef(localConfigObj);

    return JNI_VERSION_1_6;
}

extern "C" JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return;
    }
    if (g_DocumentFrameClass) env->DeleteGlobalRef(g_DocumentFrameClass);
    if (g_CaptureResultClass) env->DeleteGlobalRef(g_CaptureResultClass);
    if (g_BitmapClass) env->DeleteGlobalRef(g_BitmapClass);
    if (g_BitmapConfigArgb8888) env->DeleteGlobalRef(g_BitmapConfigArgb8888);
}

// ============================================================================
// Utility Functions
// ============================================================================

ScannerConfig extractConfig(JNIEnv* env, jobject jconfig) {
    jclass cls = env->GetObjectClass(jconfig);
    ScannerConfig config;
    config.target_width = env->GetIntField(jconfig, env->GetFieldID(cls, "targetWidth", "I"));
    config.canny_sigma = env->GetFloatField(jconfig, env->GetFieldID(cls, "cannySigma", "F"));
    config.min_area_ratio = env->GetDoubleField(jconfig, env->GetFieldID(cls, "minAreaRatio", "D"));
    config.low_light_mode = env->GetBooleanField(jconfig, env->GetFieldID(cls, "lowLightMode", "Z"));
    return config;
}

cv::Mat bitmapToMat(JNIEnv* env, jobject bitmap) {
    AndroidBitmapInfo info;
    void* pixels;
    cv::Mat dst;

    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) return dst;
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) return dst;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) return dst;

    dst.create(info.height, info.width, CV_8UC4);
    memcpy(dst.data, pixels, info.height * info.width * 4);
    AndroidBitmap_unlockPixels(env, bitmap);
    return dst;
}

jobject matToBitmap(JNIEnv* env, const cv::Mat& src) {
    if (src.empty()) return nullptr;

    cv::Mat rgba;
    if (src.channels() == 1) {
        cv::cvtColor(src, rgba, cv::COLOR_GRAY2RGBA);
    } else {
        cv::cvtColor(src, rgba, cv::COLOR_BGR2RGBA);
    }

    jobject jbitmap = env->CallStaticObjectMethod(
            g_BitmapClass,
            g_BitmapCreateMethod,
            rgba.cols,
            rgba.rows,
            g_BitmapConfigArgb8888
    );

    void* bitmapPixels;
    AndroidBitmap_lockPixels(env, jbitmap, &bitmapPixels);
    memcpy(bitmapPixels, rgba.data, rgba.total() * rgba.elemSize());
    AndroidBitmap_unlockPixels(env, jbitmap);

    return jbitmap;
}

// ============================================================================
// JNI Entry Point Functions
// ============================================================================

extern "C" JNIEXPORT jlong JNICALL
JNI_METHOD(nativeInit)(JNIEnv* env, jobject thiz, jobject jconfig) {
    ScannerConfig config = extractConfig(env, jconfig);
    auto* engine = new ScannerEngine(config);
    return reinterpret_cast<jlong>(engine);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(nativeUpdateConfig)(JNIEnv* env, jobject thiz, jlong ptr, jobject jconfig){
    ScannerConfig config = extractConfig(env, jconfig);
    auto* engine = reinterpret_cast<ScannerEngine*>(ptr);
    engine->updateConfig(config);
}

extern "C" JNIEXPORT void JNICALL
JNI_METHOD(nativeRelease)(JNIEnv* env, jobject thiz, jlong ptr) {
    auto* engine = reinterpret_cast<ScannerEngine*>(ptr);
    if (engine) {
        engine->setLogger(nullptr);
    delete engine;
}
}

/**
 * @brief Registers an Android logger callback with the C++ engine.
 */
extern "C" JNIEXPORT void JNICALL
JNI_METHOD(nativeSetLogger)(JNIEnv* env, jobject thiz, jlong ptr) {
    auto* engine = reinterpret_cast<ScannerEngine*>(ptr);
    if (!engine) return;

    auto android_logger = [](LogLevel level, const std::string& msg) {
        JNIEnv* jni_env;
        int get_env_stat = g_vm->GetEnv(reinterpret_cast<void**>(&jni_env), JNI_VERSION_1_6);
        if (get_env_stat == JNI_EDETACHED) {
            if (g_vm->AttachCurrentThread(&jni_env, nullptr) != 0) {
                return;
            }
        }

        android_LogPriority android_level = ANDROID_LOG_DEFAULT;
        switch (level) {
            case LogLevel::DEBUG: android_level = ANDROID_LOG_DEBUG; break;
            case LogLevel::INFO:  android_level = ANDROID_LOG_INFO;  break;
            case LogLevel::WARN:  android_level = ANDROID_LOG_WARN;  break;
            case LogLevel::ERROR: android_level = ANDROID_LOG_ERROR; break;
        }

        __android_log_print(android_level, LOG_TAG, "%s", msg.c_str());

        if (get_env_stat == JNI_EDETACHED) {
            g_vm->DetachCurrentThread();
        }
        };

    engine->setLogger(android_logger);
    }

extern "C" JNIEXPORT jobject JNICALL
JNI_METHOD(nativeDetect)(JNIEnv* env, jobject thiz, jlong ptr, jobject bitmap, jint type, jint rotation) {
    auto* engine = reinterpret_cast<ScannerEngine*>(ptr);
    if (!engine) return nullptr;

    cv::Mat preview_mat = bitmapToMat(env, bitmap);
    DocumentFrame frame = engine->detectDocument(preview_mat, static_cast<DocumentType>(type), rotation);

    jfloatArray jpoints = env->NewFloatArray(8);
    if (frame.is_detected && frame.points.size() == 4) {
        float pts[8] = {
                frame.points[0].x, frame.points[0].y,
                frame.points[1].x, frame.points[1].y,
                frame.points[2].x, frame.points[2].y,
                frame.points[3].x, frame.points[3].y
        };
        env->SetFloatArrayRegion(jpoints, 0, 8, pts);
    }
    return env->NewObject(
            g_DocumentFrameClass,
            g_DocumentFrameConstructor,
            jpoints,
            static_cast<jboolean>(frame.is_detected),
            static_cast<jfloat>(frame.confidence),
            static_cast<jboolean>(frame.is_stable)
    );
}

extern "C" JNIEXPORT jobject JNICALL
JNI_METHOD(nativeCapture)(JNIEnv* env, jobject thiz, jlong ptr, jobject bitmap, jobject jframe, jint pWidth, jint pHeight, jint mode, jint type, jint rotation) {
    auto* engine = reinterpret_cast<ScannerEngine*>(ptr);
    if (!engine) return nullptr;

    jboolean is_detected = env->GetBooleanField(jframe, g_FrameIsDetectedField);
    jobject jpoints_obj = env->GetObjectField(jframe, g_FramePointsField);

    jfloatArray jpoints_array = static_cast<jfloatArray>(jpoints_obj);
    jfloat* points_ptr = env->GetFloatArrayElements(jpoints_array, nullptr);

    DocumentFrame frame;
    frame.is_detected = is_detected;
    if (is_detected) {
        frame.points.push_back(cv::Point2f(points_ptr[0], points_ptr[1]));
        frame.points.push_back(cv::Point2f(points_ptr[2], points_ptr[3]));
        frame.points.push_back(cv::Point2f(points_ptr[4], points_ptr[5]));
        frame.points.push_back(cv::Point2f(points_ptr[6], points_ptr[7]));
    }
    env->ReleaseFloatArrayElements(jpoints_array, points_ptr, JNI_ABORT);

    cv::Mat src_mat = bitmapToMat(env, bitmap);
    cv::Size preview_size(pWidth, pHeight);

    CaptureResult result = engine->captureDocument(
            src_mat, frame, preview_size,
            static_cast<ProcessingMode>(mode),
            static_cast<DocumentType>(type),
            rotation
    );

    jobject resultBitmap = nullptr;
    if (!result.image.empty()) {
        resultBitmap = matToBitmap(env, result.image);
    }

    jstring jmessage = env->NewStringUTF(result.message.c_str());

    return env->NewObject(
            g_CaptureResultClass,
            g_CaptureResultConstructor,
            resultBitmap,
            static_cast<jint>(result.status),
            static_cast<jboolean>(result.is_blurry),
            static_cast<jboolean>(result.has_glare),
            jmessage
    );
}
