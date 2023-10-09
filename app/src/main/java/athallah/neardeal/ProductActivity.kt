package athallah.neardeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import athallah.neardeal.R
import athallah.neardeal.fragments.DealFragment
import athallah.neardeal.fragments.ProductFragment
import athallah.neardeal.utils.showShortToast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProductActivity : AppCompatActivity() {
    private val storeId get() = intent.getLongExtra(KEY_STORE_ID, -1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        val tabLayout = findViewById<TabLayout>(R.id.product_tab)
        val viewPager = findViewById<ViewPager2>(R.id.product_vp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewPager.adapter = PagerAdapter(this, storeId)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val title = when (position) {
                PagerAdapter.TAB_PRODUCT_POS -> "Product"
                PagerAdapter.TAB_DEAL_POS -> "Deal"
                else -> null
            }
            tab.text = title
        }.attach()
//        showShortToast("Received : $storeId")
    }

    class PagerAdapter(
        activity: FragmentActivity,
        private val storeId: Long
    ) : FragmentStateAdapter(activity) {

        companion object {
            const val TAB_PRODUCT_POS = 0
            const val TAB_DEAL_POS = 1
        }

        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            val bundle = Bundle().apply {
                putLong(KEY_STORE_ID, storeId)
            }
            return when (position) {
                TAB_PRODUCT_POS -> ProductFragment().apply { arguments = bundle }
                TAB_DEAL_POS -> DealFragment().apply { arguments = bundle }
                else -> throw Exception("Tabs Only $itemCount")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val KEY_STORE_ID = "storeId"
    }
}