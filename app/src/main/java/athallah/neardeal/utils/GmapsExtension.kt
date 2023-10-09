package athallah.neardeal.utils

import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

fun GoogleMap.zoomToLocation(location: Location, zoom: Float) {
    val latlng = LatLng(location.latitude, location.longitude)
    val factory = CameraUpdateFactory.newLatLngZoom(latlng,zoom)
    moveCamera(factory)
}

fun GoogleMap.addMarker(lat: Double, lng: Double, title: String, iconRes: Int?= null, tag: Any? = null) {
    val markerOptions = MarkerOptions()
        .position(LatLng(lat,lng))
        .title(title)

    if (iconRes != null) {
        markerOptions.icon(BitmapDescriptorFactory.fromResource(iconRes))
    }
    val marker = addMarker(markerOptions)
    marker?.tag = tag
}