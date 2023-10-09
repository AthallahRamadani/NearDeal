package athallah.neardeal.realm

import athallah.neardeal.realm.datasource.CartDatasource
import athallah.neardeal.realm.datasource.CartDatasourceImpl

object RealmFactory {
    val cartDatasource: CartDatasource get() = CartDatasourceImpl()
}