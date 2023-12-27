cmake_minimum_required(VERSION 3.20)
project(DA VERSION 1.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 14)

add_executable(DA src/main.cpp src/menu.cpp src/menu.h src/graph.cpp src/station.cpp src/networkline.cpp)