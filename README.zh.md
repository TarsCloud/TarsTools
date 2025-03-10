# Jce/Tars Support

[In English](README.md)

该项目为实现编辑Jce/Tars文件的JetBrains插件，支持Intellij IDEA、Android Studio、PhpStorm、WebStorm、GoLand、CLion等Intellij IDE。

-------------------------------

## 功能介绍

1. 关键字高亮, 代码折叠，面包屑导航
2. 静态分析与自动修复
3. 点击跳转与查看usage
4. 代码格式化与代码风格设置
5. 智能提示module与自定义类型
6. 自动提示include文件
7. 智能重命名
8. 自动生成测试用例
9. java与jce/tars之间相互跳转
10. 可以转换为proto文件

具体开发体验，可查看[功能截图](./doc/features.md)

-------------------------------

## 安装说明

### 网络安装

本插件已经在JetBrains Plugin Repository发布，通过IDE的Plugin入口，查找**Jce Support**，可进行在线安装或更新。

建议尽量使用该方式进行安装。

### 本地安装

下载本项目的dist文件夹下的[releases](https://github.com/TarsCloud/TarsTools/releases), 然后再通过Intellij Idea的设置菜单安装本地插件。

注意，通过本方法安装的插件，将不会自动更新

-------------------------------

## Todo

- [x] 支持tars
- [ ] 编译jce

-------------------------------

## 维护说明

1. 本项目利用Intellij IDEA开发，请参考Intellij
   IDEA的[Custom Language Support](https://plugins.jetbrains.com/docs/intellij/custom-language-support.html)，可使用Intellij IDEA Community
   Edition来维护本项目的插件
2. src/main/java下的代码为插件所有逻辑, src/main/resources下的代码为插件的资源文件
3. 右键如下文件选择*Run JFlex Generator*，选择路径时可以选择项目根目录
    1. `src/main/java/com/tencent/jceplugin/language/_JceLexer.flex`
    2. `src/main/java/com/tencent/jceplugin/language/jcedoc/_JceDocLexer.flex`
4. 右键如下文件选择*Generate Parser Code*
    1. `src/main/java/com/tencent/jceplugin/language/jce-grammar.bnf`
    2. `src/main/java/com/tencent/jceplugin/language/jcedoc/jcedoc.bnf`
5. 然后就可以运行`gradle runIde`本地调试了

-------------------------------

## 致谢

在开发过程中，有如下人员付出了卓越贡献：

* Harvey Xu
* Kong Yuanyuan

还有不少朋友和同事，提出了宝贵意见，欢迎继续支持。
