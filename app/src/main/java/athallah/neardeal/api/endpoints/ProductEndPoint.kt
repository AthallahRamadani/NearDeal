package athallah.neardeal.api.endpoints

import athallah.neardeal.api.request.CheckoutRequest
import athallah.neardeal.api.responses.*
import retrofit2.Call
import retrofit2.http.*

interface ProductEndPoint {

    @POST("get_product.php")
    @FormUrlEncoded
    fun getProductByStore(
        @Header("Authorization") tokenBearer: String,
        @Field("store_id") storeId: Long
    ): Call<ApiResponse<List<ProductResponse>>>

    @GET("get_deal.php")
    fun getDealbyStore(
        @Header("Authorization") tokenBearer: String,
        @Query("store_id") storeId: Long
    ): Call<ApiResponse<List<DealResponse>>>

    @POST("get_product_detail.php")
    @FormUrlEncoded
    fun getProductDetail(
        @Header("Authorization") tokenBearer: String,
        @Field("product_id") productId: Long
    ): Call<ApiResponse<ProductDetailResponse>>

    @POST("checkout.php")
    fun checkout(
        @Header("Authorization") tokenBearer: String,
        @Body checkoutRequest: CheckoutRequest
    ): Call<ApiResponse<String>>
}