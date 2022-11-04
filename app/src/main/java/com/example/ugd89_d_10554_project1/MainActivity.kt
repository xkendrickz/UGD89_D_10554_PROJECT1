package com.example.ugd89_d_10554_project1

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.*
import android.hardware.Camera.CameraInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null
    lateinit var sensorStatusTV: TextView
    lateinit var proximitySensor: Sensor
    lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorStatusTV = findViewById(R.id.idTVSensorStatus)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proximitySensor == null) {
            Toast.makeText(this, "No proximity sensor found in device..", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            sensorManager.registerListener(
                proximitySensorEventListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    private fun openFrontFacingCamera(): Camera? {
        var cameraCount = 0
        var cam: Camera? = null
        val cameraInfo = CameraInfo()
        cameraCount = Camera.getNumberOfCameras()
        for (camIdx in 0 until cameraCount) {
            Camera.getCameraInfo(camIdx, cameraInfo)
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx)
                } catch (e: RuntimeException) {
                    Log.e("Your_TAG", "Camera failed to open: " + e.localizedMessage)
                }
            }
        }
        return cam
    }

    private fun openBackFacingCamera(): Camera? {
        var cameraCount = 0
        var cam: Camera? = null
        val cameraInfo = CameraInfo()
        cameraCount = Camera.getNumberOfCameras()
        for (camIdx in 0 until cameraCount) {
            Camera.getCameraInfo(camIdx, cameraInfo)
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                try {
                    cam = Camera.open(camIdx)
                } catch (e: RuntimeException) {
                    Log.e("Your_TAG", "Camera failed to open: " + e.localizedMessage)
                }
            }
        }
        return cam
    }

    var proximitySensorEventListener: SensorEventListener? = object: SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0f) {
                    try{
                        mCamera = openFrontFacingCamera()
                    }catch (e: Exception){
                        Log.d("error", "Failed to get Camera" + e.message)
                    }
                    if (mCamera != null){
                        mCameraView = CameraView(this@MainActivity, mCamera!!)
                        val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
                        camera_view.addView(mCameraView)
                    }
                    @SuppressLint("MissingInFlatedId", "LocalSuppress") val imageClose =
                        findViewById<View>(R.id.imgClose) as ImageButton
                    imageClose.setOnClickListener { view: View? -> System.exit(0) }

                } else {
                    try{
                        mCamera = openBackFacingCamera()
                    }catch (e: Exception){
                        Log.d("error", "Failed to get Camera" + e.message)
                    }
                    if (mCamera != null){
                        mCameraView = CameraView(this@MainActivity, mCamera!!)
                        val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
                        camera_view.addView(mCameraView)
                    }
                    @SuppressLint("MissingInFlatedId", "LocalSuppress") val imageClose =
                        findViewById<View>(R.id.imgClose) as ImageButton
                    imageClose.setOnClickListener { view: View? -> System.exit(0) }
                }
            }
        }

    }
}