package athallah.neardeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import athallah.neardeal.api.EndpointFactory
import athallah.neardeal.api.responses.DealResponse
import athallah.neardeal.api.responses.ProductDetailResponse
import athallah.neardeal.realm.RealmFactory
import athallah.neardeal.realm.entities.Cart
import athallah.neardeal.sharedPrefs.LoginSharedPref
import athallah.neardeal.utils.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dika.neardeal.util.loadUrl

class ProductDetailActivity : AppCompatActivity() {
    private var detail : ProductDetailResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        findViewById<FloatingActionButton>(R.id.back_btn).setOnClickListener { finish() }
        findViewById<Button>(R.id.buy_btn).setOnClickListener{saveToChart()}
        loadDetailProduct()
    }

    private fun loadDetailProduct() {
        val productId = intent.getLongExtra(KEY_PROD_ID, -1)
        if (productId < 1) {
            showAlert(message = "No product id has been received")
            return
        }
        val endpoint = EndpointFactory.productEndPoint
        val token = LoginSharedPref(this).tokenBearer
        showLoading("Get detail product")
        endpoint.getProductDetail(
            productId = productId,
            tokenBearer = token
        ).executeApi(
            this,
            onSuccess = {
                dismissLoading()
                showData(it)
            },
            onFailed = {
                it.printStackTrace()
                dismissLoading()
                showData(null)
                showAlert(message = it.message ?: "Uknown error occurred")
            }
        )
    }

    private fun showData(detail: ProductDetailResponse?) {
        this.detail = detail
        findViewById<ImageView>(R.id.product_img).loadUrl(detail?.photo ?: "-")
        findViewById<ImageView>(R.id.store_img).loadUrl(detail?.photo ?: "")
        findViewById<TextView>(R.id.store_title).text = detail?.storeName ?: "-"
        findViewById<TextView>(R.id.store_phone_number).text = detail?.storePhoneNumber ?: "-"
        findViewById<TextView>(R.id.product_name_tv).text = detail?.name ?: "-"
        findViewById<TextView>(R.id.price_tv).text = detail?.price?.formatToIndoCurrency() ?: "-"
        findViewById<TextView>(R.id.discount_tv).run {
            val price = detail?.price
            val discount = detail?.discount
            text = if (price != null && discount != null) {
                discountInPrice(price, discount).formatToIndoCurrency()
            } else {
                "-"
            }
        }
        findViewById<TextView>(R.id.discount_period_tv).run {
            text = if (detail != null) {
                "${detail.startDate} s/d ${detail.endDate}"
            } else {
                "-"
            }
        }
        findViewById<TextView>(R.id.product_desc_tv).text = detail?.description ?: "-"
        findViewById<TextView>(R.id.to_pay_tv).run {
            text = if (detail != null) {
                priceWithDiscount(detail.price,detail.discount).formatToIndoCurrency()
            }else{
                "-"
            }
        }

    }

    private fun saveToChart(){
        val toSaveProduct = detail
        if (toSaveProduct == null) {
            showAlert(message = "no data product")
            return
        }
        try {
            val datasource = RealmFactory.cartDatasource
            datasource.save(toSaveProduct)
            showAlert(
                title = "success",
                message = "product has been added to your cart")
        } catch (e : Exception){
            showAlert(message = e.message ?:"unknown error occured")
        }

    }
    companion object {
        const val KEY_PROD_ID = "product_id"
    }
}