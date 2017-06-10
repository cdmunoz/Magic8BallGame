package co.cdmunoz.magic8ballgame

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_magic8_ball_game.ball
import kotlinx.android.synthetic.main.activity_magic8_ball_game.message
import java.util.Random

class Magic8BallGameActivity : AppCompatActivity(), SensorEventListener {

  companion object {
    val FADE_DURATION = 1500L
    val START_OFFSET = 1000L
    val VIBRATE_TIME = 250L
    val THRESHOLD = 240
    val SHAKE_COUNT = 2
    val RANDOM = Random()
    lateinit var vibrator: Vibrator
    lateinit var sensorManager: SensorManager
    lateinit var sensor: Sensor
    var lastX: Float = 0F
    var lastY: Float = 0F
    var lastZ: Float = 0F
    var shakeCount = 0
    lateinit var ballAnimation: Animation
    lateinit var answers: ArrayList<String>
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_magic8_ball_game)

    vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    ballAnimation = AnimationUtils.loadAnimation(this, R.anim.shake)
    answers = loadAnswers()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.shake -> {
        showAnswer(getAnswer(), true)
        return true
      }
      else -> {
        return false
      }
    }
  }

  override fun onResume() {
    super.onResume()
    sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
    //showAnswer(getString(R.string.shake_me), false)
  }

  override fun onPause() {
    super.onPause()
    sensorManager.unregisterListener(this)
  }

  override fun onSensorChanged(event: SensorEvent) {
    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
      if (/*isShakeEnough(event.values[0], event.values[1], event.values[2])*/true) {
        //showAnswer(getAnswer(), false)
      }
    }
  }

  private fun isShakeEnough(x: Float, y: Float, z: Float): Boolean {
    var force = 0.0
    force += Math.pow(((x - lastX) - SensorManager.GRAVITY_EARTH).toDouble(), 2.0)
    force += Math.pow(((y - lastY) - SensorManager.GRAVITY_EARTH).toDouble(), 2.0)
    force += Math.pow(((z - lastZ) - SensorManager.GRAVITY_EARTH).toDouble(), 2.0)

    force = Math.sqrt(force)

    lastX = x
    lastY = y
    lastZ = z

    if (force > (THRESHOLD / 100F)) {
      ball.startAnimation(ballAnimation)
      shakeCount++

      if (shakeCount > SHAKE_COUNT) {
        shakeCount = 0
        lastX = 0F
        lastY = 0F
        lastZ = 0F
        return true
      }
    }
    return false
  }

  private fun showAnswer(answer: String, withAnim: Boolean) {
    if (withAnim) {
      ball.startAnimation(ballAnimation)
    }

    message.visibility = View.INVISIBLE
    message.text = answer
    val alphaAnimation = AlphaAnimation(0F, 1F)
    alphaAnimation.startOffset = START_OFFSET
    message.visibility = View.VISIBLE
    alphaAnimation.duration = FADE_DURATION
    message.startAnimation(alphaAnimation)
    vibrator.vibrate(VIBRATE_TIME)

  }

  private fun getAnswer(): String {
    val randomAnswer = RANDOM.nextInt(answers.size)
    return answers[randomAnswer]
  }

  private fun loadAnswers(): ArrayList<String> {
    val theAnswers = resources.getStringArray(R.array.answers)
    val list = arrayListOf<String>()
    if (null != theAnswers && theAnswers.isNotEmpty()) {
      list += theAnswers
    }
    return list

  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  }

}
