package com.bignerdranch.android.employees.listfragment.view

import android.content.Context
import android.icu.util.LocaleData
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.employees.databinding.FragmentElementBinding
import com.bignerdranch.android.employees.listfragment.model.NextYear
import com.bignerdranch.android.employees.listfragment.model.Person
import com.bignerdranch.android.employees.listfragment.model.RecyclerDataDiffUtill
import com.bignerdranch.android.employees.listfragment.model.RecyclerDataModel
import com.bignerdranch.android.employees.listfragment.presenter.EmployeeListFragmentPresenter
import com.bignerdranch.android.employees.utils.Employee
import com.bignerdranch.android.employees.utils.LocalDateTypeAdapter
import dagger.android.support.AndroidSupportInjection
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

class CollectionElementFragment : MvpAppCompatFragment(), IEmployeeListFragmentView{
    private var _binding: FragmentElementBinding? = null //используется view binding для ссылки на layout
    private val binding //This property is only valid between onCreateView and onDestroyView.
        get() = _binding!!

    lateinit var employeesByDepartment: List<Employee>

    @Inject
    lateinit var presenterProvider: Provider<EmployeeListFragmentPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    private val recyclerAdapter = CollectionElementRecyclerAdapter()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    // Instances of this class are fragments representing a single object in our collection.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentElementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_ELEMENT) }?.apply {
            employeesByDepartment = getParcelableArrayList<Employee>(ARG_ELEMENT)?.toList() ?: listOf()
        }

        recyclerAdapter.setData(convertToRecyler(employeesByDepartment))//еще в презентере отсортировал по алф
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun update(searchStr: String, sortType: String) {
        if(searchStr.isEmpty()){// если строка поиска пуста то отбражаем всех
            showNotFound(false)
            updateRecyclerData(convertToRecyler(employeesByDepartment, sortType))
        }else{
            val filteredBySearch = mutableListOf<Employee>()
            for(employee in employeesByDepartment){
                val employeeName = (employee.firstName + " " + employee.lastName + " " + employee.userTag).lowercase()
                if(employeeName.contains(searchStr.lowercase()))
                    filteredBySearch.add(employee)
            }
            if (filteredBySearch.isEmpty()){//если ничего не нашли то отображаем NotFoundView
                showNotFound(true)
                updateRecyclerData(listOf())//а если нашли то отображаем найденных
            }
            else{
                showNotFound(false)
                updateRecyclerData(convertToRecyler(filteredBySearch, sortType))//а если нашли то отображаем найденных
            }
        }
    }

    fun updateRecyclerData(newData: List<RecyclerDataModel>){
        val diffUtill = RecyclerDataDiffUtill(recyclerAdapter.getData(), newData)
        val diffResult = DiffUtil.calculateDiff(diffUtill, true)
        recyclerAdapter.setData(newData)
        //binding.recyclerView.post{
            diffResult.dispatchUpdatesTo(recyclerAdapter)
        //}
    }

    fun convertToRecyler(employees: List<Employee>, sortBy: String = ""): List<RecyclerDataModel> {
        val recyclerData: MutableList<RecyclerDataModel> = mutableListOf()
        if(sortBy == "birth"){
            val now = LocalDate.now().dayOfYear
            var labelAdded = false
            for (employee in employees.sortedByDescending { it.birthday.dayOfYear }){// sortedWith(compareBy<Employee> { it.birthday.month }.thenBy { it.birthday.day })){
                //Log.d("SORT: ", "${employee.birthday.month}.${employee.birthday}")
                if(!labelAdded && employee.birthday.dayOfYear < now){
                    recyclerData.add(NextYear( LocalDate.now().year + 1))
                    labelAdded = true
                }
                recyclerData.add(Person(employee))
            }
        }else{
            for(employee in employees){
                recyclerData.add(Person(employee))
            }
        }
        return recyclerData
    }

    fun showNotFound(boolean: Boolean){
        if(boolean){
            binding.recyclerView.visibility = View.INVISIBLE
            binding.notFoundView.visibility = View.VISIBLE
        }
        else{
            binding.recyclerView.visibility = View.VISIBLE
            binding.notFoundView.visibility = View.INVISIBLE
        }
    }
}
