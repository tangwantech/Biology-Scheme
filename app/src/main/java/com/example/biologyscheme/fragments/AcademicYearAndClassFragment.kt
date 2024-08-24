package com.example.biologyscheme.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.biologyscheme.Constants
import com.example.biologyscheme.R
import com.example.biologyscheme.databinding.FragmentAcademicYearAndClassBinding
import com.example.biologyscheme.viewmodels.MainActivityViewModel

class AcademicYearAndClassFragment : Fragment() {

    private lateinit var activityViewModel: MainActivityViewModel
    private lateinit var binding:FragmentAcademicYearAndClassBinding
    private lateinit var onButtonClickListener: OnButtonClickListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnButtonClickListener){
            onButtonClickListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAcademicYearAndClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupActionBar()
//        setupViewModel()
//        setupViewListeners()
//        changeBtnNextState()

    }

    override fun onResume() {
        super.onResume()
        setupActionBar()
        setupViewModel()
        setupViewListeners()
        changeBtnNextState()
        setupAutocompleteViews()
    }

    private fun setupViewModel(){
        activityViewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
    }

    private fun setupViewListeners(){
        binding.btnNext.setOnClickListener {
            onButtonClickListener.onNextButtonClicked()
        }

        binding.btnExit.setOnClickListener {
            onButtonClickListener.onExitButtonClicked()
        }

        binding.autoCompleteAcademicYear.doOnTextChanged { text, start, before, count ->
            changeBtnNextState()
            val academicYear = text.toString()
            if (academicYear.isNotEmpty()){
                activityViewModel.setAcademicYearAndIndex(academicYear, Constants.ACADEMIC_YEARS.indexOf(academicYear))
            }

        }

        binding.autoCompleteClassName.doOnTextChanged { text, start, before, count ->
            changeBtnNextState()
            val className = text.toString()
            if (className.isNotEmpty()){
                activityViewModel.setClassName(className)
            }

        }


    }

    private fun changeBtnNextState(){
        binding.btnNext.isEnabled = binding.autoCompleteAcademicYear.text.toString().isNotEmpty() && binding.autoCompleteClassName.text.toString().isNotEmpty()
    }

    private fun setupAutocompleteViews(){
        val adapterAcademicYear = ArrayAdapter<String>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Constants.ACADEMIC_YEARS)
        binding.autoCompleteAcademicYear.setAdapter(adapterAcademicYear)

        val adapterClassName = ArrayAdapter<String>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Constants.CLASS_NAMES)
        binding.autoCompleteClassName.setAdapter(adapterClassName)
    }

    private fun setupActionBar(){
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolBar.toolBar)
        activity.supportActionBar?.title = requireContext().getString(R.string.app_name)
    }


    interface OnButtonClickListener{
        fun onNextButtonClicked()
        fun onExitButtonClicked()
    }

    companion object {

        const val FRAGMENT_NAME = "AcademicYearAndClassFragment"
        fun newInstance() =
            AcademicYearAndClassFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}