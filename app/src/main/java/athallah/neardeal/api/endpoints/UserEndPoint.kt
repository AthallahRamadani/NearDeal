package athallah.neardeal.api.endpoints

import athallah.neardeal.api.responses.ApiResponse
import athallah.neardeal.api.responses.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface UserEndPoint {
    @POST("login.php")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ApiResponse<LoginResponse>>

    @GET("logout.php")
    fun logout(
        @Header("Authorization") tokenBearer: String
    ): Call<ApiResponse<String>>
}