package athallah.neardeal.api.responses

import com.google.gson.annotations.SerializedName

class DealResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("store_id") val storeId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("description") val desc: String,
    @SerializedName("photo") val photo: String,
    @SerializedName("product_id") val productId: Long,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("discount") val discount: Int,
    )