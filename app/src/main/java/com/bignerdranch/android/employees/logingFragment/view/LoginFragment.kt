package com.bignerdranch.android.employees.logingFragment.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.employees.databinding.FragmentLoginBinding
import com.bignerdranch.android.employees.utils.Repository
import com.bignerdranch.android.employees.utils.UserPreferences
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null //используется view binding для ссылки на layout
    private val binding //This property is only valid between onCreateView and onDestroyView.
        get() = _binding!!

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var dbRef: DatabaseReference

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        if (!UserPreferences.getPhoneNum(context).isBlank()){// если уже зарегистрировались то пропускаем регистрацию и переходим на следующий фрагмент
            val savedData = UserPreferences.getData(requireContext())
            repository.saveCurUser(savedData[0], savedData[1], savedData[2])
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToEmployeeListFragment())
        }
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.registerBtn.setOnClickListener{
            val phoneNum = binding.etPhoneNum.text.toString()
            val email = binding.etEmail.text.toString()
            val name = binding.etName.text.toString()

            if(name.isBlank() || email.isBlank() || phoneNum.isBlank()){
                Toast.makeText(context, "All fields required!", Toast.LENGTH_SHORT).show()
            }
            else {
                //TODO: progress bar show
                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {//addListenerForSingleValueEvent() executes onDataChange method immediately and after executing that method once, it stops listening to the reference location it is attached to.
                    override fun onDataChange(snapshot: DataSnapshot) {//Слушатель запускается один раз для начального состояния данных
                        //TODO: progress bar hide
                        //Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue()
                        if (snapshot.hasChild(phoneNum)){// {8900={name="name", avatar="https://...", email="@mail"},   8999={...}}
                            Toast.makeText(context, "Mobile exist! $phoneNum", Toast.LENGTH_LONG).show()
                        }
                        else{
                            dbRef.child(phoneNum).child("email").setValue(email)
                            dbRef.child(phoneNum).child("name").setValue(name)
                            dbRef.child(phoneNum).child("avatar").setValue("")
                            UserPreferences.setData(requireContext(), phoneNum, name, email)
                            repository.saveCurUser(name, phoneNum, email)//TODO: СКОРЕЕ ВСЕГО ЭТО НЕ НУЖНО
                            Toast.makeText(context, "Registred!", Toast.LENGTH_LONG).show()
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToEmployeeListFragment())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        //TODO: progress bar hide
                        Log.e("LoginFragment", "Failed to read value. " + error.toException())
                    }
                })
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}