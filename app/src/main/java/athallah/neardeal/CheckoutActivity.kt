package athallah.neardeal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.widget.addTextChangedListener
import athallah.neardeal.api.EndpointFactory
import athallah.neardeal.api.request.CheckoutRequest
import athallah.neardeal.api.request.ProductRequest
import athallah.neardeal.databinding.ActivityCheckoutBinding
import athallah.neardeal.realm.RealmFactory
import athallah.neardeal.realm.entities.Cart
import athallah.neardeal.sharedPrefs.LoginSharedPref
import athallah.neardeal.utils.*
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.reflect.TypeToken
import dika.neardeal.util.clearError
import dika.neardeal.util.input
import dika.neardeal.util.setErrorInput

class CheckoutActivity : AppCompatActivity() {
    private var carts: List<Cart> = emptyList()
    private lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)
        binding.checkoutBtn.setOnClickListener { checkout() }
        binding.nameEd.addTextChangedListener {
            validateName()
        }
        binding.phoneEd.addTextChangedListener {
            validatePhone()
        }
        binding.addressEd.addTextChangedListener {
            validateAddress()
        }
        getReceivedCarts()


    }

    private fun getReceivedCarts() {
        val cartInJson = intent.getStringExtra(DATA_CART)
        if (cartInJson != null && cartInJson.isNotEmpty()) {
            val type = object : TypeToken<List<Cart>>() {}
            carts = cartInJson.fromJson(type)
            return
        }
        showAlert(message = "No data cart has been received")
    }

    private fun convertCartToProductRequest(): List<ProductRequest> {
        return carts.map { cart ->
            ProductRequest(
                id = cart.productId,
                price = cart.price.toLong()
            )
        }
    }

    private fun checkout() {
        if (!validateName() && !validatePhone() && !validateAddress()) {
            showAlert(message = "inputan tidak valid")
            return
        }
        showLoading("Checkout your carts")
        val request = CheckoutRequest(
            name = binding.nameEd.text.toString(),
            noHp = binding.phoneEd.text.toString(),
            address = binding.addressEd.text.toString(),
            products = convertCartToProductRequest()
        )
        val endpoint = EndpointFactory.productEndPoint
        endpoint.checkout(
            tokenBearer = LoginSharedPref(this).tokenBearer,
            checkoutRequest = request
        ).executeApi(
            this,
            onSuccess = {
                dismissLoading()
                showShortToast(message = it)
                clearCarts()
            },
            onFailed = {
                dismissLoading()
                showAlert(message = it.message ?: "unknown error occurred")
            }
        )
    }


    private fun backToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun clearCarts() {
        try {
            val datasource = RealmFactory.cartDatasource
            datasource.deleteAll()
            backToHome()
        } catch (e: Exception) {
            showAlert(message = e.message ?: "unknown error occurred")
        }
    }

    private fun validateName(): Boolean {
        val name = binding.nameTil.input
        if (name.isEmpty()) {
            binding.nameTil.setErrorInput("Nama depan jangan kosong")
            return false
        }
        binding.nameTil.clearError()
        return true
    }

    private fun validatePhone(): Boolean {
        val name = binding.phoneTil.input
        if (name.isEmpty()) {
            binding.phoneTil.setErrorInput("No HP jangan kosong")
            return false
        }
        binding.phoneTil.clearError()
        return true
    }

    private fun validateAddress(): Boolean {
        val name = binding.addressTil.input
        if (name.isEmpty()) {
            binding.addressTil.setErrorInput("Alamat jangan kosong")
            return false
        }
        binding.addressTil.clearError()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    companion object {
        const val DATA_CART = "Data cart"
    }
}