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
import athallah.neardeal.utils.formatToIndoCurrency
import dika.neardeal.util.loadUrl

class ItemProductAdapter(
    var products: List<ProductResponse>
) : RecyclerView.Adapter<ItemProductAdapter.ViewHolder>() {
    var onSelectedProductListener: OnSelectedProductListener? = null

    //ngasih tau gimana cara nampilin datanya
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    // modifikasi data didalam viewnya
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = products[position]
        holder.bind(store)

        val listener = onSelectedProductListener
        if (listener!= null){
            holder.containerCv.setOnClickListener{
                listener.onSelectedProduct(store)
            }
        }
    }

    // total list berapa banyak
    override fun getItemCount(): Int {
        return products.size
    }

    interface OnSelectedProductListener {
        fun onSelectedProduct(
            product: ProductResponse
        )
    }

    // tanggung jawab ke
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val containerCv: CardView = itemView.findViewById(R.id.container)

        fun bind(product: ProductResponse) {
            itemView.run {
                findViewById<TextView>(R.id.product_name_tv).text = product.name
                findViewById<TextView>(R.id.product_price_tv).text = product.price.formatToIndoCurrency()
                findViewById<ImageView>(R.id.product_img).loadUrl(product.photo)

            }
        }
    }
}