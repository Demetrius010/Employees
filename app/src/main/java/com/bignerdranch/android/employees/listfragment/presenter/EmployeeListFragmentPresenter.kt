package com.bignerdranch.android.employees.listfragment.presenter

import android.app.Application
import android.content.Context
import androidx.lifecycle.Observer
import com.bignerdranch.android.employees.R
import com.bignerdranch.android.employees.listfragment.view.IEmployeeListFragmentView
import com.bignerdranch.android.employees.utils.Employee
import com.bignerdranch.android.employees.utils.api.Repository
import moxy.MvpPresenter

class EmployeeListFragmentPresenter : MvpPresenter<IEmployeeListFragmentView>() {

    private val repository = Repository.get()
    private var allEmployees: List<Employee> = listOf()


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.startDataFetching()

        repository.fetchData().observeForever{ employeeList ->
            if(employeeList.isEmpty()){
                viewState.onFailure(R.string.apiError)
            }
            else{
                allEmployees = employeeList
                viewState.onSuccess(employeeList)
            }
        }
    }

    fun getDepartments(): List<String>{
        val departmentsSet = mutableSetOf<String>()
        departmentsSet.add("ALL")
        for (emploee in allEmployees){
            departmentsSet.add(emploee.department)
        }
        return departmentsSet.sortedBy{it}
    }

}