package com.example.weatherapp.app.presentation.util.ext

import androidx.core.view.isVisible
import com.example.domain.exceptions.InvalidCityException
import com.example.domain.exceptions.LocationPermissionDeniedException
import com.example.weatherapp.R
import com.example.weatherapp.databinding.StateLoadingBinding
import retrofit2.HttpException
import java.io.IOException

fun StateLoadingBinding.loadingFinished() {
    progressBar.hide()
    tvError.isVisible = false
    btnTryAgain.isVisible = false
}

fun StateLoadingBinding.loadingStarted() {
    progressBar.show()
    tvError.isVisible = false
    btnTryAgain.isVisible = false
}

fun StateLoadingBinding.errorOccurred(error: Throwable, tryAgainAction: () -> Unit) {
    progressBar.hide()
    val errorId = when (error) {
        is IOException -> R.string.error_connection_lost
        is HttpException -> R.string.error_connection_lost
        is LocationPermissionDeniedException -> R.string.error_location_permission_denied
        is InvalidCityException -> R.string.error_invalid_city
        else -> R.string.error_message
    }
    val text = if (error is IOException || error is HttpException) {
        root.context.getString(errorId, error.message.toString())
    } else root.context.getString(errorId)
    tvError.text = text
    btnTryAgain.setOnClickListener { tryAgainAction() }
    tvError.isVisible = true
    btnTryAgain.isVisible = true
}
