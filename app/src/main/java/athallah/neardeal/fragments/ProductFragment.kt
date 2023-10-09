package athallah.neardeal.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import athallah.neardeal.ProductActivity
import athallah.neardeal.ProductDetailActivity
import athallah.neardeal.R
import athallah.neardeal.adapters.ItemProductAdapter
import athallah.neardeal.api.EndpointFactory
import athallah.neardeal.api.responses.ProductResponse
import athallah.neardeal.sharedPrefs.LoginSharedPref
import athallah.neardeal.utils.dismissLoading
import athallah.neardeal.utils.executeApi
import athallah.neardeal.utils.showAlert
import athallah.neardeal.utils.showLoading


class ProductFragment : Fragment(), ItemProductAdapter.OnSelectedProductListener {
    private val storeId get() = requireArguments().getLong(ProductActivity.KEY_STORE_ID)

    private lateinit var itemProductAdapter: ItemProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product, container, false)
        itemProductAdapter = ItemProductAdapter(emptyList())
        itemProductAdapter.onSelectedProductListener = this
        view.findViewById<RecyclerView>(R.id.product_rv).run {
            adapter = itemProductAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProducts()
    }


    private fun loadProducts(){
        requireContext().showLoading("Load data products")
        val endpoint = EndpointFactory.productEndPoint
        val token = LoginSharedPref(requireContext()).tokenBearer
        endpoint.getProductByStore(
            storeId = storeId,
            tokenBearer = token
        ).executeApi(
            requireContext(),
            onSuccess = {
                dismissLoading()
                showDataProducts(it)
            },
            onFailed = {
                showDataProducts(emptyList())
                dismissLoading()
                requireContext().showAlert(message = it.message ?: "Unknown error")
            }
        )
    }

    override fun onSelectedProduct(product: ProductResponse) {
        val intent = Intent(requireActivity(), ProductDetailActivity::class.java).apply {
            putExtra(ProductDetailActivity.KEY_PROD_ID, product.id)
        }
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showDataProducts(products: List<ProductResponse>) {
        itemProductAdapter.products = products
        itemProductAdapter.notifyDataSetChanged()
    }
}