package athallah.neardeal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import athallah.neardeal.R
import athallah.neardeal.api.responses.ProductResponse
import athallah.neardeal.realm.entities.Cart
import athallah.neardeal.utils.formatToIndoCurrency
import dika.neardeal.util.loadUrl

class ItemCartAdapter(
    var carts: List<Cart>
) : RecyclerView.Adapter<ItemCartAdapter.ViewHolder>() {

    //ngasih tau gimana cara nampilin datanya
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    // modifikasi data didalam viewnya
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = carts[position]
        holder.bind(cart)
    }

    // total list berapa banyak
    override fun getItemCount(): Int {
        return carts.size
    }


    // tanggung jawab ke
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val containerCv: CardView = itemView.findViewById(R.id.container)

        fun bind(cart: Cart) {
            itemView.run {
                findViewById<TextView>(R.id.product_name_tv).text = cart.productName
                findViewById<TextView>(R.id.product_price_tv).text = cart.price.formatToIndoCurrency()
                findViewById<ImageView>(R.id.product_img).loadUrl(cart.photo)

            }
        }
    }
}