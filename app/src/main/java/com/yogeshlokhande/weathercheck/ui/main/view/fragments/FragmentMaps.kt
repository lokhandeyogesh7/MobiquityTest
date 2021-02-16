package com.yogeshlokhande.weathercheck.ui.main.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.yogeshlokhande.weathercheck.R
import com.yogeshlokhande.weathercheck.data.model.LatLong
import com.yogeshlokhande.weathercheck.data.repository.CityRepository
import com.yogeshlokhande.weathercheck.databinding.FragmentMapBinding
import com.yogeshlokhande.weathercheck.ui.base.BaseFragment
import com.yogeshlokhande.weathercheck.ui.main.viewmodel.CityViewModel
import java.util.*


class FragmentMaps : BaseFragment<CityViewModel, FragmentMapBinding, CityRepository>(),
    OnMapReadyCallback {

    private lateinit var selectedLatLong: LatLng
    private var mMap: GoogleMap? = null
    private val TAG = "MapsFragment"
    private var mPlacesClient: PlacesClient? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLastKnownLocation: Location? = null
    private val DEFAULT_ZOOM = 15
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var mLocationPermissionGranted = false


    @SuppressLint("SetTextI18n")
    override fun onMapReady(p0: GoogleMap?) {
        Log.d(TAG, "p0 $p0")
        mMap = p0
        mMap?.uiSettings?.isZoomControlsEnabled = false
        pickCurrentPlace()

        getLocationPermission()

        mMap?.setOnMapClickListener { latLng ->
            Log.d(TAG, "onMapClick: lat a and long " + latLng)
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng!!)

            selectedLatLong = latLng
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            binding.tvLocationName.text = "You have selected: ${addresses[0].getAddressLine(0)}"
            markerOptions.title(addresses[0].locality)

            mMap!!.clear()
            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap!!.addMarker(markerOptions)
            binding.rlLocationView.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.toolbar.title = "Locate Your City"

        var mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@FragmentMaps)

        // Initialize the Places client
        val apiKey = getString(R.string.google_maps_key)
        Places.initialize(requireContext().applicationContext, apiKey)
        mPlacesClient = Places.createClient(requireContext())
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireContext()
        )

        binding.rlButtonView.setOnClickListener {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(
                selectedLatLong!!.latitude,
                selectedLatLong!!.longitude,
                1
            )
            val locality = if (addresses[0].locality.isNullOrEmpty()) {
                if (addresses[0].getAddressLine(0).split(",")[0].isNullOrEmpty()) {
                    ""
                } else {
                    addresses[0].getAddressLine(0).split(",")[0]
                }
            } else {
                addresses[0].locality
            }
            viewModel.insert(
                LatLong(
                    selectedLatLong.latitude,
                    selectedLatLong.longitude,
                    addresses[0].getAddressLine(0),
                    locality
                )
            ).observe(viewLifecycleOwner, {
                if (it != 0L) {
                    Toast.makeText(
                        requireContext(),
                        "Location Bookmarked Successfully!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    /**
     * Populates the app bar with the menu.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Handles user clicks on menu items.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_geolocate -> {
                pickCurrentPlace()
                true
            }
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }


    /**
     * Fetch a list of likely places, and show the current place on the map - provided the user
     * has granted location permission.
     */
    private fun pickCurrentPlace() {
        if (mMap == null) {
            return
        }
        if (mLocationPermissionGranted) {
            getDeviceLocation()
        } else {
            Log.i(TAG, "The user did not grant location permission.")
            getLocationPermission()
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true
                }
            }
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private fun getLocationPermission() {
        mLocationPermissionGranted = false
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    /**
     * Get the current location of the device, and position the map's camera
     */
    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                val locationResult: Task<Location> = mFusedLocationProviderClient!!.lastLocation
                locationResult.addOnSuccessListener(
                    requireActivity()
                ) { location ->
                    if (location != null) {
                        mLastKnownLocation = location
                        Log.d(TAG, "Latitude: " + mLastKnownLocation!!.latitude)
                        Log.d(TAG, "Longitude: " + mLastKnownLocation!!.longitude)
                        mMap!!.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    mLastKnownLocation!!.latitude,
                                    mLastKnownLocation!!.longitude
                                ), DEFAULT_ZOOM.toFloat()
                            )
                        )
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val addresses = geocoder.getFromLocation(
                            mLastKnownLocation!!.latitude,
                            mLastKnownLocation!!.longitude,
                            1
                        )
                        mMap!!.addMarker(
                            MarkerOptions()
                                .title(addresses[0].locality)
                                .position(
                                    LatLng(
                                        mLastKnownLocation!!.latitude,
                                        mLastKnownLocation!!.longitude
                                    )
                                )
                                .snippet("Selected location")
                        )
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Exception: %s", e.message!!)
        }
    }

    override fun getViewModel() = CityViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMapBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = CityRepository(requireActivity().application)

}