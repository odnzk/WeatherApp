package com.example.weatherapp.util.managers

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat

class PermissionManager(
    private val context: Context,
    private val locationPermissionRequest: ActivityResultLauncher<Array<String>>
) {

    fun arePermissionsAllowed(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                return false
            }
        }
        return true
    }

    fun askForPermission(vararg permissions: String) {
        locationPermissionRequest.launch(permissions as Array<String>?)
    }
}
