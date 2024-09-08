package com.example.biologyscheme.viewmodels

import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    private var academicYearIndex: Int? = null
    private var academicYear: String? = null
    private var className: String? = null


    fun setAcademicYearAndIndex(academicYear: String, yearIndex: Int){
        this.academicYear = academicYear
        academicYearIndex = yearIndex
    }

    fun setClassName(className: String){
        this.className = className
    }

    fun getAcademicYearIndex(): Int{
        return academicYearIndex!!
    }

    fun getAcademicYear(): String?{
        return academicYear
    }

    fun getClassName(): String?{
        return className
    }
}