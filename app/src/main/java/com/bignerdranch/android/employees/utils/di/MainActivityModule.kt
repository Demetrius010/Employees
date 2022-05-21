package com.bignerdranch.android.employees.utils.di

import com.bignerdranch.android.employees.listfragment.presenter.EmployeeListFragmentPresenter
import com.bignerdranch.android.employees.listfragment.view.CollectionElementFragment
import com.bignerdranch.android.employees.listfragment.view.EmployeeListFragment
import com.bignerdranch.android.employees.listfragment.view.IEmployeeListFragmentView
import com.bignerdranch.android.employees.utils.Repository
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import moxy.MvpPresenter
import javax.inject.Singleton

@Module (includes = [FragmentsContributesInjectorModule::class])
object MainActivityModule{
    @Singleton
    @Provides
    fun provideRepository(): Repository {
        Repository.initialize()
        return Repository.get()
    }

//    @Singleton
//    @Provides
//    fun providePresenter(repository: Repository): EmployeeListFragmentPresenter = EmployeeListFragmentPresenter(repository)
}

@Module
abstract class FragmentsContributesInjectorModule {

    @ContributesAndroidInjector
    abstract fun employeeListFragmentInjector(): EmployeeListFragment

    @ContributesAndroidInjector
    abstract fun collectionElementFragmentInjector(): CollectionElementFragment
}
