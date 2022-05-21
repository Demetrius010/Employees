package com.bignerdranch.android.employees

import android.app.Application
import android.os.Bundle
import com.bignerdranch.android.employees.utils.di.DaggerMainActivtyComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class EmployeesApp: Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerMainActivtyComponent.create().injectApplication(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector// пробовал переносить в активити, там вылетает ошибка что dispatchingAndroidInjector не инициализированно
    }
}