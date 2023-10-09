package athallah.neardeal.api

import athallah.neardeal.api.endpoints.ProductEndPoint
import athallah.neardeal.api.endpoints.StoreEndPoint
import athallah.neardeal.api.endpoints.UserEndPoint

object EndpointFactory {
    val storeEndpoint: StoreEndPoint get() = createEndpoint(StoreEndPoint::class.java)
    val productEndPoint: ProductEndPoint get() = createEndpoint(ProductEndPoint::class.java)
    val userEndPoint: UserEndPoint get() = createEndpoint(UserEndPoint::class.java)

    private fun <T> createEndpoint(kClass: Class<T>) = ApiClient.getClient().create(kClass)
}