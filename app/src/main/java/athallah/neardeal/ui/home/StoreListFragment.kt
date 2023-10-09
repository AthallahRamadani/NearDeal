package athallah.neardeal.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.RadioGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import athallah.neardeal.MapActivity
import athallah.neardeal.ProductActivity
import athallah.neardeal.adapters.ItemStoreAdapter
import athallah.neardeal.api.EndpointFactory
import athallah.neardeal.api.endpoints.StoreEndPoint
import athallah.neardeal.api.responses.StoreResponse
import athallah.neardeal.R
import athallah.neardeal.databinding.FragmentHomeBinding
import athallah.neardeal.sharedPrefs.LoginSharedPref
import athallah.neardeal.utils.*
import dika.neardeal.util.AcquireLocationFailedCause
import dika.neardeal.util.getCurrentLocation

class StoreListFragment : Fragment(), ItemStoreAdapter.OnSelectedStoreListener {
    private lateinit var itemStoreAdapter: ItemStoreAdapter

    private var _binding: FragmentHomeBinding? = null
    private lateinit var reqPermissionRequestLauncher: ActivityResultLauncher<Array<String>>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    //untuk setup layout saja
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        itemStoreAdapter = ItemStoreAdapter(emptyList())
        itemStoreAdapter.onSelectedStoreListener = this
        binding.storeRv.run {
            adapter = itemStoreAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        setHasOptionsMenu(true)
        reqPermissionRequestLauncher = createActivityResultLauncher(
            onPermissionDenied = {
                requireContext().showAlert(
                    title = "Permission Required",
                    message = "Location access required"
                )
            },
            onPermissionsGranted = {

            }
        )
        return binding.root
    }


    //untuk loading data
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadStores()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSelectedStore(store: StoreResponse) {
        requireContext().showShortToast(store.name)
        val intent = Intent(requireActivity(), ProductActivity::class.java).apply {
            putExtra(ProductActivity.KEY_STORE_ID, store.id)
        }
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showDataStores(stores: List<StoreResponse>) {
        itemStoreAdapter.stores = stores
        itemStoreAdapter.notifyDataSetChanged()
    }

    private fun loadStores() {
        requireContext().showLoading("Loading stores....")
        val apiEndPoint: StoreEndPoint = EndpointFactory.storeEndpoint
        val token = LoginSharedPref(requireContext()).tokenBearer
        apiEndPoint.getAllStores(token).executeApi(
            requireContext(),
            onSuccess = {
                dismissLoading()
                showDataStores(it)
            },
            onFailed = {
               dismissLoading()
                showDataStores(emptyList())
                requireContext().showShortToast(message = it.message ?: "unknown error")
            }
        )

//        val call  = apiEndPoint.getAllStores()
//        call.enqueue(object : Callback<ApiResponse<List<StoreResponse>>?> {
//            override fun onResponse(
//                call: Call<ApiResponse<List<StoreResponse>>?>,
//                response: Response<ApiResponse<List<StoreResponse>>?>
//            ) {
//                PopupUtil.dismissDialog()
//                val storeResponse = response.body()
//                if (storeResponse != null) {
//                    if (storeResponse.isSuccess) {
//                        showDataStores(storeResponse.data ?: emptyList())
//                    } else {
//                        showDataStores(emptyList())
//                    }
//                } else {
//                    showDataStores(emptyList())
//                }
//            }
//
//            override fun onFailure(call: Call<ApiResponse<List<StoreResponse>>?>, t: Throwable) {
//                PopupUtil.dismissDialog()
//                showDataStores(emptyList())
//            }
//        })
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onCreateOptionsMenu(menu, inflater)",
            "androidx.fragment.app.Fragment"
        )
    )
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_store_list_menu, menu)
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onOptionsItemSelected(item)",
            "androidx.fragment.app.Fragment"
        )
    )
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_menu -> {
                showFilterDialog()
                true
            }
            R.id.map_menu -> {
                startMapActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun startMapActivity() {
        val stores = itemStoreAdapter.stores
        if (stores.isEmpty()) {
            requireActivity().showAlert(message = "No data store to show on map")
            return
        }
        val storeJson = stores.toJson()
        val intent = Intent(requireActivity(), MapActivity::class.java).apply {
            putExtra(MapActivity.DATA_STORE_JSON, storeJson)
        }
        startActivity(intent)
    }

    private fun showFilterDialog() {
        val dialog = requireContext().showCustomDialog(R.layout.dialog_filter_store)
        dialog.findViewById<Button>(R.id.cancel_btn).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<Button>(R.id.apply_btn).setOnClickListener {
            dialog.dismiss()
            val isAllStore =
                dialog.findViewById<RadioGroup>(R.id.filter_rb).checkedRadioButtonId == R.id.all_rb
            if (!isAllStore) {
                loadStoresNearMe()
            } else {
                loadStores()
            }
        }
    }

    private fun loadStoresNearMe() {
        requireContext().showLoading("Loading stores....")
        requireContext().getCurrentLocation(
            onLocationAcquired = { location ->
                val apiEndPoint: StoreEndPoint = EndpointFactory.storeEndpoint
                val token = LoginSharedPref(requireContext()).tokenBearer
                apiEndPoint.getAllStoresNearMe(
                    lat = location.latitude,
                    lng = location.longitude,
                    tokenBearer = token
                ).executeApi(
                    requireContext(),
                    onSuccess = {
                        dismissLoading()
                        showDataStores(it)
                    },
                    onFailed = {
                        dismissLoading()
                        showDataStores(emptyList())
                        requireContext().showShortToast(message = it.message ?: "unknown error")
                    }
                )
            },
            onLocationAcquireFailed = { cause ->
                dismissLoading()
                when (cause) {
                    AcquireLocationFailedCause.PERMISSION_NOT_GRANTED -> {

                        reqPermissionRequestLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                    AcquireLocationFailedCause.ACQUIRING_TASK_NOT_SUCCESS -> {
                        requireContext().showAlert(
                            message = "Unable to access your current loction"
                        )
                    }
                }
            }
        )

    }

}