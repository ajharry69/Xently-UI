package com.xently.xui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xently.xui.databinding.ListFragmentBinding
import com.xently.xui.utils.ui.ISearchParamsChange
import com.xently.xui.utils.ui.fragment.IListFragment
import com.xently.xui.utils.ui.view.hideViewsCompletely
import com.xently.xui.utils.ui.view.showAndEnableViews

/**
 * Updates UI with [List] of [T] objects. It also controls and responds to UI/[View] interactions
 * common to list-displaying screen e.g. refreshing, showing loading progress, filtering, sorting
 * and showing "**NO DATA**" message when list is empty.
 *
 * ## THIS IS THE DEFAULT OPTIONS MENU LAYOUT INFLATED BY THIS CLASS
 *
 * ```xml
 * <?xml version="1.0" encoding="utf-8"?>
 * <menu xmlns:android="http://schemas.android.com/apk/res/android"
 * xmlns:app="http://schemas.android.com/apk/res-auto">
 * <!-- Used to show search dialog -->
 * <item
 * android:id="@+id/menu_list_search"
 * android:icon="@drawable/ic_action_search"
 * android:orderInCategory="1"
 * android:title="@string/xui_menu_list_search"
 * app:showAsAction="ifRoom" />
 *
 * <item
 * android:id="@+id/menu_list_refresh"
 * android:icon="@drawable/ic_action_refresh"
 * android:orderInCategory="12"
 * android:title="@string/xui_menu_list_refresh"
 * app:showAsAction="ifRoom|withText" />
 * </menu>
 * ```
 *
 * **N/B:** By default clicking a **Search** menu item launches a `Search Dialog` if you'd like to
 * instead launch a `SearchView` consider implementing a solution like in your [onCreateOptionsMenu]
 * as described in [Developers Guide](https://developer.android.com/guide/topics/search/search-dialog#UsingSearchWidget):
 *
 * ```kotlin
 * val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
 * (menu.findItem(R.id.menu_list_search).actionView as SearchView).apply {
 *      setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
 * //   setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
 * //   isSubmitButtonEnabled = true
 *      isQueryRefinementEnabled = true
 * }
 * ```
 * @see SearchableActivity
 */
abstract class ListFragment<T> : SwipeRefreshFragment<T>(), IListFragment<T> {

    private var iSearchParamsChange: ISearchParamsChange? = null

    private var _binding: ListFragmentBinding? = null
    protected val binding: ListFragmentBinding by lazy { _binding!! }

    /**
     * used to identify the fragment(screen) from which search was initiated
     */
    open val searchParams: Bundle
        get() = Bundle().apply {
            putString(SEARCH_IDENTIFIER, this@ListFragment::class.java.name)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        enable "type-to-search" functionality, which activates the search dialog when the user
        starts typing on the keyboard—the keystrokes are inserted into the search dialog
         */
        activity?.setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ListFragmentBinding.inflate(inflater, container, false)
        initRequiredViews()
        return binding.root
    }

    override fun onDestroyView() {
        onRefreshListener = null
        binding.swipeRefresh.setOnRefreshListener(onRefreshListener)
        _binding = null
        super.onDestroyView()
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRequiredViews()
        onCreateRecyclerView(binding.list)

        // Shown when list is empty
        binding.error.text = noDataText ?: getString(R.string.xui_text_empty_list)

        with(binding.fab) {
            val fabClickListener = onFabClickListener(requireContext())
            if (fabClickListener == null) hideViewsCompletely(this)
            else showAndEnableViews(this)
            setOnClickListener(fabClickListener)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ISearchParamsChange) iSearchParamsChange = context
    }

    override fun onDetach() {
        iSearchParamsChange = null
        super.onDetach()
    }

    override fun onResume() {
        super.onResume()
        iSearchParamsChange?.onSearchParamsChange(searchParams)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.xui_menu_fragment_list, menu)
        /*if (!isSearchViewDialog) {
            val searchManager =
                requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
            menu.findItem(R.id.menu_list_search).apply {
                if (actionView !is SearchView) actionView = SearchView(requireContext())
                (actionView as SearchView).apply {
                    setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
//                    setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
//                    isSubmitButtonEnabled = true
                    isQueryRefinementEnabled = true
                }
            }
        }*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        // Used since it can be used to send custom parameters with search intent
        R.id.menu_list_search -> activity?.onSearchRequested() ?: false
        else -> super.onOptionsItemSelected(item)
    }

    protected open fun onFabClickListener(context: Context): View.OnClickListener? = null

    protected open fun onCreateRecyclerView(recyclerView: RecyclerView): RecyclerView {
        return recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initRequiredViews() {
        swipeRefresh = binding.swipeRefresh
        statusContainer = binding.errorContainer
    }

    companion object {
        const val SEARCH_IDENTIFIER = "SEARCH_IDENTIFIER"
    }
}