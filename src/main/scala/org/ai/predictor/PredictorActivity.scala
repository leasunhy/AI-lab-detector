package org.ai.predictor

import scala.collection.JavaConversions._
import android.widget.TextView
import android.graphics.Color
import android.hardware.{Sensor, SensorEvent, SensorEventListener, SensorManager}
import android.support.v7.app.AppCompatActivity
import org.scaloid.common._

class PredictorActivity extends AppCompatActivity with SActivity with SensorEventListener {

  lazy val prediction = find[TextView](R.id.prediction)

  onCreate {
    setContentView(R.layout.main_layout)
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

  lazy val predictor = new Predictor(resources.openRawResource(R.raw.trained))

  val textColors = Vector(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
  def onSensorChanged(event: SensorEvent): Unit = {
    predictor.synchronized {
      val atts = predictor.Attributes
      event.sensor.getType match {
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
      val classIndex = predictor.classifyIndex()
      prediction.text(predictor.classEmoji(classIndex))
//      prediction.text(predictor.className(classIndex))
//      prediction.textColor(textColors(classIndex))
    }
  }

  def onAccuracyChanged(s: Sensor, accuracy: Int): Unit = {}

}

