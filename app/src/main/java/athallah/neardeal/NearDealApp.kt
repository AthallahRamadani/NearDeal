package athallah.neardeal

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class NearDealApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("neardeal")
            .schemaVersion(1)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}