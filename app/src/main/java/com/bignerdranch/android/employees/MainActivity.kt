package com.bignerdranch.android.employees

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.employees.utils.DataFetcher
import com.bignerdranch.android.employees.utils.Repository
import com.bignerdranch.android.employees.utils.di.DaggerMainActivtyComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(){//, HasAndroidInjector {
   // @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // DaggerMainActivtyComponent.create().injectActivity(this)
    }
//
//    override fun androidInjector(): AndroidInjector<Any> {
//        return dispatchingAndroidInjector
//    }
}