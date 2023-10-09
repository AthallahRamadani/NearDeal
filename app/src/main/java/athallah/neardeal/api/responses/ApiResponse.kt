package athallah.neardeal.api.responses

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success") val isSuccess: Boolean,
    @SerializedName("error") val error: String?,
    @SerializedName("data") var data: T?
)