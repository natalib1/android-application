Readme:

# NataliShopApp
#Final project in the course [Advanced Java Programming Workshop - 20503] (http://www.openu.ac.il/courses/20503.html) at The Open University Of Israel.
# The code is regular java.
# Project description
# Project structure

Software Requirements
---------------------
In order to build/run our this application you need to install the following
free software distributions :
1) Java 8
2) Android Studio 2.3+ (http://developer.android.com/sdk/index.html)
3) Android SDK 17+    (http://developer.android.com/sdk/index.html)

Project Structure
-----------------
The project structure of an Android application follows the directory layout
prescribed by the Android system/
In particular:
# The "AndroidManifest.xml" file contains essential information the Android
  system needs to run the application's code.
# The "ant.properties" file defines customizable Ant properties for the
  Android build system; in our case we need to define at least the following
  properties (adapt the respective values to your own environment).

# The "default.properties" file defines the default API level of an Android.
# The "build.xml" Ant build script defines targets such as "clean", "install"
  and "uninstall" and has been slightly modified to handle also Scala source
  files. Concretely.
# The "build-scala.xml" Ant build script defines the targets "compile-scala" and "-post-compile-scala" where respectively the "<scalac>" Ant task generates
Java bytecode from the Scala source files and the "<proguard>" task creates a shrinked version of the Scala standard library by removing the unreferenced
code. Those two tasks are featured by the Scala and ProGuard software distributions respectively.

-Natali-
