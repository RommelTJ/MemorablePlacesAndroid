package com.rommelrico.memorableplacesandroid

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    internal var locationManager: LocationManager? = null
    internal var locationListener: LocationListener? = null
    private lateinit var mMap: GoogleMap

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
                val lastKnownLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                centerMapOnLocation(lastKnownLocation, "Your Location")
            }
        }
    } // end onRequestPermissionsResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapLongClickListener(this)

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onMapLongClick(latLng: LatLng) {
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        var address = ""

        try {
            val listAdddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (listAdddresses != null && listAdddresses.size > 0) {
                if (listAdddresses[0].thoroughfare != null) {
                    if (listAdddresses[0].subThoroughfare != null) {
                        address += listAdddresses[0].subThoroughfare + " "
                    }
                    address += listAdddresses[0].thoroughfare
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (address == "") {
            val sdf = SimpleDateFormat("HH:mm yyyy-MM-dd")
            address += sdf.format(Date())
        }

        mMap.addMarker(MarkerOptions().position(latLng).title(address))

//        MainActivity.places.add(address)
//        MainActivity.locations.add(latLng)
//        MainActivity.arrayAdapter.notifyDataSetChanged()

        Toast.makeText(this, "Location Saved!", Toast.LENGTH_SHORT).show()
    }

    fun centerMapOnLocation(location: Location?, title: String) {
        if (location != null) {
            val userLocation = LatLng(location.latitude, location.longitude)
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(userLocation).title(title))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12f))
        }
    } // end centerMapOnLocation

} // end MapsActivity
