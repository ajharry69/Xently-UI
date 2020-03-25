package com.xently.xui

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.xently.xui.utils.ui.IModifyToolbar
import com.xently.xui.utils.ui.ISearchParamsChange

abstract class SearchableActivity : AppCompatActivity(), IModifyToolbar, ISearchParamsChange {
    private var searchParams: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    override fun onSearchRequested(): Boolean {
        startSearch(null, false, searchParams, false)
        return true
    }

    override fun onModifyToolbar(
        title: String?,
        subTitle: String?,
        @DrawableRes upIcon: Int?,
        show: Boolean
    ) {
        val toolbar = supportActionBar?.apply {
            if (show) {
                upIcon?.let { setHomeAsUpIndicator(it) }
                show()
            } else hide()
        } ?: return

        if (!title.isNullOrBlank()) toolbar.title = title
        if (!subTitle.isNullOrBlank()) toolbar.subtitle = subTitle
    }

    override fun onSearchParamsChange(params: Bundle?) {
        searchParams = params
    }

    /**
     * Called when as [Intent.ACTION_SEARCH] action is received. If you are showing your list data
     * using [ListFragment], your could get the fragment/screen's identifier(class name) provided by
     * `{fragment-class-name}::class.java.name` e.g `EmployeeListFragment::class.java.name`
     * from which the search was initiated using:
     *
     * ```kotlin
     * // metadata: second parameter in the method
     * val searchInitiatorFragmentID = metadata.getString(ListFragment.SEARCH_IDENTIFIER)
     * ```
     * @param query search query received
     * @param metadata additional information passed along with the [query]
     */
    abstract fun onSearchIntentReceived(query: String, metadata: Bundle?)

    private fun handleIntent(intent: Intent) {
        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY) ?: return
            val metadata = intent.getBundleExtra(SearchManager.APP_DATA)
            onSearchIntentReceived(query, metadata)
        }
    }
}