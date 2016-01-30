name := "ai-lab-detector"

import android.Keys._
android.Plugin.androidBuild

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")
scalaVersion := "2.11.7"
scalacOptions in Compile += "-feature"

updateCheck in Android := {} // disable update check
proguardCache in Android ++= Seq("org.scaloid")

platformTarget in Android := "android-23"

proguardOptions in Android ++= Seq("-dontobfuscate", "-dontoptimize", "-keepattributes Signature", "-printseeds target/seeds.txt", "-printusage target/usage.txt"
  , "-dontwarn scala.collection.**" // required from Scala 2.11.4
  , "-dontwarn org.scaloid.**" // this can be omitted if current Android Build target is android-16
  , "-dontwarn java_cup.**"
  , "-dontwarn SymbolFactory"
  //, "-keep class android.support.v4.app.** { *;} "
  //, "-keep interface android.support.v4.app.** { *;} "
  //, "-keep class com.actionbarsherlock.** { *;} "
  , "-keep class org.scaloid.common.*Layout { *;} "
  , "-keep class org.scaloid.common.Trait* { *;} "
  , "-keep class org.scaloid.common.STextView { *;} "
  , "-keep class org.scaloid.common.SButton { *;} "
  , "-keep class weka.classifiers.bayes.NaiveBayes { *;} "
  , "-keep class org.ai.predictor.** { *;} "
)

libraryDependencies ++= Seq(
  "org.scaloid" %% "scaloid" % "4.1",
  aar("com.android.support" % "appcompat-v7" % "23.1.1")
)

run <<= run in Android
install <<= install in Android

// Tests //////////////////////////////

libraryDependencies ++= Seq(
  "org.apache.maven" % "maven-ant-tasks" % "2.1.3" % "test",
  "org.robolectric" % "robolectric" % "3.0" % "test",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test"
)

// without this, @Config throws an exception,
unmanagedClasspath in Test ++= (bootClasspath in Android).value
