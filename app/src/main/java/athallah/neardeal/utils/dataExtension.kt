package athallah.neardeal.utils

import org.joda.time.DateTime
import org.joda.time.Days
import java.util.*

fun discountInPrice(price: Int, discount: Int): Double {
    return price.toDouble() * discount / 100
}

fun priceWithDiscount(price: Int, discount: Int): Double {
    return price - discountInPrice(price, discount)
}

data class DiscountStatus(
    val isDiscountAlreadyEnded: Boolean,
    val isDiscountNotStarted: Boolean,
    val isDiscountStartToday: Boolean,
    val isDiscountEndToday: Boolean,
    val daysEndDiscount: Int?
)

fun discountStatusOf(start: Date?, end: Date?): DiscountStatus? {
    if (start != null && end != null) {
        val today = Calendar.getInstance().time
        return DiscountStatus(
            isDiscountAlreadyEnded = today.after(end),
            isDiscountNotStarted = start.after(today),
            isDiscountStartToday = start.month == today.month && start.year == today.year && start.date == today.date,
            isDiscountEndToday = end.month == today.month && end.year == today.year && end.date == today.date,
            daysEndDiscount = Days.daysBetween(DateTime(today), DateTime(end)).days
        )
    }
    return null
}