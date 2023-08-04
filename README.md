# Project Details

## Project Name: Human Verification Demo
## Language: Kotlin

### Description
Human Verification Demo is a media-pipe face detection demo.

#### Folder structure : MVVM


```bash
.gitignore
README.md
app
   |-- src
   |   |-- main
   |   |   |-- assets
   |   |   |-- java
   |   |   |   |-- com
   |   |   |   |   |-- humanverificationdemo
   |   |   |   |   |   |-- activities
   |   |   |   |   |   |-- utils
   |   |   |   |   |   |-- viewmodels
   |   |   |-- jniLibs
   |   |   |   |-- arm64-v8a
   |   |   |   |   |-- libmediapipe_jni.so
   |   |   |   |   |-- libopencv_java3.so
   |   |   |-- res
   |   |   |   |-- drawable-hdpi
   |   |   |   |-- drawable-ldpi
   |   |   |   |-- drawable-mdpi
   |   |   |   |-- drawable-v24
   |   |   |   |-- drawable-xhdpi
   |   |   |   |-- drawable-xxhdpi
   |   |   |   |-- drawable-xxxhdpi
   |   |   |   |-- drawable
   |   |   |   |-- font
   |   |   |   |-- layout
   |   |   |   |-- mipmap-anydpi-v26
   |   |   |   |-- mipmap-hdpi
   |   |   |   |-- mipmap-mdpi
   |   |   |   |-- mipmap-xhdpi
   |   |   |   |-- mipmap-xxhdpi
   |   |   |   |-- mipmap-xxxhdpi
   |   |   |   |-- values-night
   |   |   |   |   |-- themes.xml
   |   |   |   |-- values
   |   |   |   |   |-- attr.xml
   |   |   |   |   |-- colors.xml
   |   |   |   |   |-- strings.xml
   |   |   |   |   |-- styles.xml
   |   |   |   |   |-- themes.xml


```
***
## Folders Details

The folder structure of this app is explained below:

| Folder Name                                            | Description                                              |
|--------------------------------------------------------|----------------------------------------------------------|
| app/src/main/java/com/humanverificationdemo/activity   | This folder contains all Activity files                  |
| app/src/main/java/com/humanverificationdemo/utils      | This folder contain all constant and common files        |
| app/src/main/java/com/humanverificationdemo/viewmodels | This folder contains all viewmodel files                 |
| app/src/res/drawable-hdpi                              | App icons and Images and Drawable files(hdpi)            |
| app/src/res/drawable-ldpi                              | App icons and Images and Drawable files                  |
| app/src/res/drawable-mdpi                              | App icons and Images and Drawable files                  |
| app/src/res/drawable-xhdpi                             | App icons and Images and Drawable files                  |
| app/src/res/drawable-xxhdpi                            | App icons and Images and Drawable files                  |
| app/src/res/drawable-xxxhdpi                           | App icons and Images and Drawable files                  |
| app/src/res/drawable-v24                               | App icons and Images and Drawable files                  |
| app/src/res/drawable                                   | App icons and Images and Drawable files                  |
| app/src/res/layout                                     | Layout Files for Round shape watch                       |
| app/src/res/mipmap-anydpi-v26                          | Launcher App icon                                        |
| app/src/res/mipmap-hdpi                                | Launcher App icon                                        |
| app/src/res/mipmap-mdpi                                | Launcher App icon                                        |
| app/src/res/mipmap-xhdpi                               | Launcher App icon                                        | 
| app/src/res/mipmap-xxhdpi                              | Launcher App icon                                        |
| app/src/res/mipmap-xxxhdpi                             | Launcher App icon                                        |  
| app/src/res/values/attr.xml                            | represents an attribute of an Element object             |
| app/src/res/values/colors.xml                          | All Colors code Added in this file which are used in App |
| app/src/res/values/dimens.xml                          | Add Dimen in this file                                   |
| app/src/res/values/string.xml                          | Add All string in this file which are used in App        |
| app/src/res/values/styles.xml                          | Add All style in this file                               |
| app/src/res/values/themes.xml                          | Add All theme in this file                               |
| app/src/res/values-night/themes.xml                    | Add All dark mode theme in this file                     |
 


