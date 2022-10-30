package com.example.weatherapp.util.managers

//class LocationHelperManager(
//    private val activity: Activity,
//    private val locationPermissionRequest: ActivityResultLauncher<Array<String>>
//) : LocationListener {
//    private lateinit var locationManager: LocationManager
//    private var myLocation: MyLocation? = null
//
//    @SuppressLint("MissingPermission", "ServiceCast")
//    fun getLastKnownLocation(): Task<android.location.Location>? {
//        if (!PermissionManager(activity, locationPermissionRequest).arePermissionsAllowed(
//                android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            )
//        ) {
//            PermissionManager(activity, locationPermissionRequest).askForPermission(
//                android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            return null
//        } else {
//            return LocationServices.getFusedLocationProviderClient(activity).lastLocation.addOnSuccessListener { l ->
//                if (l == null) {
//                    locationManager =
//                        activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//                    val criteria = Criteria()
//                    val provider = locationManager.getBestProvider(criteria, true).toString()
//                    locationManager.requestLocationUpdates(provider, 1000, 0f, this)
//                } else {
//                    myLocation = MyLocation(l.longitude.toInt(), l.latitude.toInt())
//                }
//            }
//        }
//    }
//
//    override fun onLocationChanged(location: android.location.Location) {
//        locationManager.removeUpdates(this)
//        myLocation = MyLocation(location.longitude.toInt(), location.latitude.toInt())
//    }
//}
