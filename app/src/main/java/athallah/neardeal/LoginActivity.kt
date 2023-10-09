package athallah.neardeal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import athallah.neardeal.api.EndpointFactory
import athallah.neardeal.sharedPrefs.LoginSharedPref
import athallah.neardeal.utils.dismissLoading
import athallah.neardeal.utils.executeApi
import athallah.neardeal.utils.showAlert
import athallah.neardeal.utils.showLoading
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.login_btn).setOnClickListener { login() }

        if (LoginSharedPref(this).isLoggedIn){
            startMainActivity()
        }
    }

    private fun login() {
        val username =
            findViewById<TextInputLayout>(R.id.username_til).editText?.text?.toString() ?: ""
        val password =
            findViewById<TextInputLayout>(R.id.password_til).editText?.text?.toString() ?: ""

        showLoading("Logging in")
        val endpoint = EndpointFactory.userEndPoint
        endpoint.login(
            username = username,
            password = password
        ).executeApi(
            this,
            onSuccess = {
                dismissLoading()
                startMainActivity()
                LoginSharedPref(this).save(it)
            },
            onFailed = {
                dismissLoading()
                showAlert(message = "it.message ?: unknown error occured")
            }

        )

    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
}