package com.bignerdranch.android.employees.listfragment.model

import com.bignerdranch.android.employees.utils.Employee

sealed class RecyclerDataModel
data class Person(val employee: Employee): RecyclerDataModel()
data class NextYear(val year: Int): RecyclerDataModel()