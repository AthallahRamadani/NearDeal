package athallah.neardeal.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import athallah.neardeal.R
import athallah.neardeal.api.responses.DealResponse
import athallah.neardeal.api.responses.ProductResponse
import athallah.neardeal.utils.discountStatusOf
import athallah.neardeal.utils.formatToIndoCurrency
import athallah.neardeal.utils.parse
import athallah.neardeal.utils.priceWithDiscount
import dika.neardeal.util.loadUrl

class ItemDealAdapter(
    var deals: List<DealResponse>
) : RecyclerView.Adapter<ItemDealAdapter.ViewHolder>() {

    //ngasih tau gimana cara nampilin datanya
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deal, parent, false)
        return ViewHolder(view)
    }

    // modifikasi data didalam viewnya
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deal = deals[position]
        holder.bind(deal)
    }

    // total list berapa banyak
    override fun getItemCount(): Int {
        return deals.size
    }

    // tanggung jawab ke
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(deal: DealResponse) {
            itemView.run {
                findViewById<TextView>(R.id.tv_name).text = deal.name
                findViewById<TextView>(R.id.tv_price_old).run {
                    text = deal.price.formatToIndoCurrency()
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                findViewById<ImageView>(R.id.imageView).loadUrl(deal.photo)
                findViewById<TextView>(R.id.tv_price).text =
                    priceWithDiscount(deal.price, deal.discount).formatToIndoCurrency()
                findViewById<TextView>(R.id.tv_date).run {
                    val discountStatus = discountStatusOf(
                        start = deal.startDate.parse(),
                        end = deal.endDate.parse()
                    )
                    if (discountStatus != null) {
                        if (discountStatus.isDiscountNotStarted) {
                            text = "discount will start on ${deal.startDate}"
                        } else if (discountStatus.isDiscountStartToday) {
                            text = "discount start today"
                        } else if (discountStatus.isDiscountEndToday) {
                            text = "discount end today"
                        } else if (discountStatus.isDiscountAlreadyEnded) {
                            val color = ContextCompat.getColor(context, R.color.red)
                            setBackgroundColor(color)
                            text = "discount already ended"

                        }
                        else {
                            text = "discount will end in ${discountStatus.daysEndDiscount} days later"
                        }
                    }
                }
            }
        }
    }
}