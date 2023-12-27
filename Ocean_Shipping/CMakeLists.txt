cmake_minimum_required(VERSION 3.20)
project(ProjectDA2 VERSION 1.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 14)

add_executable(ProjectDA2 src/main.cpp src/menu.cpp include/menu.h src/graph.cpp src/station.cpp src/route.cpp)