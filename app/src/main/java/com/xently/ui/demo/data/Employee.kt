package com.xently.ui.demo.data

data class Employee(
    val id: Int,
    val firstName: String,
    val lastName: String?,
    val age: Int,
    val department: Department,
    val salary: Float
) {
    enum class Department {
        IT,
        ENGINEERING,
        SALES,
        ACCOUNTING
    }

    val name: String
        get() = "$firstName $lastName"

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + firstName.hashCode()
        result = 31 * result + (lastName?.hashCode() ?: 0)
        result = 31 * result + age
        result = 31 * result + department.hashCode()
        result = 31 * result + salary.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Employee

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (age != other.age) return false
        if (department != other.department) return false
        if (salary != other.salary) return false

        return true
    }

    override fun toString(): String = name
}