package com.example.myfaith.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mynavigationapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                enableMyLocation()
            } else {
                Snackbar.make(
                    requireView(),
                    "Location permission is required to show your location on the map.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)?.visibility = View.GONE

        // Initialize Places API with the correct API key retrieval
        Places.initialize(requireContext(), getString(R.string.google_maps_api_key))
        placesClient = Places.createClient(requireContext())
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val almaty = LatLng(43.2565, 76.9284)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(almaty, 12f))

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            enableMyLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(
                requireView(),
                "Location permission is needed to show your location on the map.",
                Snackbar.LENGTH_LONG
            )
                .setAction("Grant") {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }.show()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val userLatLng = LatLng(it.latitude, it.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14f))
                    searchForPrayerPlaces(listOf("Мешіт", "намаз", "мечеть"), userLatLng)
                }
            }
        }
    }

    private fun searchForPrayerPlaces(queries: List<String>, location: LatLng) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(
                requireView(),
                "Location permission is required to search for prayer places.",
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        googleMap.clear()

        queries.forEach { query ->
            // Increase the radius by creating a larger rectangular bounds
            val radius = 500.0 // 5000 meters (5 kilometers)
            val northEast = LatLng(location.latitude + radius / 111000.0, location.longitude + radius / (111000.0 * Math.cos(location.latitude * Math.PI / 180.0)))
            val southWest = LatLng(location.latitude - radius / 111000.0, location.longitude - radius / (111000.0 * Math.cos(location.latitude * Math.PI / 180.0)))
            val bounds = RectangularBounds.newInstance(southWest, northEast)

            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setLocationBias(bounds)
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    for (prediction in response.autocompletePredictions) {
                        placesClient.fetchPlace(com.google.android.libraries.places.api.net.FetchPlaceRequest.newInstance(prediction.placeId, listOf(Place.Field.NAME, Place.Field.LAT_LNG)))
                            .addOnSuccessListener { placeResponse ->
                                val place = placeResponse.place
                                val latLng = place.latLng
                                if (latLng != null) {
                                    googleMap.addMarker(
                                        MarkerOptions()
                                            .position(latLng)
                                            .title(place.name)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    )
                                }
                            }.addOnFailureListener {
                                Snackbar.make(requireView(), "Failed to fetch place details", Snackbar.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        Snackbar.make(
                            requireView(),
                            "Failed to load prayer places: ${exception.statusCode}",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}