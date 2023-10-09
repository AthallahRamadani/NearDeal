package athallah.neardeal.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import athallah.neardeal.MapActivity
import athallah.neardeal.api.responses.StoreResponse
import athallah.neardeal.R
import dika.neardeal.util.loadUrl

class ItemStoreAdapter(
    var stores: List<StoreResponse>
) : RecyclerView.Adapter<ItemStoreAdapter.ViewHolder>() {
    var onSelectedStoreListener: OnSelectedStoreListener? = null

    //ngasih tau gimana cara nampilin datanya
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_store, parent, false)
        return ViewHolder(view)
    }

    // modifikasi data didalam viewnya
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = stores[position]
        holder.bind(store)

        val listener = onSelectedStoreListener
        if (listener!= null){
            holder.containerCv.setOnClickListener{
                listener.onSelectedStore(store)
            }
        }
    }

    // total list berapa banyak
    override fun getItemCount(): Int {
        return stores.size
    }

    interface OnSelectedStoreListener {
        fun onSelectedStore(
            store: StoreResponse
        )
    }



    // tanggung jawab ke
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val containerCv: CardView = itemView.findViewById(R.id.container)

        fun bind(store: StoreResponse) {
            itemView.run {
                findViewById<TextView>(R.id.store_title).text = store.name
                findViewById<TextView>(R.id.store_phone_number).text = store.phoneNumber
                findViewById<ImageView>(R.id.store_img).loadUrl(store.photo)

            }
        }
    }
}