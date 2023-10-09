package athallah.neardeal.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

fun Number.formatToIndoCurrency(): String {
    val numberFormat = DecimalFormat("#,###")
    numberFormat.decimalFormatSymbols = DecimalFormatSymbols().apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    return "Rp ${numberFormat.format(this)}"
}

fun String.parse(): Date?{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return try {
        dateFormat.parse(this)
    } catch(e: Exception){
        null
    }
}