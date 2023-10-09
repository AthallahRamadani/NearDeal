package athallah.neardeal.realm.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Cart(
    @PrimaryKey var id: Int = -1,
    var productId: Long = -1,
    var productName: String = "",
    var photo: String = "",
    var price: Int = -1
): RealmObject()

