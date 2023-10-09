package athallah.neardeal.sharedPrefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.edit
import athallah.neardeal.api.responses.LoginResponse

class LoginSharedPref(
    private val context: Context
) {
    private val keyToken = "token"
    private val sharedPref: SharedPreferences get() = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    val tokenBearer: String get(){
        val token = sharedPref.getString(keyToken, null)
        if (token != null && token.isNotEmpty()) return "Bearer $token"
        throw Exception("no session data has been saved")
    }
    val isLoggedIn: Boolean get() {
        val token = sharedPref.getString(keyToken, null)
        return token != null && token.isNotEmpty()
    }

    fun save(response: LoginResponse) {
        sharedPref.edit {
            putString(keyToken, response.token)
            commit()
        }
    }

    fun clear() {
        sharedPref.edit {
            putString(keyToken, null)
            commit()
        }
    }
}