# Versions name with their code ::
Android Studio version : Android Studio Giraffe | 2022.3.1


## Dependencies (Packages and Library)

| name                                             | version        | Details                                                                                                                                                                 |
|--------------------------------------------------|----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| org.jetbrains.kotlin:kotlin-stdlib-jdk7          | 1.8.0          | The Kotlin Standard Library for JDK 7, providing essential utility functions and extensions for Kotlin programming                                                      |
| androidx.appcompat:appcompat                     | 1.6.1          | library that provides backward-compatible implementations of newer Android features and UI components                                                                   |
| androidx.constraintlayout:constraintlayout       | 2.1.4          | library that helps to create flexible and responsive user interfaces in Android by using a constraint-based layout                                                      |
| androidx.core:core-ktx                           | 1.10.1         | Kotlin extensions library for Android that simplifies and enhances the use of core Android APIs.                                                                        |  
| com.google.android.material:material             | 1.4.0          | library provided by Google that implements Material Design components                                                                                                   |
| com.google.mediapipe:solution-core               | latest.release | google media pipe dependency.                                                                                                                                           |
| com.google.mediapipe:facemesh                    | latest.release | mgoogle media pipe dependency.                                                                                                                                          | 
| com.google.flogger:flogger                       | 0.6            | logging library provided by Google                                                                                                                                      |
| com.google.flogger:flogger-system-backend        | 0.6            | ogging library provided by Google                                                                                                                                       |
| androidx.camera:camera-core                      | 1.0.0-beta10   | provides the core functionalities and abstractions for working with the camera                                                                                          |
| androidx.camera:camera-camera2                   | 1.0.0-beta10   | low-level Android camera package that replaces the deprecated Camera class                                                                                              |
| androidx.camera:camera-lifecycle                 | 1.0.0-beta10   | module integrates CameraX with the Android Lifecycle library,                                                                                                           |
| com.google.auto.value:auto-value-annotations     | 1.8.1          | Google library for generating immutable value classes with annotations.                                                                                                 |
| org.jetbrains.kotlinx:kotlinx-coroutines-core    | 1.6.1          | Kotlin coroutines library for asynchronous programming in Kotlin.                                                                                                       |
| org.jetbrains.kotlinx:kotlinx-coroutines-android | 1.6.4          | Kotlin coroutines library for Android, providing support for asynchronous programming                                                                                   |
| com.intuit.ssp:ssp-android                       | 1.1.0          | This size unit scales with the screen size based on the sp size unit                                                                                                    |
| com.intuit.sdp:sdp-android                       | 1.1.0          | This size unit scales with the screen size. It can help Android developers with supporting multiple screens.                                                            |
| com.google.firebase:firebase-firestore           | 22.0.1         | library provided by Google as part of the Firebase platform                                                                                                             |
| com.google.firebase:firebase-storage             | 20.0.0         | Firebase Storage is a cloud storage service that allows developers to securely upload, download, and manage user-generated content like images, videos, and other files |
| com.google.android.gms:play-services-auth        | 20.6.0         | This library provides authentication and authorization functionalities for Android applications using Google services.                                                  |
| androidx.lifecycle:lifecycle-viewmodel-ktx       | 2.6.1          | Kotlin extensions library provided by AndroidX for the Android Lifecycle ViewModel component.                                                                           |
| androidx.lifecycle:lifecycle-livedata-ktx        | 2.6.1          | Kotlin extensions library for working with LiveData in Android Architecture Components.                                                                                 |




# SDK Version supports:
*Min SDK version required: 23*
*TargetSdk SDK version required: 33*


