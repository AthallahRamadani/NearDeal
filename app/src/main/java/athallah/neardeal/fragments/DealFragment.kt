package athallah.neardeal.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import athallah.neardeal.ProductActivity
import athallah.neardeal.R
import athallah.neardeal.adapters.ItemDealAdapter
import athallah.neardeal.api.EndpointFactory
import athallah.neardeal.api.responses.DealResponse
import athallah.neardeal.sharedPrefs.LoginSharedPref
import athallah.neardeal.utils.dismissLoading
import athallah.neardeal.utils.executeApi
import athallah.neardeal.utils.showAlert
import athallah.neardeal.utils.showLoading

class DealFragment : Fragment() {
    private val storeId get() = requireArguments().getLong(ProductActivity.KEY_STORE_ID)
    private lateinit var itemDealAdapter: ItemDealAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_deal, container, false)
        itemDealAdapter = ItemDealAdapter(emptyList())
        view.findViewById<RecyclerView>(R.id.deal_rv).run{
            adapter = itemDealAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDeals()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showData(deals: List<DealResponse>) {
        itemDealAdapter.deals = deals
        itemDealAdapter.notifyDataSetChanged()
    }

    private fun loadDeals(){
        val endpoint = EndpointFactory.productEndPoint
        val token = LoginSharedPref(requireContext()).tokenBearer
        requireActivity().showLoading("Load data products")
        endpoint.getDealbyStore(
            storeId = storeId,
            tokenBearer = token
        ).executeApi(
            requireContext(),
            onSuccess = {
                dismissLoading()
                showData(it)
            },
            onFailed = {
                showData(emptyList())
                dismissLoading()
                requireContext().showAlert(message = it.message ?: "Unknown error occurred")
            }
        )
    }

}