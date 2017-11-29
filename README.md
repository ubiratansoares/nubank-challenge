# Nubank Challenge
 
> This is my take for an Android Software Engineer position at Nubank. Check all details below

## Why

I just want work (finally) in a company where technology 
**really matters** and people **really cares** about Software Engineering and 
the product. 

I believe that Nubank matches ... So here I am. 🚀

## How

This is a small Android application that uses the provided 
sandbox REST API to solve the chargeback user flow. 

This solution relies on several modern approaches to build Android applications

- 100% written in Kotlin
- Rock-solid reactive architecture, powered with MVVM + RxJava2 with unique approach
- Massive test coverage, using both local JVM (JUnit and Roboletric) 
and Instrumentation (Espresso) test frameworks
- ETC

## Setup

This application uses
- Android Gradle Plugin 3+
- Kotlin 1.2+

In order to import and runs this project on Android Studio, please check your 
IDE plugins to meet these requirements (avoiding some nasty bugs).

## Building and running tests

All the build variants can be built by the straightfoward

```
./gradlew build
```

This Gradle tasks will build all the four variants for this project as well 
run the JVM local tests for all of them.

If you want to run instrumentation tests, you may want to leverage one of the `mock` 
variants of this build, since these variants rely on REST API calls stubbed with
Mockito and properly configured accorded the desired scenario per test.

```
./gradlew connectedMockDebugAndroidTest
```

This approach provides hermetic testing conditions for the acceptance tests 
implemented with Instrumentation/Espresso.
 
**NOTE** - You must have an online AVD or Android device to run this task

## Installing

Any one of the four outputs APKs may be used for manual checking of this solution.

However, note that `app-mock-*.apk` ones will **EVER** behave the same way, ignoring 
netwoking and assuming all the REST API call as successfull : therefore, you are 
not able to verify any error treatments using these artifacts.

In order to check all the full features delivered with this exercise, you must
use one of `app-live-*.apk` APK artifacts, grabing one APK from 
**app/build/outputs/apk** after the proper assemble Gradle tasks like

```
./gradlew assembleLive
```

or installing the APK directly on device, by instance

```
./gradlew installLiveDebug 
```

## Questions?

I`m glad to awser, just ping me via email/hangouts 😄

