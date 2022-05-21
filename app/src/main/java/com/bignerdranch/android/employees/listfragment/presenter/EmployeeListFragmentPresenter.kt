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
//    @Inject
//    lateinit var repository: Repository

    private var allEmployees: List<Employee> = listOf()
    fun setSearchStr(string: String){
        viewState.update(string)
//        for (v in attachedViews){
//            Log.d("EmployeeListFragmentPresenter", "setSearchStr attachedViews: ${v.toString()}")
//        }
    }

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