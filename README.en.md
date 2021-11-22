# Jce/Tars Support
[点我查看中文版](README.md)
 
This program is a JetBrains plugin tool that implements the editing for Jce/Tars files. The tool supports Intellij IDE such as Intellij IDEA, Android Studio, PhpStorm, Webstorm, Goland, and CLion.

-------------------------------
 
## Features
 
1. Keyword highlighting, code folding, breadcrumb navigation
2. Static analysis and automatic repair
3. Click to jump and view usage
4. Code formatting and code style setting
5. Smart prompt module and custom type
6. Automatic prompt for include files
7. Smart Renaming
8. Automatically generate test cases
9. Jump between java and jce
10. Convert to protobuf file
 
Please check these [screenshots](./doc/features.md) to see the features in action. 
 
-------------------------------
 
## Installation
 
### Network installation
 
This plug-in is in JetBrains Plugin Repository. You can find **Jce Support** through the Plugin entrance of the IDE, and you can install or update it online.
 
This method for installation is most recommended.  
 
### Local installation
 
Download the [jce-support-0.3.3.zip](dist/jce-support-0.3.3.zip) under the dist folder of this project, and then install the local plug-in through the Intellij Idea setup menu.
 
Note that the plug-in installed via this method will not be automatically updated
 
 
-------------------------------
 
## Todo
 
- [x] supports tars
- [ ] compiles jce
 
-------------------------------
 
## Maintenance Instructions
 
1. This project is developed using Intellij IDEA. Please refer to Intellij IDEA's [Plugin Development](http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started.html), and Intellij IDEA Community Edition can maintain the plugin for this project
2. The code under src/main/java is the logic of the plugin, and the code under src/main/resources is the resource file of the plugin
3. Right click the files below and choose *Run JFlex Generator*. If asked to choose a directory, the project root can be a choice.
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
 
