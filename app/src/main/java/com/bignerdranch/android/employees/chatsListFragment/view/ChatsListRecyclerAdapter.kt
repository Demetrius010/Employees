package com.bignerdranch.android.employees.chatsListFragment.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.employees.R
import com.bignerdranch.android.employees.chatsListFragment.model.Chat
import com.bignerdranch.android.employees.listfragment.view.EmployeeListFragmentDirections
import com.bumptech.glide.Glide

class ChatsListRecyclerAdapter : RecyclerView.Adapter<ChatViewHolder>() {
    private var mDataList: MutableList<Chat> = mutableListOf()
//    fun getData() = mDataList
//    fun setData(newData: List<Chat>) {
//        mDataList.clear()
//        mDataList.addAll(newData)
//        notifyDataSetChanged()
//    }

    fun addValue(chat: Chat) {
        if (mDataList.contains(chat)){
//            val i = mDataList.indexOf(chat)
//            mDataList[i] = chat
//            notifyItemChanged(i)
        }else{
            mDataList.add(chat)
            notifyItemInserted(mDataList.size-1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ChatViewHolder(inflater.inflate(R.layout.item_chat, parent, false))
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }
}

class ChatViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    private val name: TextView = view.findViewById(R.id.fullNameTV)
    private val lastMessage: TextView = view.findViewById(R.id.lastMsgTV)
    private val unseenTV: TextView = view.findViewById(R.id.unseenMsgTV)
    private val avatar: ImageView = view.findViewById(R.id.avatarChatImg)
    private lateinit var chatData: Chat

    init {
        view.setOnClickListener(this)
    }

    fun bind(itemChat: Chat) {
        chatData = itemChat
        name.text = itemChat.name
        lastMessage.text = itemChat.lastMessage
        if (itemChat.unseenMessage == 0) {
            unseenTV.visibility = View.INVISIBLE
        } else {
            unseenTV.visibility = View.VISIBLE
            unseenTV.text = itemChat.unseenMessage.toString()
        }

        Glide.with(view)
            .load(itemChat.avatarUrl)
            .into(avatar)
    }

    override fun onClick(p0: View?) {
        val action = ChatsListFragmentDirections.actionChatsListFragmentToTalkFragment(chatData)
        findNavController(view).navigate(action)
        //findNavController(p0!!).navigate(ChatsListFragmentDirections.actionChatsListFragmentToTalkFragment())
    }

}