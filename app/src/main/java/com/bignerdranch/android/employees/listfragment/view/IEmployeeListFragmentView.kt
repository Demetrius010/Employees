package com.bignerdranch.android.employees.listfragment.view

import com.bignerdranch.android.employees.utils.Employee
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface IEmployeeListFragmentView : MvpView {
    @OneExecution
    fun startDataFetching()
    fun onFailure(errorStrId: Int)
    fun onSuccess(data: List<Employee>)
}