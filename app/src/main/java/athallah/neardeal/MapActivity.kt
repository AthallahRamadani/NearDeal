package athallah.neardeal

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import athallah.neardeal.api.responses.StoreResponse
import athallah.neardeal.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.reflect.TypeToken
import dika.neardeal.util.AcquireLocationFailedCause
import dika.neardeal.util.getCurrentLocation
import dika.neardeal.util.loadUrl

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private var map: GoogleMap? = null
    private var stores: List<StoreResponse> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this)
        findViewById<FloatingActionButton>(R.id.back_btn).setOnClickListener{ finish() }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setOnMarkerClickListener(this)
        map = googleMap
        loadCurrentLocation()
        getReceivedStores()
    }

    private fun loadCurrentLocation() {
        showLoading("Getting your location")
        getCurrentLocation(
            onLocationAcquired = {
                dismissLoading()
                map?.zoomToLocation(it, 15f)
                map?.addMarker(it.latitude, it.longitude, "Current location")
            },
            onLocationAcquireFailed = {
                dismissLoading()
                when(it) {
                    AcquireLocationFailedCause.PERMISSION_NOT_GRANTED -> {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ),
                            REQ_LOCATION
                        )
                    }
                    AcquireLocationFailedCause.ACQUIRING_TASK_NOT_SUCCESS -> {
                        showAlert(message = "unable to acquired your location")
                    }
                }
            }
        )
    }

    private fun getReceivedStores() {
        val storeInJson = intent.getStringExtra(DATA_STORE_JSON)
        if(storeInJson != null && storeInJson.isNotEmpty()){
            val type = object : TypeToken<List<StoreResponse>>(){}
            stores = storeInJson.fromJson(type)
            stores.forEach{ store ->
                map?.addMarker(
                    lat = store.lat.toDouble(),
                    lng = store.lng.toDouble(),
                    title = store.name,
                    iconRes = R.drawable.store,
                    tag = store.id
                )
            }
        }
    }

    private fun showDetailStore(store : StoreResponse){
        val bottomSheetDialog = BottomSheetDialog(this, R.style.Theme_NearDeal_BottomSheetDialog).apply {
            setContentView(R.layout.dialog_marker_store)
            findViewById<ImageView>(R.id.store_img)?.loadUrl(store.photo)
            findViewById<TextView>(R.id.store_title)?.text = store.name
            findViewById<TextView>(R.id.store_phone_number)?.text = store.phoneNumber
            findViewById<TextView>(R.id.open_hour_tv)?.text = store.openHour
            findViewById<TextView>(R.id.desc_tv)?.text = store.description
            findViewById<TextView>(R.id.address_tv)?.text = store.address
            findViewById<TextView>(R.id.close_btn)?.setOnClickListener { dismiss() }
            findViewById<Button>(R.id.navigate_btn)?.setOnClickListener{ openGmaps(lat = store.lat.toDouble(),lng = store.lng.toDouble())}
        }
        bottomSheetDialog.show()
    }
    companion object {
        const val REQ_LOCATION = 1
        const val DATA_STORE_JSON = "STORE_JSON"
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag
        if (tag is Long) {
            val selectedStore = stores.findLast { store -> store.id == tag }
            if (selectedStore!= null) {
                showDetailStore(selectedStore)
                return true
            }
        }
        return false
    }
}