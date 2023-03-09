package com.example.weatherapp.app.presentation.util.extensions

import androidx.core.view.isVisible
import com.example.domain.util.ConnectionLostException
import com.example.domain.util.InvalidCityException
import com.example.domain.util.LocationPermissionDeniedException
import com.example.domain.util.NetworkException
import com.example.weatherapp.R
import com.example.weatherapp.databinding.StateLoadingBinding

fun StateLoadingBinding.loadingFinished() {
    progressBar.hide()
    tvError.isVisible = false
    btnTryAgain.isVisible = false
}

fun StateLoadingBinding.loadingStarted() {
    progressBar.show()
    progressBar.show()
    tvError.isVisible = false
    btnTryAgain.isVisible = false
}

fun StateLoadingBinding.errorOccurred(error: Throwable, tryAgainAction: () -> Unit) {
    progressBar.hide()
    val errorId = when (error) {
        is ConnectionLostException -> R.string.error_connection_lost
        is NetworkException -> R.string.error_connection_lost
        is LocationPermissionDeniedException -> R.string.error_location_permission_denied
        is InvalidCityException -> R.string.error_invalid_city
        else -> R.string.error_message
    }
    val text = if (error is ConnectionLostException || error is NetworkException) {
        root.context.getString(errorId, error.message.toString())
    } else root.context.getString(errorId)
    tvError.text = text
    btnTryAgain.setOnClickListener { tryAgainAction() }
    tvError.isVisible = true
    btnTryAgain.isVisible = true
}
