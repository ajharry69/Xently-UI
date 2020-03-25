package com.xently.ui.demo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.xently.ui.demo.data.Employee
import com.xently.xui.utils.RefreshEvent
import com.xently.xui.utils.RefreshEvent.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale.ROOT
import kotlin.random.Random

const val FOUNDER_FIRST_NAME: String = "Harrison"

const val FOUNDER_LAST_NAME: String = "Orinda"

abstract class EmployeeViewModel : ViewModel() {

    private val departments = Employee.Department.values()
    private var founder: Employee = Employee(
        1,
        FOUNDER_LAST_NAME,
        FOUNDER_FIRST_NAME,
        54,
        departments[Random.nextInt(0, departments.size)],
        98900f
    )
    private val _observableEmployeeList = MutableLiveData<List<Employee>>()
    private val _observableEmployeeListRefreshEvent = MutableLiveData<RefreshEvent>()

    val observableEmployeeListRefreshEvent: LiveData<RefreshEvent>
        get() = _observableEmployeeListRefreshEvent

    fun getObservableEmployeeList(searchQuery: String?) =
        Transformations.map(_observableEmployeeList) {
            it.filterList(searchQuery)
        }

    /**
     * @return true if [employee] as successfully fired(removed) else false
     */
    fun fireEmployee(employee: Employee): Exception? {
        if (employee == founder) return Exception("Cannot fire founder employee!")
        val unfiredEmployees =
            _observableEmployeeList.value?.filter { it.id != employee.id }
        return if (unfiredEmployees != null) {
            setEmployeeList(unfiredEmployees)
            !unfiredEmployees.contains(employee)
            null
        } else Exception("Error firing employee!")
    }

    suspend fun hireEmployee(): Boolean {
        val employee = pickRandomEmployee(1)

        val newEmployees = _observableEmployeeList.value?.toMutableList() ?: arrayListOf()
        newEmployees.add(employee.copy(id = newEmployees.size + 1))

        setEmployeeList(newEmployees)
        return newEmployees.contains(employee)
    }

    suspend fun getEmployeeList(
        searchQuery: String? = null,
        limit: Int = Random.nextInt(5, 10)
    ): List<Employee> {
        val employees = arrayListOf<Employee>()
        for (i in 0 until limit) employees += pickRandomEmployee(i + 1)
        // Add in random position of the employee list considered as the founder(always present)
        // whose role in the company can be in any department
        founder = founder.copy(id = employees.size + 1)
        employees.add(Random.nextInt(1, employees.size - 1), founder)
        val filteredList = employees.filterList(searchQuery)
        setEmployeeList(filteredList)
        return filteredList
    }

    private suspend fun pickRandomEmployee(id: Int): Employee {
        val firstNameID = withContext(Dispatchers.Default) {
            Random.nextInt(101, 1000)
        }
        val age = withContext(Dispatchers.Default) {
            Random.nextInt(10, 90)
        }
        val department = withContext(Dispatchers.Default) {
            Random.nextInt(0, departments.size)
        }
        val salary = withContext(Dispatchers.Default) {
            Random.nextInt(20000, 100000)
        }
        return Employee(
            id = id,
            firstName = "FName $firstNameID",
            lastName = "LName ${id + firstNameID}",
            age = age,
            department = departments[department],
            salary = salary.toFloat()
        )
    }

    private fun List<Employee>.filterList(searchQuery: String?) =
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
        _observableEmployeeList.postValue(list)
        _observableEmployeeListRefreshEvent.postValue(RefreshEvent(State.ENDED))
    }
}