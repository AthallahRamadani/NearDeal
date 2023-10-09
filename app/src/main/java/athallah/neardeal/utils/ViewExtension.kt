package dika.neardeal.util

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import athallah.neardeal.BuildConfig
import athallah.neardeal.R
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso

fun ImageView.loadUrl(url: String) {
    if (url.isEmpty()) {
        setImageResource(R.drawable.ic_baseline_broken_image_24)
        return
    }

    val picasso = Picasso.get()
    picasso.isLoggingEnabled = BuildConfig.DEBUG
    picasso.setIndicatorsEnabled(BuildConfig.DEBUG)
    picasso.load(url)
        .fit()
        .centerCrop()
        .placeholder(R.drawable.ic_baseline_broken_image_24)
        .into(this)
}

fun gridLayoutManager(context: Context): GridLayoutManager {
    return object : GridLayoutManager(context, 2) {
        override fun canScrollVertically(): Boolean {
            return false
        }
    }
}

fun TextInputLayout.setErrorInput(message: String) {
    error = message
    isErrorEnabled = true
}

fun TextInputLayout.clearError() {
    error = null
    isErrorEnabled = false
}

val TextInputLayout.input: String get() = editText?.text?.toString()?:""