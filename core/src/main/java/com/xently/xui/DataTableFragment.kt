package com.xently.xui

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.ArrayRes
import androidx.recyclerview.widget.RecyclerView
import com.evrencoskun.tableview.listener.ITableViewListener
import com.evrencoskun.tableview.pagination.Pagination
import com.evrencoskun.tableview.pagination.Pagination.OnTableViewPageTurnedListener
import com.evrencoskun.tableview.sort.SortState
import com.xently.xui.adapters.table.ColumnHeaderViewHolder
import com.xently.xui.adapters.table.DataTableAdapter
import com.xently.xui.databinding.DataTableFragmentBinding
import com.xently.xui.utils.ListLoadEvent
import com.xently.xui.utils.ListLoadEvent.Status.LOADED
import com.xently.xui.utils.getSharedPref
import com.xently.xui.viewmodels.DataTableViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class DataTableFragment<T>(private val viewModel: DataTableViewModel<T>) :
    SwipeRefreshFragment<T>(), ITableViewListener, OnTableViewPageTurnedListener {

    /**
     * A set of column positions whose values should be aligned to the **CENTER** of the cell.
     * Default alignment is to the **START** or **LEFT** of the cell. **0** is the position of the
     * first column in the data-table
     */
    open val alignValuesCenter: Set<Int> = emptySet()

    /**
     * A set of column positions whose values should be aligned to the **RIGHT** of the cell.
     * Default alignment is to the **START** or **LEFT** of the cell. **0** is the position of the
     * first column in the data-table
     */
    open val alignValuesRight: Set<Int> = emptySet()

    /**
     * Do not show column at position provided. **0** is the position of the first column in the
     * data-table
     */
    open val hideColumnAtPosition: Int? = null

    /**
     * Setup/initialize data table sorted in [defaultSortOrder] order using values at column index
     * provided. **0** is the position of the first column in the data-table
     */
    open val defaultSortColumnPosition: Int? = null

    /**
     * Sort order to use when sorting table column at [defaultSortColumnPosition]
     */
    open val defaultSortOrder: SortState = SortState.ASCENDING

    /**
     * Retrieves **stored** value for default sort column
     */
    val savedDefaultSortColumnPosition: Int?
        get() {
            val inAcceptable = Int.MIN_VALUE
            val pref =
                prefColumnSortState.getInt(this@DataTableFragment::class.java.name, inAcceptable)
            // Do not accept the default value as saved sort column position
            if (pref == inAcceptable) return null
            return pref
        }

    private var pagination: Pagination<T>? = null
    private lateinit var prefColumnSortState: SharedPreferences
    private var totalEntries: Int = 0
    private var paginateFrom: Int = 1
    private var paginateTo: Int = Int.MAX_VALUE
    private val sortColumnPositionPrefValueKey = "sort_column_position"
    private var clickedDataTableColumnPosition: Int? = null
    private var columnHeaderViewHolder: ColumnHeaderViewHolder? = null

    protected val tableAdapter: DataTableAdapter<T> by lazy {
        DataTableAdapter(requireContext(), viewModel, alignValuesCenter, alignValuesRight)
    }

    private var _binding: DataTableFragmentBinding? = null
    protected val binding: DataTableFragmentBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefColumnSortState = getSharedPref(requireContext(), "DATA_TABLE_SORT_COLUMN_ASCENDING")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataTableFragmentBinding.inflate(inflater, container, false)
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
        val entriesResource: Int = R.array.data_table_entries
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
            if (hideColumnAtPosition != null) hideColumn(hideColumnAtPosition!!)
            if (defaultSortColumnPosition != null) {
                val sortCol = defaultSortColumnPosition ?: return@with
                sortColumn(sortCol, defaultSortOrder)
                setAsPreviousSortColumn(sortCol)
            }
        }

        binding.swipeRefresh.apply {
            setOnRefreshListener(onRefreshListener)
        }

        pagination = Pagination(tableBinding.table, dataTablePageCount, this)
        tableBinding.previous.setOnClickListener {
            pagination?.previousPage()
        }
        tableBinding.next.setOnClickListener {
            pagination?.nextPage()
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
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter.createFromResource(
                requireContext(),
                entriesResource,
                android.R.layout.simple_spinner_item
            ).also { adp ->
                // Specify the layout to use when the list of choices appears
                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                adapter = adp
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onListLoadEvent(event: ListLoadEvent<T>) {
        when (event.status) {
            LOADED -> {
                super.onListLoadEvent(event)
                val data = event.data ?: emptyList()
                totalEntries = data.size
                tableAdapter.submitList(data)
            }
            else -> super.onListLoadEvent(event)
        }
    }

    override fun onPageTurned(numItems: Int, itemsStart: Int, itemsEnd: Int) {
        paginateFrom = itemsStart + 1
        paginateTo = itemsEnd + 1
        binding.dataTable.footer.text = getString(
            R.string.xui_data_table_shown_entries,
            paginateFrom,
            paginateTo,
            totalEntries
        )
        binding.dataTable.page.text =
            Editable.Factory.getInstance().newEditable("${pagination?.currentPage}")
    }

    override fun onCellLongPressed(p0: RecyclerView.ViewHolder, p1: Int, p2: Int) = Unit

    override fun onColumnHeaderLongPressed(p0: RecyclerView.ViewHolder, p1: Int) = Unit

    override fun onRowHeaderClicked(p0: RecyclerView.ViewHolder, p1: Int) = Unit

    override fun onColumnHeaderClicked(p0: RecyclerView.ViewHolder, p1: Int) {

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

    private fun initRequiredViews() {
        swipeRefresh = binding.swipeRefresh
        statusContainer = binding.errorContainer
    }

    /**
     * Reset previous sort column to its original state
     */
    private fun resetPreviousSortColumnState(viewHolder: RecyclerView.ViewHolder, column: Int) {
        val savedSortColumnPosition: Int = prefColumnSortState.getInt(
            sortColumnPositionPrefValueKey,
            defaultSortColumnPosition ?: column
        )

        if (savedSortColumnPosition != column || column != defaultSortColumnPosition) {
            // Position is for previous sort column. Hide it's sort icon button
            if (viewHolder is ColumnHeaderViewHolder) viewHolder.getViewHolderAtPosition(
                savedSortColumnPosition
            )?.hideSort()
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