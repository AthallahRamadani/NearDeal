package athallah.neardeal.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openGmaps(
    lat: Double,
    lng: Double
) {
    val gmmIntentUri = Uri.parse("http://maps.google.com/maps?q=loc:$lat,$lng")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    startActivity(mapIntent)
}