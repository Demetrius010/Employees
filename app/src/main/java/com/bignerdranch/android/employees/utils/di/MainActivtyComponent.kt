package com.bignerdranch.android.employees.utils.di

import com.bignerdranch.android.employees.EmployeesApp
import dagger.Component
import javax.inject.Singleton
import dagger.android.AndroidInjectionModule

@Singleton
@Component(modules = [MainActivityModule::class, AndroidInjectionModule::class])
interface MainActivtyComponent {
    fun injectApplication(employeesApp: EmployeesApp)
}