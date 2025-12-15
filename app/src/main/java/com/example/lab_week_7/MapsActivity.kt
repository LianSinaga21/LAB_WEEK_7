package com.example.lab_week_7

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    getLastLocation()
                } else {
                    showPermissionRationale()
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Default location (anti blank)
        val jakarta = LatLng(-6.2, 106.816666)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 10f))

        if (hasLocationPermission()) {
            getLastLocation()
        } else {
            permissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Location Permission")
            .setMessage("This app needs location access to show your current position on the map.")
            .setPositiveButton("OK") { _, _ ->
                permissionLauncher.launch(ACCESS_FINE_LOCATION)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun getLastLocation() {
        // 🔐 Explicit permission check (lint-safe)
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(ACCESS_FINE_LOCATION)
            return
        }

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(userLatLng)
                                .title("You are here")
                        )
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(userLatLng, 12f)
                        )
                    }
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
