package com.bignerdranch.android.employees.listfragment.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.employees.R
import com.bignerdranch.android.employees.listfragment.model.NextYear
import com.bignerdranch.android.employees.listfragment.model.Person
import com.bignerdranch.android.employees.listfragment.model.RecyclerDataModel
import com.bumptech.glide.Glide
import java.util.*

class CollectionElementRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val mDataList: MutableList<RecyclerDataModel> = mutableListOf()//LinkedList()

    fun getData() = mDataList
    fun setData(newData: List<RecyclerDataModel>){
        mDataList.clear()
        mDataList.addAll(newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            TYPE_PERSON -> PersonViewHolder(inflater.inflate(R.layout.item_person, parent, false))
            TYPE_NEXT_YEAR -> NextYearViewHolder(inflater.inflate(R.layout.item_next_year, parent, false))
            else -> throw IllegalStateException("Invalid type")
        }
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PersonViewHolder -> holder.bind(mDataList[position] as Person)
            is NextYearViewHolder -> holder.bind(mDataList[position] as NextYear)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (mDataList[position]){
            is Person -> TYPE_PERSON
            is NextYear -> TYPE_NEXT_YEAR
        }
    }

        companion object{
            private const val TYPE_PERSON = 0
            private const val TYPE_NEXT_YEAR = 1
        }
}

class PersonViewHolder(val view: View): RecyclerView.ViewHolder(view){
    private val personImage: ImageView = view.findViewById(R.id.personAvatarIV)
    private val personName: TextView = view.findViewById(R.id.personNameTV)
    private val personTag: TextView = view.findViewById(R.id.personTagTV)
    private val personDepartment: TextView = view.findViewById(R.id.personDepartmentTV)

    fun bind(item: Person){
        item.employee.apply {
            personName.text = (firstName + " " + lastName)
            personTag.text = userTag.lowercase()
            personDepartment.text = department
            Glide.with(view)
                .load(avatarUrl)
                .placeholder(R.drawable.ic_image_search)
                .error(R.drawable.ic_image_not_supported)
                .circleCrop()
                .onlyRetrieveFromCache(true)
                .into(personImage)
        }
        view.setOnClickListener{view ->
            val action = EmployeeListFragmentDirections.actionEmployeeListFragmentToEmployeeDetailsFragment(item.employee)
            Navigation.findNavController(view).navigate(action)
        }
    }
}

class NextYearViewHolder(val view: View): RecyclerView.ViewHolder(view){
    private val nextYearTextView: TextView = view.findViewById(R.id.nextYearTV)

    fun bind(item: NextYear){
        nextYearTextView.text = item.year.toString()
    }
}