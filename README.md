# Jce/Tars Support

[点我查看中文版](README.zh.md)

This program is a JetBrains plugin tool that implements the editing for Jce/Tars files. The tool supports Intellij IDE such as Intellij IDEA, Android Studio, PhpStorm, Webstorm, Goland, and CLion.

-------------------------------

<!-- Plugin description -->
This plugin supports the editing of the Jce/Tars file, providing the following features:

- Keyword highlighting, code folding, breadcrumb navigation
- Static analysis and automatic repair
- Click to jump and view usage
- Code formatting and code style setting
- Smart prompt module and custom type
- Automatic prompt for include files
- Smart Renaming
- Automatically generate test cases
- Jump between java and jce/tars
- Convert to protobuf file

If any question, contact with harveyxu @ wechat work or [kongyuanyuan@yuewen.com](mailto:kongyuanyuan@yuewen.com)
<!-- Plugin description end -->

Please check these [screenshots](./doc/features.md) to see the features in action.

-------------------------------

## Installation

### Network installation

This plugin has been deployed to JetBrains Plugin Repository.
It is recommended to install and update through *Plugins* settings of your IDEs.
You may search **Jce Support** through the Plugin entrance of the IDE, and install or update it accordingly.

### Local installation

Download from the [releases](https://github.com/TarsCloud/TarsTools/releases) of this project,
and then install the local plugin through the Intellij Idea setup menu.

Note that the plugin installed via this method will not be automatically updated

 
-------------------------------

## Todo

- [x] supports tars
- [ ] compiles jce

-------------------------------

## Maintenance Instructions

1. This project is developed using Intellij IDEA. Please refer to Intellij IDEA's [Custom Language Support](https://plugins.jetbrains.com/docs/intellij/custom-language-support.html), and Intellij IDEA Community Edition can be used for this project.
2. The code under src/main/java is the logic of the plugin, and the code under src/main/resources is the resource file of the plugin
3. Right click the files below and choose *Run JFlex
   Generator*. If asked to choose a directory, the project root can be a choice.
    1. `src/main/java/com/tencent/jceplugin/language/_JceLexer.flex`
    2. `src/main/java/com/tencent/jceplugin/language/jcedoc/_JceDocLexer.flex`
4. Right click the files below and choose*Generate Parser Code*
    1. `src/main/java/com/tencent/jceplugin/language/jce-grammar.bnf`
    2. `src/main/java/com/tencent/jceplugin/language/jcedoc/jcedoc.bnf`
5. The project is ready to go. Run `gradle runIde` to debug or `gradle buildPlugin` to build.

-------------------------------

## Acknowledgement

We would like to thank the following people for their outstanding contributions during the project's development:

* Harvey Xu
* Kong Yuanyuan

There are also many friends and colleagues who have provided valuable suggestions, and we continue to welcome and value your support.
