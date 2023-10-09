package athallah.neardeal.api.responses

import com.google.gson.annotations.SerializedName



data class StoreResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String,
    @SerializedName("photo") val photo: String,
    @SerializedName("telp") val phoneNumber: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("open_hour") val openHour: String,
    @SerializedName("address") val address: String?,
    @SerializedName("created_at") val createdAt: String?
)