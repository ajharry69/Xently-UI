package com.xently.ui.demo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.xently.ui.demo.data.Employee
import com.xently.xui.utils.RefreshEvent
import com.xently.xui.utils.RefreshEvent.State
import java.util.Locale.ROOT
import kotlin.random.Random

abstract class EmployeeViewModel : ViewModel() {

    private val _observableEmployeeList: MutableLiveData<List<Employee>> = MutableLiveData()
    private val _observableEmployeeListRefreshEvent: MutableLiveData<RefreshEvent> =
        MutableLiveData()

    val observableEmployeeListRefreshEvent: LiveData<RefreshEvent>
        get() = _observableEmployeeListRefreshEvent

    fun getObservableEmployeeList(searchQuery: String?): LiveData<List<Employee>> {
        return Transformations.map(_observableEmployeeList) {
            it.filterList(searchQuery)
        }
    }

    /**
     * @return true if [employee] as successfully fired(removed) else false
     */
    fun fireEmployee(employee: Employee): Boolean {
        val unfiredEmployees =
            _observableEmployeeList.value?.filter { it.id != employee.id }
        return if (unfiredEmployees != null) {
            setEmployeeList(unfiredEmployees)
            !unfiredEmployees.contains(employee)
        } else false
    }

    fun employEmployee(employee: Employee): Boolean {
        val newEmployees = _observableEmployeeList.value?.toMutableList()?.apply {
            add(employee.copy(id = size + 1))
        }?.toList()

        return if (newEmployees != null) {
            setEmployeeList(newEmployees)
            newEmployees.contains(employee)
        } else false
    }

    fun getEmployeeList(searchQuery: String? = null): List<Employee> {
        val employeeList = arrayListOf<Employee>()
        for (i in 0 until Random(5).nextInt(5, 10)) {
            val id = i + 1
            val departments = Employee.Department.values()
            val firstNameID = Random(999).nextInt(1000)
            employeeList.add(
                Employee(
                    id = id,
                    firstName = "FName $firstNameID",
                    lastName = "LName ${id + firstNameID}",
                    age = Random(10).nextInt(90),
                    department = departments[Random(0).nextInt(departments.size - 1)],
                    salary = Random(20000).nextInt(100000).toFloat()
                )
            )
        }
        val filteredList = employeeList.filterList(searchQuery)
        setEmployeeList(filteredList)
        return filteredList
    }

    private fun List<Employee>.filterList(searchQuery: String?): List<Employee> =
        if (searchQuery == null) this else {
            filter {
                "${it.name} ${it.department.name}".toLowerCase(ROOT)
                    .contains(Regex(searchQuery.toLowerCase(ROOT)))
            }
        }

    /**
     * Refreshes the list employees with [list]
     */
    private fun setEmployeeList(list: List<Employee>) {
        _observableEmployeeList.value = list
        _observableEmployeeListRefreshEvent.value = RefreshEvent(State.ENDED)
    }
}