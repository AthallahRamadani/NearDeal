package athallah.neardeal.utils

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import athallah.neardeal.api.responses.ApiResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> String.fromJson(kClass: Class<T>): T {
    return Gson().fromJson(this, kClass)
}

fun <T> String.fromJson(typeToken: TypeToken<T>): T {
    return Gson().fromJson(this, typeToken.type)
}

fun Any.toJson(): String {
    return Gson().toJson(this)
}

fun <S, T: ApiResponse<S>> Call<T>.executeApi(
    context: Context,
    onSuccess: (data: S) -> Unit,
    onFailed: (error: Throwable) -> Unit
) {
    if (!context.isConnectedToInternet()) {
        onFailed(Exception("No internet connection"))
        return
    }

    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val data = response.body()?.data
            if (data != null) {
                onSuccess(data)
                return
            }
            val errorMessage = response.errorBody()?.string()?.fromJson(ApiResponse::class.java)?.error ?: "Unknown error"
            onFailed(NotFoundException(errorMessage))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onFailed(t)
            t.printStackTrace()
        }

    })
}

@Suppress("DEPRECATION")
fun Context.isConnectedToInternet(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = cm.activeNetwork ?: return false
        val actNw =
            cm.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        return when (cm.activeNetworkInfo?.type) {
            ConnectivityManager.TYPE_WIFI -> true
            ConnectivityManager.TYPE_MOBILE -> true
            ConnectivityManager.TYPE_ETHERNET -> true
            else -> false
        }
    }
}