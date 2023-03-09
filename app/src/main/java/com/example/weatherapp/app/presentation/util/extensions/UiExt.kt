package com.example.weatherapp.app.presentation.util.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding

fun View.showToast(@StringRes messageId: Int) {
    Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show()
}

fun ViewBinding.hideChildren(isChildVisible: Boolean = false) {
    (root as? ViewGroup)?.children?.forEach { it.isVisible = isChildVisible }
}

fun ViewBinding.showChildren() {
    hideChildren(isChildVisible = true)
}
