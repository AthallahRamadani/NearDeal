package dika.neardeal.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener

enum class AcquireLocationFailedCause {
    PERMISSION_NOT_GRANTED,
    ACQUIRING_TASK_NOT_SUCCESS
}

fun Context.getCurrentLocation(
    onLocationAcquired: (location: Location) -> Unit,
    onLocationAcquireFailed: ((cause: AcquireLocationFailedCause) -> Unit)? = null
) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        onLocationAcquireFailed?.invoke(AcquireLocationFailedCause.PERMISSION_NOT_GRANTED)
        return
    }
    val task = LocationServices.getFusedLocationProviderClient(this).getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken = CancellationTokenSource().token
            override fun isCancellationRequested(): Boolean = false
        }
    )
    task.addOnCompleteListener {
        if (it.isSuccessful) {
            onLocationAcquired(it.result)
            return@addOnCompleteListener
        }
        onLocationAcquireFailed?.invoke(AcquireLocationFailedCause.ACQUIRING_TASK_NOT_SUCCESS)
    }
}