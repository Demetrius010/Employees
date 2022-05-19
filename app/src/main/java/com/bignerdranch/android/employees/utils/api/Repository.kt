package com.bignerdranch.android.employees.utils.api

import com.bignerdranch.android.employees.utils.DataFetcher

class Repository private constructor(){
    companion object{
        private var instance: Repository? = null

        fun get(): Repository{
            return instance ?: Repository()
        }
    }

    fun fetchData() = DataFetcher().fetchEmployees()
}