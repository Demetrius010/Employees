package com.bignerdranch.android.employees.listfragment.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bignerdranch.android.employees.utils.Employee

const val ARG_ELEMENT = "element"

class CollectionAdapter(fragment: Fragment, val employees: List<Employee>, val departmens: List<String>) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return departmens.size
    }

    override fun createFragment(position: Int): Fragment {
        val filteredData = mutableListOf<Employee>()
        if (position>0){
            for (employee in employees){
                if (employee.department == departmens.elementAt(position)){
                    filteredData.add(employee)
                }
            }
        }
        else filteredData.addAll(employees)

        val fragment = CollectionElementFragment(filteredData)// Return a NEW fragment instance in createFragment(int)
        fragment.arguments = Bundle().apply {
            putInt(ARG_ELEMENT, position+1)// Our object is just an integer :-P
        }
        return fragment
    }
}