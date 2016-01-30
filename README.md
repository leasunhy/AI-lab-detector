AI-Lab-Detector
===============

一个利用机器学习算法（Naive Bayes），通过对手机传感器数据的分类，检测用户正在进行的动作的Android应用。

用到的传感器数据
--------------
* 光传感器
* 近距离传感器
* 加速度传感器
* 方向传感器（虚拟，实际是由另外的传感器数据计算得出）


动作
----
* **喝咖啡**：在台灯下把手机当成咖啡杯拿到嘴边。
* **行走**：把手机拿在右手上走路。
* **唱歌**：把手机当成麦克风拿起。
* **关窗**：把手机拿在右手上，右手抬起在半空向左移作关窗状。


数据来源
-------
小组成员自己做动作收集而来。
收集所用的App见：[coolspring1293/AI-Lab-Final](https://github.com/coolspring1293/AI_LAB_FINAL)。


关于Weka库
---------
由于Weka有许多依赖在Android不能找到，需要一些努力（代码裁剪）才能让它可用于Android。

本项目所用的Weka库来自[Institute for Pervasive Computing, Johannes Kepler Universität Linz](http://www.pervasive.jku.at/Teaching/_2012WS/PervasiveComputingInfrastructure/Uebungen/UE04/weka-3.6.6-android.jar)。
详见[此文档](http://www.pervasive.jku.at/Teaching/_2012WS/PervasiveComputingInfrastructure/Uebungen/UE04/04%20Exercise%20Realtime%20Classification.pdf)。

Weka基于GPL协议。


构建
----
先保证sbt已安装，然后

```bash
$ sbt android:package
```

这会生成一个Apk文件，位于`<project root>/target/android/output/ai-lab-detector-debug.apk`。

由于sbt的Android插件有一些Bug，如果出错，可以尝试：

1. 再dex一次，实际上只需：`$ sbt android:package`。
2. 清除缓存：`$ rm -rf target`。然后重新开始构建。

详见[pocorall/hello-scaloid-sbt](https://github.com/pocorall/hello-scaloid-sbt)和[pfn/android-sdk-plugin](https://github.com/pfn/android-sdk-plugin)。

License
-------
GPL V3.0.

