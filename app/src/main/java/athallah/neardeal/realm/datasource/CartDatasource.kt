package athallah.neardeal.realm.datasource

import athallah.neardeal.api.responses.ProductDetailResponse
import athallah.neardeal.realm.entities.Cart
import io.realm.Realm

interface CartDatasource {
    fun save(product: ProductDetailResponse)

    fun getAll(): List<Cart>

    fun delete(cart: Cart)

    fun deleteAll()
}

class CartDatasourceImpl: CartDatasource {

    override fun save(product: ProductDetailResponse) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val carts = it.where(Cart::class.java)
                .findAll()
            val lastId = carts.size
            val cart = Cart(
                id = lastId + 1,
                photo = product.photo,
                price = product.price,
                productId = product.id,
                productName = product.name
            )
            it.insert(cart)
        }
        realm.close()
    }

    override fun getAll(): List<Cart> {
        val realm = Realm.getDefaultInstance()
        val results = arrayListOf<Cart>()
        realm.executeTransaction {
            val savedCarts = it.where(Cart::class.java)
                .findAll()
                .map {cart -> it.copyFromRealm(cart) }
            results.addAll(savedCarts)
        }
        realm.close()
        return results
    }

    override fun delete(cart: Cart) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val results = it.where(Cart::class.java).equalTo("id", cart.id)
                .findAll()
            results.deleteAllFromRealm()
        }
        realm.close()
    }

    override fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val results = it.where(Cart::class.java)
                .findAll()
            results.deleteAllFromRealm()
        }
        realm.close()
    }
}