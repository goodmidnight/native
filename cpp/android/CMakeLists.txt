cmake_minimum_required(VERSION 3.22.1)
project("native-scanner")

if(NOT OpenCV_DIR)
    set(OpenCV_DIR ${CMAKE_SOURCE_DIR}/../../native/jni)
endif()

find_package(OpenCV REQUIRED)

include_directories(${CMAKE_SOURCE_DIR}/../core)
include_directories(${CMAKE_SOURCE_DIR}/../core/include)

set(SOURCE_FILES
        ScannerWrapperJni.cpp
        ../core/ScannerEngine.cpp
        ../core/src/GeometryUtils.cpp
    ../core/src/image_postprocessor.cpp
        ../core/src/ImagePreprocessor.cpp
        ../core/src/ImageUtils.cpp
)

add_library(native-scanner SHARED ${SOURCE_FILES})

target_link_libraries(
    native-scanner
    ${OpenCV_LIBS}
    log
    jnigraphics
)