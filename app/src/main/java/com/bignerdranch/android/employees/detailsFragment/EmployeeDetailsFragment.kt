package com.bignerdranch.android.employees.detailsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.employees.R
import com.bignerdranch.android.employees.databinding.FragmentEmployeeDetailsBinding
import com.bignerdranch.android.employees.databinding.FragmentEmployeeListBinding
import com.bumptech.glide.Glide

class EmployeeDetailsFragment : Fragment() {
    private var _binding: FragmentEmployeeDetailsBinding? = null
    private val binding// This property is only valid between onCreateView and onDestroyView.
    get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEmployeeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            val args = EmployeeDetailsFragmentArgs.fromBundle(it)
            val employeeData = args.employeeData
            binding.run{
                nameTV.text = employeeData.firstName + " " + employeeData.lastName
                tagTV.text = employeeData.userTag
                departmentTV.text = employeeData.department
                birthDateTV.text = employeeData.birthday
                phoneTV.text = employeeData.phone
                Glide.with(requireContext())// загружаем обложку фильма из кеша
                    .load(employeeData.avatarUrl)
                    .placeholder(R.drawable.ic_image_search)
                    .error(R.drawable.ic_image_not_supported)
                    .circleCrop()
                    .onlyRetrieveFromCache(true)
                    .into(personImgView)
            }
        }
        binding.backBtn.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}