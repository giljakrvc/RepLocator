package edu.rvc.student.relocator

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val ZOOM_LEVEL = 15f
    private val TILT_LEVEL = 0f
    private val BEARING_LEVEL = 0f

    var salesRepList: List<Address> =  emptyList()


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

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        salesRepList =  Geocoder(this).getFromLocationName(
            "207 N Academy, Janesville, WI 53548", 1)
        val repLocation = LatLng(salesRepList[0].latitude, salesRepList[0].longitude)
        mMap.addMarker(MarkerOptions().position(repLocation ).title("Sales Representative: " + salesRepList[0].postalCode))

        var salesRepList2 = Geocoder(this).getFromLocationName("3301 N Mulford Rd, Rockford, IL 61114", 1)
        var repLocation2 = LatLng(salesRepList2[0].latitude, salesRepList2[0].longitude)
        mMap.addMarker(MarkerOptions().position(repLocation2 ).title("Sales Representative: " + salesRepList2[0].postalCode))

        val camPos = CameraPosition(repLocation2, ZOOM_LEVEL, TILT_LEVEL, BEARING_LEVEL)
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camPos))

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(repLocation))

    }
}
