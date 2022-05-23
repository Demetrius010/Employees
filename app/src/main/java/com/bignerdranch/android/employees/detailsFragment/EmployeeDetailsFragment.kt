package com.bignerdranch.android.employees.detailsFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.employees.R
import com.bignerdranch.android.employees.databinding.FragmentEmployeeDetailsBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

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
                birthDateTV.text = employeeData.birthday.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))//SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(employeeData.birthday)
                //val diff = Period.between(Date(), employeeData.birthday)
                //val timeUtil = TimeUnit.MILLISECONDS.toDays(Date().time - employeeData.birthday.time)/365
                val diff = ChronoUnit.YEARS.between(employeeData.birthday, LocalDate.now())
                yearsTV.text = diff.toString() + " years"//(Calendar.getInstance().get(Calendar.YEAR) - employeeData.birthday.get(Calendar.YEAR)).toString() + " years" //(TimeUnit.MILLISECONDS.toDays(Date().time - employeeData.birthday.time) / 365).toString()  + " years"
                phoneTV.text = employeeData.phone
                Glide.with(requireContext())// загружаем обложку фильма из кеша
                    .load(employeeData.avatarUrl)
                    .placeholder(R.drawable.ic_image_search)
                    .error(R.drawable.ic_image_not_supported)
                    .circleCrop()
                    .onlyRetrieveFromCache(true)
                    .into(personImgView)
            }

            val bottomSheet = view.findViewById<LinearLayout>(R.id.callBottomSheet)
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
/*            bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    Log.d("EmployeeListFragment", "onStateChanged: newState: $newState")
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })*/
            binding.phoneTV.setOnClickListener{
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            bottomSheet.findViewById<Button>(R.id.makeCallBtn).setOnClickListener{
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.setData(Uri.parse("tel:" + employeeData.phone))
                startActivity(callIntent)
            }
            bottomSheet.findViewById<Button>(R.id.makeSMSBtn).setOnClickListener{
                val smsIntent = Intent(Intent.ACTION_SENDTO)
                smsIntent.setData(Uri.parse("smsto:" + employeeData.phone))
                startActivity(smsIntent)
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