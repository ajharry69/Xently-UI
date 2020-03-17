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
        hide: Boolean,
        hideUpIcon: Boolean,
        @DrawableRes upIcon: Int?
    ) {
        val toolbar = supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(hideUpIcon)
            upIcon?.let { setHomeAsUpIndicator(it) }
            if (hide) hide() else show()
        } ?: return

        if (!title.isNullOrBlank()) toolbar.title = title
    }

    override fun onSearchParamsChange(params: Bundle?) {
        searchParams = params
    }

    /**
     * Called when as [Intent.ACTION_SEARCH] action is received
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