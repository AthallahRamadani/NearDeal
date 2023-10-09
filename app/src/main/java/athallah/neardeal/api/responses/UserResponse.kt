package athallah.neardeal.api.responses

import com.google.gson.annotations.SerializedName

class LoginResponse(
    @SerializedName("username") val username: String,
    @SerializedName("token") val token: String
)