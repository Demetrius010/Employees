package com.bignerdranch.android.employees.talkFragment.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.employees.R
import com.bignerdranch.android.employees.talkFragment.model.Message
import com.bignerdranch.android.employees.utils.UserPreferences

class TalkRecyclerAdapter : RecyclerView.Adapter<MessageViewHolder>(){
    private val mDataList: MutableList<Message> = mutableListOf()
    fun getData() = mDataList
    fun setData(newData: List<Message>){
        mDataList.clear()
        mDataList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MessageViewHolder(inflater.inflate(R.layout.item_message, parent, false))
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }
}

class MessageViewHolder(val view: View): RecyclerView.ViewHolder(view){
    val mymobile = UserPreferences.getPhoneNum(view.context)
    private val myMessage: TextView = view.findViewById(R.id.myMsgTV)
    private val myTime: TextView = view.findViewById(R.id.myTimeTV)
    private val opponentMessage: TextView = view.findViewById(R.id.opponentMsgTV)
    private val opponentTime: TextView = view.findViewById(R.id.oppMsgTimeTV)

    fun bind(item: Message){
        if(item.mobile.equals(mymobile)){
            opponentMessage.visibility = View.INVISIBLE
            opponentTime.visibility = View.INVISIBLE
            myMessage.text = item.message
            myTime.text = item.date + " " + item.time
        }else{
            myMessage.visibility = View.INVISIBLE
            myTime.visibility = View.INVISIBLE
            opponentMessage.text = item.message
            opponentTime.text = item.date + " " + item.time
        }
    }
}