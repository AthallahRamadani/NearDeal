package athallah.neardeal.api.responses

import com.google.gson.annotations.SerializedName

class ProductResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("store_id") val storeId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("description") val desc: String,
    @SerializedName("photo") val photo: String,
)

class ProductDetailResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("store_id") val storeId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("description") val description: String,
    @SerializedName("photo") val photo: String,
    @SerializedName("store_name") val storeName: String,
    @SerializedName("store_photo") val storePhoto: String,
    @SerializedName("store_telp") val storePhoneNumber: String,
    @SerializedName("product_id") val productId: Long,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("discount") val discount: Int
)