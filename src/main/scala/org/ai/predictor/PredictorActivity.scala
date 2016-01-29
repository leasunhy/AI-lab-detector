package org.ai.predictor

import android.widget.Toast
import org.scaloid.common.{SActivity, STextView, toast, SVerticalLayout, Int2unitConversion}
import android.graphics.Color
import android.content.Context
import android.view.Gravity
import android.hardware.{Sensor, SensorEvent, SensorEventListener, SensorManager}

class PredictorActivity extends SActivity with SensorEventListener {
  lazy val predictor = new Predictor(getResources().openRawResource(R.raw.trained))

  lazy val prediction = new STextView("200")

  onCreate {
    contentView = new SVerticalLayout {
      prediction.textSize(20.dip).<<(WRAP_CONTENT, WRAP_CONTENT).>>.here
    } padding 20.dip
  }

  onResume {
    registerSensorListeners()
  }

  def registerSensorListeners(): Unit = {
    import scala.collection.JavaConversions._
    val manager = getSystemService(Context.SENSOR_SERVICE).asInstanceOf[SensorManager]
    val f = manager.registerListener(this, _: Sensor, SensorManager.SENSOR_DELAY_NORMAL)
    manager.getSensorList(Sensor.TYPE_ACCELEROMETER).foreach(f)
    manager.getSensorList(Sensor.TYPE_ORIENTATION).foreach(f)
    manager.getSensorList(Sensor.TYPE_PROXIMITY).foreach(f)
    manager.getSensorList(Sensor.TYPE_LIGHT).foreach(f)
  }

  def onSensorChanged(event: SensorEvent): Unit = {
    val atts = predictor.Attributes
    event.sensor.getType() match {
      case Sensor.TYPE_ACCELEROMETER =>
        predictor.set(atts.gx, event.values(0))
        predictor.set(atts.gy, event.values(1))
        predictor.set(atts.gz, event.values(2))
      case Sensor.TYPE_ORIENTATION =>
        predictor.set(atts.o1, event.values(0))
        predictor.set(atts.o2, event.values(1))
        predictor.set(atts.o3, event.values(2))
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
