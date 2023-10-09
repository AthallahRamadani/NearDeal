package athallah.neardeal.utils

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

fun isRequestPermissionResultGranted(
    resultRequestCode: Int,
    requiredRequestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
): Boolean {
    if (resultRequestCode == requiredRequestCode) {
        var isAllGranted = true
        for (i in permissions.indices) {
            val result = grantResults[i]
            if (result != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false
            }
        }
        return isAllGranted
    }
    return false
}

fun Fragment.createActivityResultLauncher(
    onPermissionsGranted: () -> Unit,
    onPermissionDenied: () -> Unit
): ActivityResultLauncher<Array<String>> {
    return registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { result ->
        var allAreGranted = true
        result.values.forEach {granted ->
            if (!granted) allAreGranted  = false
        }
        if(!allAreGranted) {
            onPermissionDenied()
        }
        onPermissionsGranted()
    }
}