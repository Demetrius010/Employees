package com.bignerdranch.android.employees.listfragment.presenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.employees.R
import com.bignerdranch.android.employees.listfragment.view.IEmployeeListFragmentView
import com.bignerdranch.android.employees.utils.Employee
import com.bignerdranch.android.employees.utils.Repository
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton// используем чтоб привязать 1 презентер к нескольким VIEW
class EmployeeListFragmentPresenter @Inject constructor(val repository: Repository): MvpPresenter<IEmployeeListFragmentView>() {
    var currentPage: Int = -1
    var searchStr: String = ""
        set(value) {
            viewState.update(value, sortType)
            field = value
        }

    var sortType: String = ""
        set(value) {
            viewState.update(searchStr, value)
            field = value
        }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.startDataFetching()

        repository.fetchData().observeForever{ employeeList ->
            if(employeeList.isEmpty()){
                viewState.onFailure()
            }
            else{
                viewState.onSuccess(employeeList.sortedBy { it.firstName }, getDepartments(employeeList))// по умолчанию сортировка по имени
            }
        }
    }

    fun getDepartments(employeesList: List<Employee>): List<String>{
        val departmentsSet = mutableSetOf<String>()
        departmentsSet.add("ALL")
        for (emploee in employeesList){
            departmentsSet.add(emploee.department)
        }
        return departmentsSet.sortedBy{it}
    }
}