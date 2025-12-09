<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# JCE Support Plugin Changelog

[点我查看中文版](CHANGELOG.zh.md)

## [Unreleased]

## [0.3.5] - 2025-12-09

### Added

- Support navigation to callback/proxy/promise/impl Java class from JCE/TARS file.

### Fixed

- Wrongly show error for default value `0.0` for double field.

### Thanks

- 感谢[xiaohaiGit](https://github.com/xiaohaiGit)

## [0.3.4] - 2025-02-22

### Added

- Support dynamic load/unload.

### Fixed

- Fix generate test case is not working since Idea 2022.3.
- Fix duplicate quick fixes on struct when tags containing problems.

### Changed

- Use *IntelliJ Platform Gradle Plugin (2.x)* new building plugin.
- Fix some deprecated usages of APIs.

## [0.3.3] - 2021-12-22

### Fix

- Fix concurrent modification during conversion to proto.
- Fix include may be empty when renaming filename in include.

## [0.3.2] - 2021-11-07

### Fixed

- Fix unable to resolve identifiers within same module from other files.

### Added

- Auto conversion from jce/tars to protobuf file.

## [0.3.1] - 2020-05-05

### Added

- Keyword highlighting, code folding, breadcrumb navigation
- Static analysis and automatic repair
- Click to jump and view usage
- Code formatting and code style setting
- Smart prompt module and custom type
- Automatic prompt for include files
- Smart Renaming
- Automatically generate test cases
- Jump between java and jce/tars
