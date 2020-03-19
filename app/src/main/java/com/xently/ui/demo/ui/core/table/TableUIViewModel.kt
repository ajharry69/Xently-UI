package com.xently.ui.demo.ui.core.table

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xently.ui.demo.R
import com.xently.ui.demo.data.Employee
import com.xently.ui.demo.viewmodels.EmployeeViewModel
import com.xently.xui.models.Cell
import com.xently.xui.models.ColumnHeader
import com.xently.xui.viewmodels.DataTableViewModel

class EmployeeTableViewModel : DataTableViewModel<Employee>() {
    override fun rowCellsData(context: Context, list: List<Employee>): List<List<Cell>> {
        return list.mapIndexed { index, employee ->
            val dataId = index + 1 + employee.id
            arrayListOf(
                Cell("${dataId + 1}", employee.id),
                Cell("${dataId + 2}", employee.firstName),
                Cell("${dataId + 3}", employee.lastName),
                Cell("${dataId + 4}", employee.age),
                Cell("${dataId + 5}", employee.department),
                Cell("${dataId + 6}", employee.salary)
            )
        }
    }

    override fun columnHeaderData(context: Context): List<ColumnHeader> {
        return listOf(
            ColumnHeader(context.getString(R.string.dt_col_id)),
            ColumnHeader(context.getString(R.string.dt_col_fname)),
            ColumnHeader(context.getString(R.string.dt_col_lname)),
            ColumnHeader(context.getString(R.string.dt_col_age)),
            ColumnHeader(context.getString(R.string.dt_col_department)),
            ColumnHeader(context.getString(R.string.dt_col_salary))
        )
    }
}

class TableUIViewModel : EmployeeViewModel()

@Suppress("UNCHECKED_CAST")
class TableUIViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = TableUIViewModel() as T
}
