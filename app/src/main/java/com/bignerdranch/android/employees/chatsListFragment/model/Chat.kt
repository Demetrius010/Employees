package com.bignerdranch.android.employees.chatsListFragment.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(val mobile: String,
                val name: String,
                val avatarUrl: String,
                val lastMessage: String,
                val unseenMessage: Int,
                val chatKey: String) : Parcelable