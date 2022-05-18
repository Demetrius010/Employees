package com.bignerdranch.android.employees.listfragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bignerdranch.android.employees.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val ARG_ELEMENT = "element"

class EmployeeListFragment : Fragment() {

    private lateinit var collectionAdapter: CollectionAdapter// When requested, this adapter returns a ElementFragment, representing an object in the collection
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)//зарегистрировать фрагмент для получения обратных вызовов меню
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        collectionAdapter = CollectionAdapter(this)
        viewPager = view.findViewById(R.id.viewPager)
        viewPager.adapter = collectionAdapter

        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            tab.text = "ELEMENT ${(position + 1)}"
        }.attach()

//        val myToolBar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.myToolbar) // для тулбара добавленного непосредственно в layout фрагмента
//        myToolBar.inflateMenu(R.menu.employee_search)
    }
}

class CollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 100
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = ElementFragment()// Return a NEW fragment instance in createFragment(int)
        fragment.arguments = Bundle().apply {
            putInt(ARG_ELEMENT, position+1)// Our object is just an integer :-P
        }
        return fragment
    }
}

class ElementFragment : Fragment(){
// Instances of this class are fragments representing a single object in our collection.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_element, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_ELEMENT) }?.apply {
            val textView: TextView = view.findViewById(R.id.myTextView)
            textView.text = getInt(ARG_ELEMENT).toString()
        }
    }
}