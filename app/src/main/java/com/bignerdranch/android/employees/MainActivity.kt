package com.bignerdranch.android.employees

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.employees.utils.DataFetcher

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataFetcher = DataFetcher()
        dataFetcher.fetchEmployees()
    }
}