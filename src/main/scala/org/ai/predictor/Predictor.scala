package org.ai.predictor

import java.io.InputStream

import weka.core.{FastVector, Attribute, Instance, Instances}
import weka.classifiers.Classifier

/**
  * Created by leasunhy on 1/29/16.
  */

object FastVector {
  def apply[T](elems: T*): FastVector = {
    val result = new FastVector(elems.size)
    elems.foreach(result.addElement(_))
    result
  }
}

class Predictor(modelFile: InputStream) {
  object Attributes {
    val gx = new Attribute("gx")
    val gy = new Attribute("gy")
    val gz = new Attribute("gz")
    val o1 = new Attribute("o1")
    val o2 = new Attribute("o2")
    val o3 = new Attribute("o3")
    val distance = new Attribute("distance", FastVector("Near", "Far"))
    val light = new Attribute("light")
    val label = new Attribute("label", FastVector("唱歌", "喝咖啡", "走路", "关窗"))

    val acceleration = Vector(gx, gy, gz)
    val orientation = Vector(o1, o2, o3)
    val attributes = FastVector(gx, gy, gz, o1, o2, o3, distance, light, label)
  }
  val dataset = new Instances("AI", Attributes.attributes, Attributes.attributes.size())
  dataset.setClassIndex(Attributes.attributes.size() - 1)

  import weka.core.SerializationHelper
  val classifier = SerializationHelper.read(modelFile).asInstanceOf[Classifier]
  val inst = new Instance(Attributes.attributes.size())
  inst.setDataset(dataset)

  def set(att: Attribute, value: Double): Unit = inst.setValue(att, value)
  def set(att: Attribute, value: String): Unit = inst.setValue(att, value)

  def classifyIndex(): Int = classifier.classifyInstance(inst).toInt

  def className(index: Int): String = inst.classAttribute().value(index)
  val emojis = Vector("\uD83C\uDFA4", "\uD83C\uDF75", "\uD83D\uDEB6", "\uD83D\uDEAA")
  def classEmoji(index: Int): String = emojis(index)
}

