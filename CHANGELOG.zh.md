<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# JCE Support Plugin Changelog

[In English](CHANGELOG.md)

## [Unreleased]

## [0.3.4] - 2025-02-22

### Added

- 动态加载/卸载插件

### Fixed

- 修复2022.3版本开始生成单元测试功能不可用的问题。
- 修复结构体字段tag有问题时，结构体标题上的自动修复提示会重复。

### Changed

- 采用*IntelliJ Platform Gradle Plugin (2.x)*新构建插件。
- 修复新版本API兼容性问题。

## [0.3.3] - 2021-12-22

### Fix

- 修复Jce转Proto时可能会出现并发修改异常的问题.
- 重命名include文件名时可能会导致include变空的问题.

## [0.3.2] - 2021-11-07

### Fixed

- 修复同名module分布在多个文件时会解析不到其他文件中该module里的符号的问题.

### Added

- 支持自动转换为proto文件.

## [0.3.1] - 2020-05-05

### Added

- 更完善的自动补全.
- 支持识别自动生成的java代码并相互跳转.
- 自动生成测试用例.
- 支持tars.
