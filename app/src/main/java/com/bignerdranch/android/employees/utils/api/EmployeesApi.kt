package com.bignerdranch.android.employees.utils.api

import com.bignerdranch.android.employees.utils.EmployeesModel
import retrofit2.Call
import retrofit2.http.GET

interface EmployeesApi {

    @GET("users")
    fun getEmployees(): Call<EmployeesModel>
}