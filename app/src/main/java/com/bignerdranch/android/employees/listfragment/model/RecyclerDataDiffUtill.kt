package com.bignerdranch.android.employees.listfragment.model

import androidx.recyclerview.widget.DiffUtil

class RecyclerDataDiffUtill(val oldList: List<RecyclerDataModel>, val newList: List<RecyclerDataModel>)
    : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: RecyclerDataModel = oldList[oldItemPosition]
        val newItem: RecyclerDataModel = newList[newItemPosition]
        if ((oldItem is Person) && (newItem is Person))
            return oldItem.employee.id == newItem.employee.id
        else if ((oldItem is NextYear) && (newItem is NextYear))
            return oldItem.date == newItem.date
        else
            return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: RecyclerDataModel = oldList[oldItemPosition]
        val newItem: RecyclerDataModel = newList[newItemPosition]
        if ((oldItem is Person) && (newItem is Person))
            return oldItem.employee == newItem.employee
        else
            return false
    }

}