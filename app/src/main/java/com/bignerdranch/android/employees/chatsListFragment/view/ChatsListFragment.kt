package com.bignerdranch.android.employees.chatsListFragment.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.employees.chatsListFragment.model.Chat
import com.bignerdranch.android.employees.databinding.FragmentChatsListBinding
import com.bignerdranch.android.employees.utils.Repository
import com.bignerdranch.android.employees.utils.UserPreferences
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ChatsListFragment : Fragment() {
    private var _binding: FragmentChatsListBinding? = null //используется view binding для ссылки на layout
    private val binding //This property is only valid between onCreateView and onDestroyView.
        get() = _binding!!

    private val recyclerChatsListAdapter = ChatsListRecyclerAdapter()
    //private val chatList: MutableList<Chat> = mutableListOf()

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var dbRef: DatabaseReference
    private val fullRef = Firebase.database( "https://employees-3f7e9-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
    private val chatRef = Firebase.database( "https://employees-3f7e9-default-rtdb.europe-west1.firebasedatabase.app/").getReference("chat")

    private var unseenMsg = 0
    private var lastMsg = ""
    private var chatKey = ""
    private var dataset = false

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChatsListBinding.inflate(inflater, container, false)
        binding.chatBackBtn.setOnClickListener{
            findNavController().popBackStack()
        }

        //        recyclerChatsListAdapter.setData(chatList)
        binding.chatsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerChatsListAdapter
        }

        fullRef.addValueEventListener(object : ValueEventListener { //собираем данные для recycler + если данные поеменялись мы пересобираем их заново
            override fun onDataChange(snapshot: DataSnapshot) {//Этот метод запускается один раз, когда прослушиватель подключен, и снова каждый раз, когда данные, включая дочерние, изменяются.
                //chatList.clear()

                for (snphUser in snapshot.child("users").children){// проход по пользователям (которые определяются номерами телефонов)
                    val otherPhone = snphUser.key
                    dataset = false
                    if (!otherPhone.equals(repository.phone)){//если это данные не текущего пользователя
                        val otherName = snphUser.child("name").getValue(String::class.java)
                        val otherAvatar = snphUser.child("avatar").getValue(String::class.java)//{8900={name="name", avatar="https://...", email="@mail"},   8999={...}}
                        Log.d("ChatsListFragment", "$otherPhone")

                        //ДЛЯ КАЖДОГО ПОЛЬЗОВАТЕЛЯ КРОМЕ ТЕКУЩЕГО
                        chatRef.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                unseenMsg = 0
                                lastMsg = ""
                                chatKey = ""
                                val chatCounts = snapshot.childrenCount
                                Log.d("ChatsListFragment", "snapshot.CHAT  ,${snapshot}")
                                //{ key = chat, value = {990={user_2=990, user_1=12, messages={   1653551424={msg=d, phone=990}, 1653551414={msg=d, phone=990},
                                if (chatCounts > 0){//если есть cуществующие чаты
                                    for (singleChatSnph in snapshot.children){//проходим по каждому чату 990={user_2=990, user_1=12, messages={   1653551424={msg=d, phone=990}, 1653551414={msg=d, phone=990},
                                        val singleChatKey = singleChatSnph.key
                                        chatKey = singleChatKey.toString()
                                        if (singleChatSnph.hasChild("user_1") && singleChatSnph.hasChild("user_2") && singleChatSnph.hasChild("messages")){
                                            val userOne = singleChatSnph.child("user_1").getValue(String::class.java)
                                            val userTwo = singleChatSnph.child("user_2").getValue(String::class.java)
                                            if ((userOne.equals(otherPhone) && userTwo.equals(repository.phone)) || (userOne.equals(repository.phone) && userTwo.equals(otherPhone))){//если это чат с текущим пользоваетлем
                                                val lastSeenMsg = UserPreferences.getLastMsg(requireContext(), singleChatKey!!).toLong()
                                                for (singleChatMsgSnph in singleChatSnph.child("messages").children){//проходим по каждому сообщению чата messages={   1653551424={msg=d, phone=990}, 1653551414={msg=d, phone=990},
                                                    val messageKey = singleChatMsgSnph.key?.toLong() ?: 0
                                                    lastMsg = singleChatMsgSnph.child("msg").getValue(String::class.java).toString()
                                                    if(messageKey > lastSeenMsg){
                                                        unseenMsg++
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                 //if(!dataset){
                                dataset = true
                                if (otherPhone!=null && otherName!=null && otherAvatar !=null){
                                    val singleChat = Chat(otherPhone, otherName, otherAvatar, lastMsg, unseenMsg, chatKey)
                                    //chatList.add(msg)
                                    recyclerChatsListAdapter.addValue(singleChat)
                                    //Log.d("CHATLIST", chatList.size.toString())
                                }
                                //recyclerChatsListAdapter.setData(chatList)
                                //}
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("ChatsListFragment", "Failed to read value. " + error.toException())
                            }

                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ChatListFragment", "Failed to read value. " + error.toException())
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {// Загрузка аватарки текущего пользователя в аппбар
        override fun onDataChange(snapshot: DataSnapshot) {
            val avatarURL = snapshot.child(repository.phone).child("avatar").getValue(String::class.java)
            if (avatarURL != null || avatarURL != "") {
                Glide.with(view).load(avatarURL).into(binding.chatUserImgView)
            }
        }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatListFragment", "Failed to read value. " + error.toException())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}