cmake_minimum_required(VERSION 3.22)
project(NativeScannerDesktop)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

list(APPEND CMAKE_PREFIX_PATH "/opt/homebrew")
find_package(OpenCV REQUIRED)

set(CORE_SOURCES
        ../core/scanner_engine.cpp
        ../core/src/image_preprocessor.cpp
        ../core/src/geometry_utils.cpp
        ../core/src/image_utils.cpp
        ../core/src/image_postprocessor.cpp
)

add_executable(NativeScannerDesktop
        main.cpp
        ${CORE_SOURCES}
)

target_include_directories(NativeScannerDesktop PRIVATE
        ${OpenCV_INCLUDE_DIRS}
        "${CMAKE_CURRENT_SOURCE_DIR}/../core"
        "${CMAKE_CURRENT_SOURCE_DIR}/../core/include"
)

target_link_libraries(NativeScannerDesktop PRIVATE ${OpenCV_LIBS})