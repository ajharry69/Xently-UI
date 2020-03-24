package com.xently.ui.demo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.xently.ui.demo.data.Employee
import com.xently.ui.demo.data.Employee.Department.IT
import com.xently.xui.utils.RefreshEvent
import com.xently.xui.utils.RefreshEvent.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale.ROOT
import kotlin.random.Random

abstract class EmployeeViewModel : ViewModel() {

    private val _observableEmployeeList: MutableLiveData<List<Employee>> = MutableLiveData()
    private val _observableEmployeeListRefreshEvent: MutableLiveData<RefreshEvent> =
        MutableLiveData()

    val observableEmployeeListRefreshEvent: LiveData<RefreshEvent>
        get() = _observableEmployeeListRefreshEvent

    fun getObservableEmployeeList(searchQuery: String?) =
        Transformations.map(_observableEmployeeList) {
            it.filterList(searchQuery)
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

    suspend fun employEmployee(): Boolean {
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
        val employeeList = arrayListOf(Employee(1, "Orinda", "Harrison", 54, IT, 98900f))
        for (i in 0 until limit) {
            val id = i + 1
            employeeList += pickRandomEmployee(id)
        }
        val filteredList = employeeList.filterList(searchQuery)
        setEmployeeList(filteredList)
        return filteredList
    }

    private suspend fun pickRandomEmployee(id: Int): Employee {
        val departments = Employee.Department.values()
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