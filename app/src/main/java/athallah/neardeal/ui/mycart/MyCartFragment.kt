package athallah.neardeal.ui.mycart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import athallah.neardeal.CheckoutActivity
import athallah.neardeal.adapters.ItemCartAdapter
import athallah.neardeal.databinding.FragmentMyCartBinding
import athallah.neardeal.realm.RealmFactory
import athallah.neardeal.realm.entities.Cart
import athallah.neardeal.utils.showAlert
import athallah.neardeal.utils.toJson

class MyCartFragment : Fragment() {

    private lateinit var binding: FragmentMyCartBinding
    private lateinit var itemCartAdapter: ItemCartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyCartBinding.inflate(inflater, container, false)
        binding.myCarRv.run {
            itemCartAdapter = ItemCartAdapter(emptyList())
            adapter = itemCartAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.checkoutBtn.setOnClickListener{ startCheckoutActivity()}
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCarts()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showCarts(carts: List<Cart>) {
        itemCartAdapter.carts = carts
        itemCartAdapter.notifyDataSetChanged()
    }

    private fun loadCarts() {
        try {
            val dataSource = RealmFactory.cartDatasource
            val founds = dataSource.getAll()
            showCarts(founds)
        }   catch (e: Exception){
            showCarts(emptyList())
            requireActivity().showAlert(
                message = e.message?:"unknown error occurred"
            )
        }
    }

    private fun startCheckoutActivity() {
        if (itemCartAdapter.carts.isEmpty()){
            requireActivity().showAlert(message = "no data cart has been saved")
            return
        }
        val intent = Intent(requireActivity(),CheckoutActivity::class.java).apply {
            val cartsInJson = itemCartAdapter.carts.toJson()
            putExtra(CheckoutActivity.DATA_CART, cartsInJson)
        }
        startActivity(intent)
    }


}