package com.bignerdranch.android.employees.utils

import android.content.Context
import androidx.preference.PreferenceManager


private const val USER_PHONE = "userPhone"
private const val USER_NAME = "userName"
private const val USER_EMAIL = "userEmail"
private const val USER_LASTMSG = "userLastMsg"

object UserPreferences{//Вашему приложению нужен только один экземпляр QueryPreferences, который может использоваться всеми другими компонентами. Из-за этого мы используем ключевое слово object (вместо class), чтобы указать, что QueryPreferences — это синглтон.
    fun getPhoneNum(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(USER_PHONE, "")!!
    }

    fun setData(context: Context, phoneNum: String, name: String, email: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(USER_PHONE, phoneNum)
            .putString(USER_NAME, name)
            .putString(USER_EMAIL, email).apply()
    }

    fun getData(context: Context): List<String> {
        val data = mutableListOf<String>()
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        data.add(prefs.getString(USER_PHONE, "")!!)
        data.add(prefs.getString(USER_NAME, "")!!)
        data.add(prefs.getString(USER_EMAIL, "")!!)
        return data
    }

    fun setLastMsg(context: Context, msg: String, chatId: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(chatId, msg).apply()
    }

    fun getLastMsg(context: Context, chatId: String): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(chatId, "0")!!
    }
}