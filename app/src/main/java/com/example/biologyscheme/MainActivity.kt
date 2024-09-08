package com.example.biologyscheme

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.biologyscheme.databinding.ActivityMainBinding
import com.example.biologyscheme.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity(){
//    private lateinit var viewModel: MainActivityViewModel

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.app_name)
        setupViewModel()

    }

    private fun gotoProgressionSheetActivity(){
        val intent = ProgressionSheetActivity.getIntent(this)
        intent.apply {
            putExtra(Constants.ACADEMIC_YEAR, viewModel.getAcademicYear())
            putExtra(Constants.CLASS_NAME, viewModel.getClassName())
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        setupViewListeners()
        changeBtnNextState()
        setupAutocompleteViews()

    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
    }

    private fun setupViewListeners(){
        binding.btnNext.setOnClickListener {
            gotoProgressionSheetActivity()

        }

        binding.btnExit.setOnClickListener {
            finish()
        }

        binding.autoCompleteAcademicYear.doOnTextChanged { text, start, before, count ->
            changeBtnNextState()
            val academicYear = text.toString()
            if (academicYear.isNotEmpty()){
                viewModel.setAcademicYearAndIndex(academicYear, Constants.ACADEMIC_YEARS.indexOf(academicYear))
            }

        }

        binding.autoCompleteClassName.doOnTextChanged { text, start, before, count ->
            changeBtnNextState()
            val className = text.toString()
            if (className.isNotEmpty()){
                viewModel.setClassName(className)
            }

        }


    }

    private fun changeBtnNextState(){
        binding.btnNext.isEnabled = binding.autoCompleteAcademicYear.text.toString().isNotEmpty() && binding.autoCompleteClassName.text.toString().isNotEmpty()
    }

    private fun setupAutocompleteViews(){

        val adapterAcademicYear = ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Constants.ACADEMIC_YEARS)
        viewModel.getAcademicYear()?.let {
            binding.autoCompleteAcademicYear.setText(it)
        }
        binding.autoCompleteAcademicYear.setAdapter(adapterAcademicYear)

        viewModel.getClassName()?.let{
            binding.autoCompleteClassName.setText(it)
        }
        val adapterClassName = ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Constants.CLASS_NAMES)
        binding.autoCompleteClassName.setAdapter(adapterClassName)
    }


}