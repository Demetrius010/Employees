package com.bignerdranch.android.employees.talkFragment.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.employees.talkFragment.model.Message
import com.bignerdranch.android.employees.databinding.FragmentTalkBinding
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
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TalkFragment : Fragment() {
    private var _binding: FragmentTalkBinding? =
        null //используется view binding для ссылки на layout
    private val binding //This property is only valid between onCreateView and onDestroyView.
        get() = _binding!!

    @Inject
    lateinit var repository: Repository

    lateinit var dbRef: DatabaseReference

    private val recyclerTalkAdapter = TalkRecyclerAdapter()
    private val messageList: MutableList<Message> = mutableListOf()

    lateinit var savedUserMobile: String
    private var loadingFirstTime = true

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        dbRef = Firebase.database("https://employees-3f7e9-default-rtdb.europe-west1.firebasedatabase.app/").getReference()// Write a message to the database
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        savedUserMobile = repository.phone//UserPreferences.getPhoneNum(requireContext())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTalkBinding.inflate(inflater, container, false)
        binding.talkBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerTalkAdapter.setData(messageList)
        binding.msgRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerTalkAdapter
        }

        arguments?.let {
            val args = TalkFragmentArgs.fromBundle(it)
            val chatData = args.chatData
            var chatKey = chatData.chatKey

            binding.run {
                talkNameTV.text = chatData.name
                Glide.with(requireContext())// загружаем обложку фильма из кеша
                    .load(chatData.avatarUrl)
                    .into(talkUserImgView)
            }


            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (chatKey.isBlank()) {
                        chatKey = "1" //TODO: GENERATE CHAT KEY. by default is 1
                        if (snapshot.hasChild("chat")) {
                            chatKey = (snapshot.child("chat").childrenCount.toInt() + 1).toString()
                        }
                    }
                    if(snapshot.hasChild("chat")){
                        if(snapshot.child("chat").child(chatKey).hasChild("messages")) {
                            messageList.clear()
                            for (messagesSnaphot in snapshot.child("chat").child(chatKey).children){
                                if(messagesSnaphot.hasChild("msg") && messagesSnaphot.hasChild("phone")){
                                    val msgTimeStamp = messagesSnaphot.key
                                    val getPhone = messagesSnaphot.child("phone").getValue(String::class.java)
                                    val getMsg = messagesSnaphot.child("msg").getValue(String::class.java)

                                    val timeStamp = msgTimeStamp?.let { it1 -> Timestamp(it1.toLong()) }
                                    val date = timeStamp?.let { it1 -> Date(it1.time) }
                                    val simplDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                    val simplTimeFormat = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
                                    val message = Message(getPhone!!, "getname", getMsg!!, simplDateFormat.format(date),simplTimeFormat.format(date))
                                    messageList.add(message)
                                    if (msgTimeStamp != null) {
                                        if (loadingFirstTime || msgTimeStamp.toLong()>UserPreferences.getLastMsg(requireContext(), chatKey).toLong()){
                                            loadingFirstTime = false
                                            UserPreferences.setLastMsg(requireContext(), msgTimeStamp, chatKey)
                                            recyclerTalkAdapter.setData(messageList)
                                            binding.msgRecyclerView.scrollToPosition(messageList.size - 1)
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TalkFragment", "Failed to read value. " + error.toException())
                }

            })


            binding.sendBtn.setOnClickListener {
                val input = binding.inputMsgET.text.toString()
                val timeStamp = System.currentTimeMillis().toString().substring(0, 10)
                dbRef.child("chat").child(chatKey).child("user_1")
                    .setValue(repository.phone)//user phone
                dbRef.child("chat").child(chatKey).child("user_2").setValue(chatData.mobile)
                dbRef.child("chat").child(chatKey).child("messages").child(timeStamp)
                    .child("msg").setValue(input)
                dbRef.child("chat").child(chatKey).child("messages").child(timeStamp)
                    .child("phone").setValue(repository.phone)
                binding.inputMsgET.setText("")
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}