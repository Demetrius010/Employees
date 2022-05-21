package com.bignerdranch.android.employees.listfragment.view

import android.content.Context
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.viewpager2.widget.ViewPager2
import com.bignerdranch.android.employees.R
import com.bignerdranch.android.employees.databinding.FragmentEmployeeListBinding
import com.bignerdranch.android.employees.listfragment.presenter.EmployeeListFragmentPresenter
import com.bignerdranch.android.employees.utils.Employee
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import moxy.presenter.ProvidePresenterTag
import javax.inject.Inject

import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import javax.inject.Provider

class EmployeeListFragment: MvpAppCompatFragment(), IEmployeeListFragmentView {
    private var _binding: FragmentEmployeeListBinding? = null //используется view binding для ссылки на layout
    private val binding //This property is only valid between onCreateView and onDestroyView.
        get() = _binding!!
    private lateinit var collectionAdapter: CollectionAdapter// When requested, this adapter returns a ElementFragment, representing an object in the collection
    private lateinit var viewPager: ViewPager2

    @Inject // КАК Я ПОНИМАЮ КОД НИЖЕ ИНЖЕКТИТ ПРЕЗЕНТЕР И ПРИВЯЗЫВАЕТ К НЕМУ ДАННУЮ ВЬЮ, а иначе пришлось бы самостоятельно аттачить и детачить
    lateinit var presenterProvider: Provider<EmployeeListFragmentPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEmployeeListBinding.inflate(inflater, container, false)
        return binding.root//inflater.inflate(R.layout.fragment_employee_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String): Boolean {
                presenter.setSearchStr(p0)
                return false
            }

            override fun onQueryTextChange(p0: String): Boolean {
                if(p0.isEmpty())
                    presenter.setSearchStr("")
                return false
            }
        })

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

    override fun onSuccess(employees: List<Employee>) {
        val departments = presenter.getDepartments()
        collectionAdapter = CollectionAdapter(this, employees, departments)
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



/* failed inject presenter attempts

//    @Inject
//    lateinit var daggerPresenter: EmployeeListFragmentPresenter
//    private val presenter by moxyPresenter { daggerPresenter }

//    @ProvidePresenter
//    fun providePresenter(): EmployeeListFragmentPresenter {
//        return presenter
//    }


//    @Inject
//    lateinit var daggerPresenter: Lazy<EmployeeListFragmentPresenter>

//    @InjectPresenter
//    lateinit var presenter: EmployeeListFragmentPresenter
//
//    @ProvidePresenter
//    fun providePresenter(): EmployeeListFragmentPresenter = daggerPresenter.value
//    @Inject
//    lateinit var daggerPresenter: Lazy<EmployeeListFragmentPresenter>
//    private val presenter by moxyPresenter { daggerPresenter.value }


//    @InjectPresenter
//    lateinit var presenter: EmployeeListFragmentPresenter
//
//    @Inject
//    lateinit var presenterProvider: Provider<EmployeeListFragmentPresenter>
//
//    @ProvidePresenter
//    fun providePresenter(): EmployeeListFragmentPresenter {
//        return presenterProvider.get();
//    }
*/