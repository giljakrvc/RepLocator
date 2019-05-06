package edu.rvc.student.relocator

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,  GoogleMap.OnMarkerClickListener {

    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var mMap: GoogleMap
    private val ZOOM_LEVEL = 9f
    private val TILT_LEVEL = 0f
    private val BEARING_LEVEL = 0f
    private lateinit var repLocation : LatLng
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient



    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Get the support action bar
        val actionBar = supportActionBar

        // Set the action bar title, subtitle and elevation
        actionBar!!.title = "RepLocator"
        actionBar.subtitle = "RepLocator Main Menu"
        actionBar.elevation = 4.0F

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.getItemId()) {
            R.id.add_rep -> {
                val intent = Intent(this, repLocatorActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.list_rep -> {
                val intent = Intent(this, RepListActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.about-> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.sign_off -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        // 1
        mMap.isMyLocationEnabled = true

        // 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
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
        var ref = FirebaseDatabase.getInstance().getReference("RepLocator")
        val context = this

        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val children = dataSnapshot.children
                children.forEach {
                    var representative: Any? = it.value

                    var fullName : Any? = it.child("fullName").value
                    var email = it.child("email").value
                    var mobile = it.child("mobile").value
                    var address = it.child("address").value
                    var city = it.child("city").value
                    var state = it.child("state").value
                    var zipCode = it.child("zipCode").value

                    //print(" record: $fullName / $email / $address / $city / $state / $zipCode")

                    val coder = Geocoder(context)
                    try {
                        var salesRep =  coder.getFromLocationName("$address, $city, $state $zipCode", 1)
                        if(salesRep != null){
                            repLocation = LatLng(salesRep[0].latitude, salesRep[0].longitude)
                            mMap.addMarker(MarkerOptions().position(repLocation ).title("$fullName / $mobile  " ).snippet("$address, $city, $state $zipCode"))

                            //val camPos = CameraPosition(repLocation, ZOOM_LEVEL, TILT_LEVEL, BEARING_LEVEL)
                            //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camPos))
                        }

                    } catch (ex: Exception ) {

                        ex.printStackTrace();
                    }


                    //Log.d("TAG", "$representative / $representative.address ")
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        ref.addListenerForSingleValueEvent(eventListener)

        mMap.getUiSettings().setZoomControlsEnabled(true)
        mMap.setOnMarkerClickListener(this)

        setUpMap()

    }
}
