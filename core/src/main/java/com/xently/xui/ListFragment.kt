package com.xently.xui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.xently.xui.databinding.ListFragmentBinding
import com.xently.xui.utils.ui.ISearchParamsChange
import com.xently.xui.utils.ui.fragment.IListFragment

/**
 * Updates UI with [List] of [T] objects. It also controls and responds to UI/[View] interactions
 * common to list-displaying screen e.g. refreshing, showing loading progress, filtering, sorting
 * and showing "**NO DATA**" message when list is empty
 */
abstract class ListFragment<T> : SwipeRefreshFragment<T>(), IListFragment<T> {

    private var iSearchParamsChange: ISearchParamsChange? = null

    private var _binding: ListFragmentBinding? = null
    protected val binding: ListFragmentBinding
        get() = _binding!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRequiredViews()

        // A text view shown when list is empty
        binding.error.text = noDataText ?: getString(R.string.xui_text_empty_list)

        // TODO: Could be repetitive! Initialization is also done at sub-class level to attach
        //  adapters
        binding.list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
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

        /*
        // Cannot be used to send custom parameter for the intent
        val searchManager =
            requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.menu_list_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
//            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
//            isSubmitButtonEnabled = true
            isQueryRefinementEnabled = true
        }*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        // Used since it can be used to send custom parameters with search intent
        R.id.menu_list_search -> activity?.onSearchRequested() ?: false
        else -> super.onOptionsItemSelected(item)
    }

    private fun initRequiredViews() {
        swipeRefresh = binding.swipeRefresh
        statusContainer = binding.errorContainer
    }

    companion object {
        const val SEARCH_IDENTIFIER = "SEARCH_IDENTIFIER"
    }
}