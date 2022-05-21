package com.bignerdranch.android.employees.listfragment.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.employees.databinding.FragmentElementBinding
import com.bignerdranch.android.employees.databinding.FragmentEmployeeListBinding
import com.bignerdranch.android.employees.listfragment.model.Person
import com.bignerdranch.android.employees.listfragment.model.RecyclerDataDiffUtill
import com.bignerdranch.android.employees.listfragment.model.RecyclerDataModel
import com.bignerdranch.android.employees.listfragment.presenter.EmployeeListFragmentPresenter
import com.bignerdranch.android.employees.utils.Employee
import dagger.android.support.AndroidSupportInjection
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import moxy.presenter.ProvidePresenterTag
import javax.inject.Inject
import javax.inject.Provider

class CollectionElementFragment(employees: List<Employee>) : MvpAppCompatFragment(), IEmployeeListFragmentView{
    private val recyclerData: MutableList<RecyclerDataModel> = mutableListOf()
    init {
        for(employee in employees){
            recyclerData.add(Person(employee))
        }
    }

    private var _binding: FragmentElementBinding? = null //используется view binding для ссылки на layout
    private val binding //This property is only valid between onCreateView and onDestroyView.
        get() = _binding!!

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
                binding.myTextView.text = getInt(ARG_ELEMENT).toString()
            }
        recyclerAdapter.setData(recyclerData)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun update(filter: String) {
        if(filter.isEmpty()){
            showNotFound(false)
            updateRecyclerData(recyclerData)
        }else{
            val filteredData = mutableListOf<RecyclerDataModel>()
            for(oldData in recyclerData){
                if((oldData as Person).employee.firstName.lowercase().contains(filter.lowercase()))
                    filteredData.add(oldData)
            }
            if (filteredData.isEmpty()){
                showNotFound(true)
            }
            else{
                showNotFound(false)
                updateRecyclerData(filteredData)
            }
        }
    }

    fun updateRecyclerData(newData: List<RecyclerDataModel>){
        val diffUtill = RecyclerDataDiffUtill(recyclerAdapter.getData(), newData)
        val diffResult = DiffUtil.calculateDiff(diffUtill)
        recyclerAdapter.setData(newData)
        binding.recyclerView.post{
            diffResult.dispatchUpdatesTo(recyclerAdapter)
        }
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
