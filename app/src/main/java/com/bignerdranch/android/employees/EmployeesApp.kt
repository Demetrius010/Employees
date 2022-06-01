package com.bignerdranch.android.employees

import android.app.Application
import android.os.Bundle
import com.bignerdranch.android.employees.utils.di.DaggerMainActivtyComponent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class EmployeesApp: Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        DaggerMainActivtyComponent.create().injectApplication(this)

        //Firebase.initialize(this)
        analytics = Firebase.analytics// Obtain the FirebaseAnalytics instance.
        val bundle = Bundle()//logs a CONTENT in your app.
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "EmployeesApp")
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector// пробовал переносить в активити, там вылетает ошибка что dispatchingAndroidInjector не инициализированно
    }
}