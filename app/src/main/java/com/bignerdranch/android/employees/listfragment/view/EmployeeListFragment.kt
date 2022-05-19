package com.bignerdranch.android.employees.listfragment.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bignerdranch.android.employees.R
import com.bignerdranch.android.employees.databinding.FragmentEmployeeListBinding
import com.bignerdranch.android.employees.listfragment.presenter.EmployeeListFragmentPresenter
import com.bignerdranch.android.employees.utils.Employee
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class EmployeeListFragment : MvpAppCompatFragment(), IEmployeeListFragmentView {
    private var _binding: FragmentEmployeeListBinding? = null //используется view binding для ссылки на layout
    private val binding //This property is only valid between onCreateView and onDestroyView.
        get() = _binding!!

    @InjectPresenter
    lateinit var presenter: EmployeeListFragmentPresenter

    private lateinit var collectionAdapter: CollectionAdapter// When requested, this adapter returns a ElementFragment, representing an object in the collection
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEmployeeListBinding.inflate(inflater, container, false)
        return binding.root//inflater.inflate(R.layout.fragment_employee_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        collectionAdapter = CollectionAdapter(this, )
//        viewPager = binding.viewPager
//        viewPager.adapter = collectionAdapter

//        TabLayoutMediator(binding.tabLayout, viewPager){ tab, position ->
//            tab.text = departments.elementAt(position)//"ELEMENT ${(position + 1)}"
//        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun startDataFetching() {
        Log.d("EmployeeListFragment", "startDataFetching")
    }

    override fun onFailure(errorStrId: Int) {
        Log.e("EmployeeListFragment", "onFailure: ${getString(errorStrId)}")
    }

    override fun onSuccess(data: List<Employee>) {
        val departments = presenter.getDepartments()
        collectionAdapter = CollectionAdapter(this, data, departments)
        viewPager = binding.viewPager
        viewPager.adapter = collectionAdapter

        TabLayoutMediator(binding.tabLayout, viewPager){ tab, position ->
            tab.text = departments.elementAt(position)//"ELEMENT ${(position + 1)}"
        }.attach()
    }
}





/*
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.employee_search, menu)
        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView
        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    Log.d("EmployeeListFragment", "onQueryTextSubmit")
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }
            })
            setOnClickListener{
                Log.d("EmployeeListFragment", "OnClickListener")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_item_search ->{
                Log.d("EmployeeListFragment", "onOptionsItemSelected")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
*/