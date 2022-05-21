package com.bignerdranch.android.employees.utils

class Repository private constructor(){
    companion object{
        private var instance: Repository? = null
        fun initialize(){
            if(instance == null)
                instance = Repository()
        }
        fun get(): Repository{
            return instance ?: throw IllegalStateException("Repository must be initialized!")
        }
//        fun get(): Repository {
//            return instance ?: Repository()
//        }
    }

    fun fetchData() = DataFetcher().fetchEmployees()
}