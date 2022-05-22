package com.bignerdranch.android.employees.listfragment.view

import com.bignerdranch.android.employees.utils.Employee
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.SingleState

@AddToEndSingle
interface IEmployeeListFragmentView : MvpView {
    @OneExecution
    fun startDataFetching(){}
    fun onFailure(){}
    fun onSuccess(employees: List<Employee>, departments: List<String>){}

    fun update(searchStr: String, sortType: String){}
}