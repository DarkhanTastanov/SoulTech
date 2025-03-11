package com.example.myfaith.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mynavigationapp.R
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt

class CompassFragment : Fragment(), SensorEventListener {

    private lateinit var compassImage: ImageView
    private lateinit var degreeTextView: TextView
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private var currentDegree = 0f
    private var lastAzimuth = 0f // Store the last azimuth value
    private var gravity: FloatArray = FloatArray(3)
    private var magneticField: FloatArray = FloatArray(3)
    private var rotationMatrix: FloatArray = FloatArray(9)
    private var orientation: FloatArray = FloatArray(3)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCompass() // Start compass functionality after permission is granted
            } else {
                Snackbar.make(
                    requireView(),
                    "Location permission is required for the compass.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_compass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compassImage = view.findViewById(R.id.compass_image)
        degreeTextView = view.findViewById(R.id.degree_text_view)

        // Check and request permission if needed
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCompass() // Start immediately if permission is already granted
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(
                requireView(),
                "Location permission is needed to use the compass feature.",
                Snackbar.LENGTH_LONG
            )
                .setAction("Grant") {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }.show()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    private fun startCompass() {
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        // Only register listeners if sensors are available
        accelerometer?.also { sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
        magnetometer?.also { sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }

    }

    override fun onResume() {
        super.onResume()
        // These are now handled in startCompass(), called after permission is granted
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this) // Unregister in onPause to avoid leaks
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {  // Use let to safely handle the nullable event
            when (it.sensor.type) { // Access members of 'it' (non-null event)
                Sensor.TYPE_ACCELEROMETER -> {
                    System.arraycopy(it.values, 0, gravity, 0, 3)
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    System.arraycopy(it.values, 0, magneticField, 0, 3)

                    SensorManager.getRotationMatrix(rotationMatrix, null, gravity, magneticField)
                    SensorManager.getOrientation(rotationMatrix, orientation)

                    // Correctly calculate and normalize azimuth
                    var azimuth = (Math.toDegrees(orientation[0].toDouble()) * -1).toFloat()
                    azimuth = (azimuth + + -90 + 360) % 360 // Normalize to 0-360 range

                    currentDegree = azimuth
                    compassImage.rotation = currentDegree

                    val roundedDegree = currentDegree.roundToInt()
                    degreeTextView.text = "$roundedDegree°"
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if needed
    }
}