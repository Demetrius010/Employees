package com.bignerdranch.android.employees.utils.di

import com.bignerdranch.android.employees.chatsListFragment.view.ChatsListFragment
import com.bignerdranch.android.employees.listfragment.presenter.EmployeeListFragmentPresenter
import com.bignerdranch.android.employees.listfragment.view.CollectionElementFragment
import com.bignerdranch.android.employees.listfragment.view.EmployeeListFragment
import com.bignerdranch.android.employees.listfragment.view.IEmployeeListFragmentView
import com.bignerdranch.android.employees.logingFragment.view.LoginFragment
import com.bignerdranch.android.employees.talkFragment.view.TalkFragment
import com.bignerdranch.android.employees.utils.Repository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

    @Singleton
    @Provides
    fun provideUsersDBRef(): DatabaseReference{
        val database = Firebase.database( "https://employees-3f7e9-default-rtdb.europe-west1.firebasedatabase.app/")
        return database.getReference("users")// Write a message to the database
    }
}

@Module
abstract class FragmentsContributesInjectorModule {

    @ContributesAndroidInjector
    abstract fun loginElementFragmentInjector(): LoginFragment

    @ContributesAndroidInjector
    abstract fun employeeListFragmentInjector(): EmployeeListFragment

    @ContributesAndroidInjector
    abstract fun collectionElementFragmentInjector(): CollectionElementFragment

    @ContributesAndroidInjector
    abstract fun chatsListFragmentInjector(): ChatsListFragment

    @ContributesAndroidInjector
    abstract fun talkFragmentInjector(): TalkFragment
}
