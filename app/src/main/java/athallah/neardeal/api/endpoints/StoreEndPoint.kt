package athallah.neardeal.api.endpoints

import athallah.neardeal.api.responses.ApiResponse
import athallah.neardeal.api.responses.StoreResponse
import retrofit2.Call
import retrofit2.http.*

interface StoreEndPoint {
    @GET("get_store.php")
    fun getAllStores(
        @Header("Authorization") tokenBearer: String
    ): Call<ApiResponse<List<StoreResponse>>>

    @POST("get_store_nearby.php")
    @FormUrlEncoded
    fun getAllStoresNearMe(
        @Header("Authorization") tokenBearer: String,
        @Field("lat") lat: Double,
        @Field("lng") lng: Double
    ): Call<ApiResponse<List<StoreResponse>>>
}