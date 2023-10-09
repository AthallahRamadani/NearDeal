package athallah.neardeal.api.request

import com.google.gson.annotations.SerializedName

data class CheckoutRequest(
    @SerializedName("name") val name: String,
    @SerializedName("no_hp")val noHp: String,
    @SerializedName("address") val address: String,
    @SerializedName("products") val products: List<ProductRequest>
)

data class ProductRequest(
    @SerializedName("id")val id: Long,
    @SerializedName("price") val price: Long
)
