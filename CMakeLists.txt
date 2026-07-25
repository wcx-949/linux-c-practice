cmake_minimum_required(VERSION 3.10)
project(untitled C)

set(CMAKE_C_STANDARD 99)

# 查找 ncurses 库 (解决 undefined reference 报错的关键)
find_package(PkgConfig REQUIRED)
pkg_check_modules(NCURSES REQUIRED ncursesw)

# 添加可执行文件 (注意：这里直接写 hello.c，不要带 build 目录路径)
add_executable(untitled hello.c)

# 链接 ncurses 库
target_include_directories(untitled PRIVATE ${NCURSES_INCLUDE_DIRS})
target_link_libraries(untitled PRIVATE ${NCURSES_LIBRARIES})