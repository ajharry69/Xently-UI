package com.xently.xui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.ArrayRes
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.evrencoskun.tableview.listener.ITableViewListener
import com.evrencoskun.tableview.pagination.Pagination
import com.evrencoskun.tableview.pagination.Pagination.OnTableViewPageTurnedListener
import com.evrencoskun.tableview.sort.SortState
import com.xently.xui.adapters.table.ColumnHeaderViewHolder
import com.xently.xui.adapters.table.DataTableAdapter
import com.xently.xui.databinding.XuiDataTableFragmentBinding
import com.xently.xui.models.TableConfig
import com.xently.xui.utils.ListLoadEvent
import com.xently.xui.utils.ListLoadEvent.Status.*
import com.xently.xui.utils.RefreshEvent
import com.xently.xui.utils.RefreshEvent.State.*
import com.xently.xui.utils.getSharedPref
import com.xently.xui.utils.getThemedColor
import com.xently.xui.utils.ui.fragment.hideKeyboard
import com.xently.xui.utils.ui.view.hideViews
import com.xently.xui.utils.ui.view.showViews
import com.xently.xui.utils.ui.view.useText
import com.xently.xui.viewmodels.DataTableViewModel

abstract class DataTableFragment<T>(private val viewModel: DataTableViewModel<T>) :
    RefreshFragment<T>(), ITableViewListener, OnTableViewPageTurnedListener {

    /**
     * Retrieves **stored** value for default sort column
     */
    @Suppress("UNUSED_PARAMETER")
    fun getSavedDefaultSortColumnPosition(context: Context): Int? {
        val inAcceptable = Int.MIN_VALUE
        val pref =
            prefColumnSortState.getInt(this::class.java.name, inAcceptable)
        // Do not accept the default value as saved sort column position
        if (pref == inAcceptable) return null
        return pref
    }

    var pagination: Pagination? = null
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) set
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE) get
    private lateinit var prefColumnSortState: SharedPreferences
    private var totalEntries: Int = 0
    private var paginateFrom: Int = 1
    private var paginateTo: Int = Int.MAX_VALUE
    private val sortColumnPositionPrefValueKey = "sort_column_position"
    private var clickedDataTableColumnPosition: Int? = null
    private var columnHeaderViewHolder: ColumnHeaderViewHolder? = null
    private var tableConfig: TableConfig = TableConfig()

    protected val tableAdapter: DataTableAdapter<T> by lazy {
        DataTableAdapter(requireContext(), viewModel, tableConfig)
    }

    private var _binding: XuiDataTableFragmentBinding? = null
    protected val binding: XuiDataTableFragmentBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefColumnSortState = getSharedPref(requireContext(), "DATA_TABLE_SORT_COLUMN_ASCENDING")
        tableConfig = getTableConfig(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = XuiDataTableFragmentBinding.inflate(inflater, container, false).apply {
            with(dataTable) {
                if (tableConfig.readOnly) hideViews(headerContainer, footerContainer)
            }
        }
        initRequiredViews()
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRequiredViews()

        val tableBinding = binding.dataTable

        @ArrayRes
        val entriesResource: Int = R.array.xui_data_table_page_size_entries
        val dataTableEntries = requireContext().resources.getStringArray(entriesResource)

        val prefDataTablePageCount =
            getSharedPref(requireContext(), "DATA_TABLE_INITIAL_PAGE_COUNT")
        val dataTablePageCount: Int = prefDataTablePageCount.getInt(
            this::class.java.name,
            dataTableEntries[2].toInt()
        )

        with(tableBinding.table) {
            adapter = tableAdapter
            tableViewListener = this@DataTableFragment
            if (tableConfig.hideColumnAtPosition != null) hideColumn(tableConfig.hideColumnAtPosition!!)
            val sortCol = tableConfig.defaultSortColumnPosition
            if (sortCol != null) {
                sortColumn(sortCol, tableConfig.defaultSortOrder)
                setAsPreviousSortColumn(sortCol)
            }
        }

        pagination = if (tableConfig.readOnly) null else Pagination(
            tableBinding.table,
            dataTablePageCount,
            this
        )
        tableBinding.previous.setOnClickListener {
            pagination?.previousPage()
        }
        tableBinding.next.setOnClickListener {
            pagination?.nextPage()
        }
        tableBinding.page.apply {
            text = Editable.Factory.getInstance().newEditable("")
            setOnEditorActionListener { _, actionId, _ ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_GO -> {
                        tableBinding.submitPage.callOnClick()
                    }
                    else -> false
                }
            }
        }
        tableBinding.submitPage.setOnClickListener {
            pagination?.setOnTableViewPageTurnedListener(this@DataTableFragment)
            val page: String? = tableBinding.page.text.toString()

            if (page.isNullOrBlank()) return@setOnClickListener

            val p = page.toIntOrNull() ?: return@setOnClickListener

            with(tableBinding.page) {
                clearFocus()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    showSoftInputOnFocus = false
                }
            }
            hideKeyboard()
            pagination?.apply {
                goToPage(p)
                removeOnTableViewPageTurnedListener()
            }
        }

        with(tableBinding.pageSize) {
            background.colorFilter = PorterDuffColorFilter(
                context.getThemedColor(R.attr.colorControlNormal),
                PorterDuff.Mode.SRC_ATOP
            )
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter.createFromResource(
                requireContext(),
                entriesResource,
                android.R.layout.simple_spinner_item
            ).also {
                // Apply the adapter to the spinner
                adapter = it.apply {
                    // Specify the layout to use when the list of choices appears
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            }
            setSelection(dataTableEntries.indexOf(dataTablePageCount.toString()))
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // Use previously selected page count to set up pagination count
                    pagination?.itemsPerPage = dataTablePageCount
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (pagination == null) return
                    val pcs: String =
                        p0?.getItemAtPosition(p2) as String? ?: return // Page count str

                    val pc = pcs.toInt() // Page count
                    // Save selected page count for future initialization of pagination
                    with(prefDataTablePageCount.edit()) {
                        putInt(this::class.java.name, pc)
                        apply()
                    }

                    try {
                        // Use selected page count to set up pagination count
                        pagination?.itemsPerPage = pc
                    } catch (ex: Exception) {
                        // Ignore errors
                    }
                }
            }
        }
    }

    override fun onListLoadEvent(event: ListLoadEvent<T>) {
        val swipeRefresh = binding.swipeRefresh
        when (event.status) {
            NULL -> {
                onRefreshEvent(RefreshEvent(STARTED))
                hideViews(statusContainer)
                showViews(swipeRefresh)
            }
            EMPTY -> {
                onRefreshEvent()
                hideViews(swipeRefresh)
                showViews(statusContainer)
            }
            LOADED -> {
                onRefreshEvent()
                hideViews(statusContainer)
                showViews(swipeRefresh)
                with(event.data ?: emptyList()) {
                    totalEntries = size
                    tableAdapter.submitList(this)
                }
            }
        }
    }

    override fun onRefreshEvent(event: RefreshEvent) {
        val pb = binding.dataTable.progressBar
        when (event.state) {
            STARTED -> {
                showViews(pb)
                onRefreshRequested(event.forced)
            }
            ACTIVE -> showViews(pb)
            ENDED -> hideViews(pb)
        }
    }

    override fun onPageTurned(numItems: Int, itemsStart: Int, itemsEnd: Int) {
        paginateFrom = itemsStart + 1
        paginateTo = itemsEnd + 1
        binding.dataTable.run {
            footer.useText(
                getString(
                    R.string.xui_data_table_shown_entries,
                    paginateFrom,
                    paginateTo,
                    totalEntries
                )
            )
            page.useText("${pagination?.currentPage}")
        }
    }

    override fun onCellLongPressed(p0: RecyclerView.ViewHolder, p1: Int, p2: Int) = Unit

    override fun onColumnHeaderLongPressed(p0: RecyclerView.ViewHolder, p1: Int) = Unit

    override fun onRowHeaderClicked(p0: RecyclerView.ViewHolder, p1: Int) = Unit

    override fun onColumnHeaderClicked(p0: RecyclerView.ViewHolder, p1: Int) {
        if (tableConfig.readOnly) return
        columnHeaderViewHolder = (p0 as ColumnHeaderViewHolder?)?.getViewHolderAtPosition(p1)
        clickedDataTableColumnPosition = p1

        val sortAscendingPrefValueKey = "sort_ascending"

        fun saveAscendingSortState(ascending: Boolean = true) {
            with(prefColumnSortState.edit()) {
                putBoolean(sortAscendingPrefValueKey, ascending)
                apply()
            }
        }

        val sortAscending = prefColumnSortState.getBoolean(sortAscendingPrefValueKey, true)

        resetPreviousSortColumnState(p0, p1)

        /*
         * Switch SortState values between ASCENDING and DESCENDING depending on column header click
         * state
         */
        val sortState: SortState = if (sortAscending) {
            saveAscendingSortState(false)
            SortState.ASCENDING
        } else {
            saveAscendingSortState()
            SortState.DESCENDING
        }

        with(binding.dataTable.table) {
            sortColumn(p1, sortState)
            // Recalculate of the width values of the columns
            remeasureColumnWidth(p1)
        }
    }

    override fun onCellClicked(p0: RecyclerView.ViewHolder, p1: Int, p2: Int) = Unit

    override fun onRowHeaderLongPressed(p0: RecyclerView.ViewHolder, p1: Int) = Unit

    open fun getTableConfig(context: Context) =
        TableConfig().copy(defaultSortColumnPosition = getSavedDefaultSortColumnPosition(context))

    private fun initRequiredViews() {
        statusContainer = binding.errorContainer
    }

    /**
     * Reset previous sort column to its original state
     */
    private fun resetPreviousSortColumnState(viewHolder: RecyclerView.ViewHolder, column: Int) {
        val savedSortColumn: Int = prefColumnSortState.getInt(
            sortColumnPositionPrefValueKey,
            tableConfig.defaultSortColumnPosition ?: column
        )

        if (savedSortColumn != column || column != tableConfig.defaultSortColumnPosition) {
            // Position is for previous sort column. Hide it's sort icon button
            if (viewHolder is ColumnHeaderViewHolder) viewHolder.getViewHolderAtPosition(
                savedSortColumn
            )?.hideSortIcon()
        }

        // Table is sorted using column at position (column). Save as sort column position
        setAsPreviousSortColumn(column)
    }

    /**
     * Save [column] as previous sort column position
     */
    private fun setAsPreviousSortColumn(column: Int) {
        with(prefColumnSortState.edit()) {
            putInt(sortColumnPositionPrefValueKey, column)
            apply()
        }
    }
}