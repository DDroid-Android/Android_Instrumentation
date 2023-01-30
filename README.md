# DDroid-instrumentor
<font face='Times New Roman' size=4>

## Introduction
DDroid-instrumentor is an automated instrumentation tool for Android apps. It is built on [ASM](https://asm.ow2.io/) and [Gradle Transformer](https://developer.android.com/reference/tools/gradle-api/7.0/com/android/build/api/transform/Transform) to automatically instrument apps at the event handlers to uniquely log executed UI events. Specifically, ASM is an all purpose Java bytecode manipulation and analysis framework, which can modify existing classes or dynamically generate classes in the binary form. 

In our work, DDroid-instrumentor is used to instrument the apps from [Themis](https://github.com/the-themis-benchmarks), a representative benchmark with diverse types of real-world bugs for Android. 

Fig. 1 shows DDroid-instrumentor's workflow.

![Fig 1](https://github.com/DDroid-Android/Android_Instrumentation/blob/main/Fig.png)

### Step (1): Instrumentation 
Given an app, we automatically instrument event handler  methods to obtain an instrumented app. Specifically, we get the ``.class`` files through Gradle Transformer and our custom Gradle plugin, and use ASM to traverse all the ``.class`` files. If the event handler method is traversed, our custom function will scan the parameter list of the current event handler method, and insert specific API-call statements into the current event handler method according to the type of the UI component bounded to the event handler to get __``UI-Infos``__ of the event handler method. For example, ``onClick (View v)`` is a typical event handler method, and the UI component type in its parameter list is __android.view.View__. Then we instrument at the beginning of the ``onClick`` method body to log the id, className and location of the View component, as well as the global qualified name of its corresponding event handler method.

### Step (2): Retrigger bug and set up the ground truth of event-signature

In this step, we manually replay the bug-retriggering-trace on the instrumented app obtained in step 1, and the customized functions will log the UI-Infos of the executed events to a ``.txt`` file. Then we extract the UI-Info of each pivot event, and use UI-Info as __*event-signature ground truth*__ to uniquely identify a pivot event.

### Step (3): Random Fuzzing 
In this step, we automatically run the GUI testing tool on
the instrumented buggy app obtained in step 1 to get the Raw
 Logs of UI-Infos.


### Step (4): Identify the executed UI events based on ground truth
In this step, we analyze the Raw Logs obtained in step 3, and identify which pivot events were executed in step 3 based on the event-signature ground truth obtained in step 2.




## Guide of using DDroid-instrumentor

Specifically, DDroid-instrumentor requires the following steps to enable the app instrumentation.

### step 0. Preparation
You need to obtain the app source code and the Gradle version and AGP version of the app.

### step 1. Import Plugin
You can import the module **_asm-method-plugin_** into your app project, or you can create a new module in your project according to the above directory of module **_asm-method-plugin_**.

### step 2. Insert Code
First, modify Gradle version in **_build.gradle_** in module _asm-method-plugin_ to the same version as your project, e.g.:
```gradle
dependencies {
	implementation gradleApi()
	implementation localGroovy()
	// modify Gradle version
	implementation 'com.android.tools.build:gradle:3.5.0'
}
```

Then, do the following to generate the plugin.
```
./gradlew asm-method-plugin:uploadArchives
```
### step 3. Modify ``build.gradle``
First, you need to import this plugin to the project-level build.gradle like the following snippet.
``` gradle
buildscript {
	repositories {
		...
		google()
		jcenter()
		maven {
			url uri('./asm-method-plugin/my-plugin')
		}
	}
	dependencies {
		...
		classpath 'com.asm.plugin:asm-method-plugin:0.0.1'
    }
	...
}
```
Second, you need to apply this plugin to the app-level build.gradle like the following snippet.
```gradle
apply plugin: 'com.asm.gradle'
```

Then, you can run the commands below to generate the instrumented app (located in the directory ```project_name/module_name/build/outputs/apk/```).

```
./gradlew tasks
./gradlew assembleDebug
```



</font>


