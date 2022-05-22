package com.bignerdranch.android.employees.listfragment.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bignerdranch.android.employees.utils.Employee

const val ARG_ELEMENT = "element"

class CollectionAdapter(fragment: Fragment, val employees: List<Employee>, val departmens: List<String>) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return departmens.size
    }

    override fun createFragment(position: Int): Fragment {
        val employeesByDepartment = mutableListOf<Employee>()
        if (position>0){
            for (employee in employees){
                if (employee.department == departmens.elementAt(position)){
                    employeesByDepartment.add(employee)
                }
            }
        }
        else employeesByDepartment.addAll(employees) // на нулевой позиции фильтр "ALL"
        val fragment = CollectionElementFragment()//(employeesByDepartment)// Return a NEW fragment instance in createFragment(int)
        fragment.arguments = Bundle().apply {
            putParcelableArrayList(ARG_ELEMENT, employeesByDepartment as ArrayList<Employee>)
        }
        return fragment
    }
}