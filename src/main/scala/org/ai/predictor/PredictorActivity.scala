package org.ai.predictor

import scala.collection.JavaConversions._
import org.scaloid.common._
import android.graphics.Color
import android.content.Context
import android.hardware.{Sensor, SensorEvent, SensorEventListener, SensorManager}

class PredictorActivity extends SActivity with SensorEventListener {

  lazy val prediction = new STextView("Init")

  onCreate {
    contentView = new SLinearLayout {
      new SVerticalLayout {
        prediction.textSize(30.dip).here
      }.wrap.here
    } padding 20.dip
  }

  onResume {
    registerSensorListeners()
  }

  onPause {
    unregisterSensorListeners()
  }

  lazy val sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER) ++
                     sensorManager.getSensorList(Sensor.TYPE_ORIENTATION) ++
                     sensorManager.getSensorList(Sensor.TYPE_PROXIMITY) ++
                     sensorManager.getSensorList(Sensor.TYPE_LIGHT)

  def registerSensorListeners(): Unit =
    sensors.foreach(sensorManager.registerListener(this, _: Sensor, SensorManager.SENSOR_DELAY_NORMAL))

  def unregisterSensorListeners(): Unit = sensorManager.unregisterListener(this)

  lazy val predictor = new Predictor(getResources().openRawResource(R.raw.trained))

  def onSensorChanged(event: SensorEvent): Unit = {
    val atts = predictor.Attributes
    event.sensor.getType() match {
      case Sensor.TYPE_ACCELEROMETER =>
        (atts.acceleration, event.values).zipped.foreach(predictor.set(_, _))
      case Sensor.TYPE_ORIENTATION =>
        (atts.orientation, event.values).zipped.foreach(predictor.set(_, _))
      case Sensor.TYPE_PROXIMITY =>
        predictor.set(atts.distance, if (event.values(0) == 0)  "Near" else "Far")
      case Sensor.TYPE_LIGHT =>
        predictor.set(atts.light, event.values(0))
      case _ => toast("Unexpected SensorChanged event.")
    }
    prediction.text(predictor.classify())
  }

  def onAccuracyChanged(s: Sensor, accuracy: Int): Unit = {}

}

