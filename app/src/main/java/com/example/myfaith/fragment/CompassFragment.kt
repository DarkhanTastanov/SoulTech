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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mynavigationapp.R
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class CompassFragment : Fragment(), SensorEventListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var compassImage: ImageView
    private lateinit var degreeTextView: TextView
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private var currentDegree = 0f
    private var gravity: FloatArray = FloatArray(3)
    private var magneticField: FloatArray = FloatArray(3)
    private var rotationMatrix: FloatArray = FloatArray(9)
    private var orientation: FloatArray = FloatArray(3)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCompass()
                getLocation()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
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
        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)?.visibility = View.GONE

        compassImage = view.findViewById(R.id.compass_image)
        degreeTextView = view.findViewById(R.id.degree_text_view)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCompass()
            getLocation()
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

    private fun getLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val latitude = it.latitude
                val longitude = it.longitude

                Log.d("CompassFragment", "Latitude: $latitude, Longitude: $longitude")
                Toast.makeText(requireContext(), "Location acquired", Toast.LENGTH_SHORT).show()

                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val locationName = address.locality ?: address.subAdminArea ?: address.countryName
                    val text = "$locationName\nLat: ${"%.4f".format(latitude)}, Lon: ${"%.4f".format(longitude)}"
                    degreeTextView.text = text
                    Log.d("CompassFragment", "Location name: $locationName")
                } else {
                    degreeTextView.text = "Lat: ${"%.4f".format(latitude)}, Lon: ${"%.4f".format(longitude)}"
                    Log.d("CompassFragment", "No address found for location")
                }
            } ?: run {
                degreeTextView.text = "Location unavailable"
                Log.e("CompassFragment", "Location is null")
                Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Log.e("CompassFragment", "Error getting location: ${it.message}")
            Toast.makeText(requireContext(), "Error getting location", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCompass() {
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        accelerometer?.also { sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
        magnetometer?.also { sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }

        Log.d("CompassFragment", "Compass sensors registered")
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)?.visibility = View.GONE

        Log.d("CompassFragment", "Fragment resumed")
    }


    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
        Log.d("CompassFragment", "Sensors unregistered")
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    System.arraycopy(it.values, 0, gravity, 0, 3)
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    System.arraycopy(it.values, 0, magneticField, 0, 3)

                    SensorManager.getRotationMatrix(rotationMatrix, null, gravity, magneticField)
                    SensorManager.getOrientation(rotationMatrix, orientation)

                    var azimuth = (Math.toDegrees(orientation[0].toDouble()) * -1).toFloat()
                    azimuth = (azimuth + 270 + 360) % 360  // adjusted to point North properly

                    currentDegree = azimuth
                    compassImage.rotation = currentDegree

                    Log.d("CompassFragment", "Azimuth: ${currentDegree.roundToInt()}")
                }

                else -> {}
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("CompassFragment", "Sensor accuracy changed: $accuracy")
    }
}
