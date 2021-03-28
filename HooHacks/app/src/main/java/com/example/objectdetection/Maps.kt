package com.example.objectdetection

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.IOException
import java.util.*

class Maps : AppCompatActivity(), OnInfoWindowCloseListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    var isPermissionGranted = false
    var mGoogleMap: GoogleMap? = null
    //    var fab: FloatingActionButton? = null
    private var mLocationClient: FusedLocationProviderClient? = null
    private val GPS_REQUEST_CODE = 9001
    var locSearch: EditText? = null
    //    var searchIcon: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        var gotoLearn = this.findViewById<Button>(R.id.gotoLearn)
        var gotoTrans = this.findViewById<Button>(R.id.gotoTrans)
        gotoLearn.setOnClickListener {
            val intent = Intent(this, Learn::class.java)
            startActivity(intent)
        }
        gotoTrans.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        var fab = this.findViewById<FloatingActionButton>(R.id.fab)
        locSearch = findViewById(R.id.et_search)
        var searchIcon = this.findViewById<ImageView>(R.id.search_icon)
        checkMyPermission()
        initMap()
        mLocationClient = FusedLocationProviderClient(this)
//        fab.setOnClickListener(View.OnClickListener { currLoc })
        searchIcon.setOnClickListener(View.OnClickListener { view: View -> geoLocate(view) })
    }

    private fun geoLocate(view: View) {
        val locationName = locSearch!!.text.toString()
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addressList = geocoder.getFromLocationName(locationName, 3)
            if (addressList.size > 0) {
                val address = addressList[0]
                gotoLocation(address.latitude, address.longitude)
                mGoogleMap!!.addMarker(MarkerOptions().position(LatLng(address.latitude, address.longitude)))
                Toast.makeText(this, address.locality, Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
        }
    }

    private fun initMap() {
        if (isPermissionGranted) {
            if (isGPSenable) {
                val supportMapFragment = supportFragmentManager.findFragmentById(R.id.fragment) as SupportMapFragment?
                supportMapFragment!!.getMapAsync(this)
            }
        }
    }

    private val isGPSenable: Boolean
        private get() {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (providerEnable) {
                return true
            } else {
                val alertDialog = AlertDialog.Builder(this)
                        .setTitle("GPS Permission")
                        .setMessage("GPS is required for this app to work. Please enable GPS")
                        .setPositiveButton("Yes", (DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int ->
                            val intent: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivityForResult(intent, GPS_REQUEST_CODE)
                        }))
                        .setCancelable(false)
                        .show()
            }
            return false
        }

//    @get:SuppressLint("MissingPermission")
//    private val currLoc: Unit
//        private get() {
//            mLocationClient!!.lastLocation.addOnCompleteListener({ task: Task<Location> ->
//                if (task.isSuccessful()) {
//                    val location: Location = task.getResult()
//                    gotoLocation(location.getLatitude(), location.getLongitude())
//                }
//            })
//        }

    private fun gotoLocation(latitude: Double, longitude: Double) {
        val LatLng = LatLng(latitude, longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng, 18f)
        mGoogleMap!!.moveCamera(cameraUpdate)
        mGoogleMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    private fun checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(object : PermissionListener {
            override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                // Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                isPermissionGranted = true
            }

            override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", packageName, "")
                intent.data = uri
                startActivity(intent)
            }

            override fun onPermissionRationaleShouldBeShown(permissionRequest: PermissionRequest, permissionToken: PermissionToken) {
                permissionToken.continuePermissionRequest()
            }
        }).check()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap!!.isMyLocationEnabled = true
        Toast.makeText(this, "Marked places for Deaf Hiring Companies", Toast.LENGTH_LONG).show()

        //Marker 1
        val res = LatLng(9.666520, 123.869610)
        googleMap.addMarker(MarkerOptions()
                .position(res)
                .draggable(true)
                .title("Dao Diamond Hotel and Restaurant")
                .snippet("821 J.A. Clarin St, Dao District, Tagbilaran City, 6300 Bohol"))
        googleMap.setOnInfoWindowClickListener({ marker: Marker -> onInfoWindowClose(marker) })
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(res))

        //Marker 2
        val res1 = LatLng(14.584220, 120.984210)
        googleMap.addMarker(MarkerOptions()
                .position(res1)
                .draggable(true)
                .title("Garden Cafe")
                .snippet("Ermita, Manila, 1000 Metro Manila"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(res1))


        //Marker 3
        val res2 = LatLng(9.685480, 124.353320)
        googleMap.addMarker(MarkerOptions()
                .position(res2)
                .draggable(true)
                .title("Dine & Sign and IDEA Pension House")
                .snippet("Jagna, 6308 Bohol"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(res2))


        //Marker 4
        val res3 = LatLng(9.667670, 123.870201)
        googleMap.addMarker(MarkerOptions()
                .position(res3)
                .draggable(true)
                .title("Hammer and Nails Livelihood Center (HNLC) Inc.")
                .snippet("0821 J.A. Clarin St, Tagbilaran City, 6300 Bohol"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(res3))

        //Marker 5
        val res4 = LatLng(14.61776, 121.00793)
        googleMap.addMarker(MarkerOptions()
                .position(res4)
                .draggable(true)
                .title("Fruitas Group of Companies")
                .snippet("68 Data St., cor. Cordillera St. Brgy. Don Manuel Galas"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(res4))

        //Marker 6
        val res5 = LatLng(14.539770, 121.00793)
        googleMap.addMarker(MarkerOptions()
                .position(res5)
                .draggable(true)
                .title("De Original Jamaican Pattie Shop")
                .snippet("790, Antonio Arnaiz Avenue, Skyway, Makati, 1200 Metro Manila"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(res5))

        //Marker 7
        val res6 = LatLng(14.627480, 121.026980)
        googleMap.addMarker(MarkerOptions()
                .position(res6)
                .draggable(true)
                .title("FARRON CAFE Head Office")
                .snippet(" 49 Magnolia, Quezon City, 1103 Metro Manila"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(res6))

        //Marker 8
        val res7 = LatLng(14.627480, 121.026980)
        googleMap.addMarker(MarkerOptions()
                .position(res7)
                .draggable(true)
                .title("Elait!")
                .snippet("Century City Mall"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(res7))

        //Marker 9
        val res8 = LatLng(14.703300, 121.082710)
        googleMap.addMarker(MarkerOptions()
                .position(res8)
                .draggable(true)
                .title("Dunamai Cafe")
                .snippet("260 Don Fabian, Quezon City, 1122 Metro Manila"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(res8))
    }

    override fun onConnected(bundle: Bundle?) {}
    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GPS_REQUEST_CODE) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (providerEnable) {
                Toast.makeText(this, "GPS is enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "GPS is not enable", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onInfoWindowClose(marker: Marker) {
        Toast.makeText(this, "This accompanies people with hearing disabilities.",
                Toast.LENGTH_SHORT).show()
    }
}