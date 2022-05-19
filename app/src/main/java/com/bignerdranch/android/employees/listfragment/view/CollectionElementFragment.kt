package com.bignerdranch.android.employees.listfragment.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.employees.R
import com.bignerdranch.android.employees.databinding.FragmentElementBinding
import com.bignerdranch.android.employees.databinding.FragmentEmployeeListBinding
import com.bignerdranch.android.employees.listfragment.model.Person
import com.bignerdranch.android.employees.listfragment.model.RecyclerDataDiffUtill
import com.bignerdranch.android.employees.listfragment.model.RecyclerDataModel
import com.bignerdranch.android.employees.listfragment.presenter.EmployeeListFragmentPresenter
import com.bignerdranch.android.employees.utils.Employee
import com.google.android.material.bottomsheet.BottomSheetBehavior
import moxy.presenter.InjectPresenter

class CollectionElementFragment(val employeeData: List<Employee>) : Fragment(){//val recyclerData: List<RecyclerDataModel>
    val recyclerData: MutableList<RecyclerDataModel> = mutableListOf()
    init {
        for(empl in employeeData){
            recyclerData.add(Person(empl))
        }
    }



    private var _binding: FragmentElementBinding? = null //используется view binding для ссылки на layout
    private val binding //This property is only valid between onCreateView and onDestroyView.
        get() = _binding!!

    private val recyclerAdapter = CollectionElementRecyclerAdapter()

    // Instances of this class are fragments representing a single object in our collection.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentElementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_ELEMENT) }?.apply {
                binding.myTextView.text = getInt(ARG_ELEMENT).toString()
            }

        val bottomSheet = view.findViewById<LinearLayout>(R.id.bottomSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        //bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.d("EmployeeListFragment", "onStateChanged")
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("EmployeeListFragment", "onSlide")
            }
        })

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

    fun updateRecyclerData(newData: List<RecyclerDataModel>){
        val diffUtill = RecyclerDataDiffUtill(recyclerAdapter.getData(), newData)
        val diffResult = DiffUtil.calculateDiff(diffUtill)
        recyclerAdapter.setData(newData)
        binding.recyclerView.post{
            diffResult.dispatchUpdatesTo(recyclerAdapter)
        }
    }
}
