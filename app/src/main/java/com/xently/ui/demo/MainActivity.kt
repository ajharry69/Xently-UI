package com.xently.ui.demo

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.xently.ui.demo.databinding.MainActivityBinding
import com.xently.ui.demo.ui.core.list.ListUIFragment
import com.xently.ui.demo.ui.core.list.filter.FilteredListFragment
import com.xently.ui.demo.ui.core.list.filter.FilteredListFragmentArgs
import com.xently.xui.ListFragment
import com.xently.xui.SearchableActivity
import com.xently.xui.utils.Log

class MainActivity : SearchableActivity() {

    private lateinit var configuration: AppBarConfiguration
    private lateinit var controller: NavController
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment?

        if (navHostFragment == null) Log.show(
            LOG_TAG,
            "Nav Host Fragment is null",
            type = Log.Type.ERROR
        ) else {
            controller = navHostFragment.navController
            configuration = AppBarConfiguration(setOf(R.id.dest_home))
            setupActionBarWithNavController(controller, configuration)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        item.onNavDestinationSelected(controller) || super.onOptionsItemSelected(item)

    override fun onSupportNavigateUp(): Boolean =
        controller.navigateUp(configuration) || super.onSupportNavigateUp()

    override fun onSearchIntentReceived(query: String, metadata: Bundle?) {
        Log.show(LOG_TAG, "Search Query: $query")

        if (metadata == null) return

        if (metadata.getString(ListFragment.SEARCH_IDENTIFIER) == ListUIFragment::class.java.name || metadata.getString(
                ListFragment.SEARCH_IDENTIFIER
            ) == FilteredListFragment::class.java.name
        ) {
            controller.navigate(
                R.id.dest_filtered_employee_list,
                FilteredListFragmentArgs(searchQuery = query).toBundle(),
                NavOptions.Builder().setLaunchSingleTop(true).build()
            )
        }
    }

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
    }
}
