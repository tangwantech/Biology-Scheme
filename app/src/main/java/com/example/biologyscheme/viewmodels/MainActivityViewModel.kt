package com.example.biologyscheme.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.biologyscheme.ClassDataManager
import com.example.biologyscheme.models.ClassSchemeData

class MainActivityViewModel: ViewModel() {
//    private var classSchemeData: ClassSchemeData? = null
    private var academicYearIndex: Int? = null
    private var academicYear: String? = null
    private var className: String? = null

    private lateinit var classDataManager: ClassDataManager

    private val _classSchemeAvailable = MutableLiveData<ClassSchemeData?>()
    val classSchemeAvailable: LiveData<ClassSchemeData?> = _classSchemeAvailable

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

    fun getAcademicYear(): String{
        return academicYear!!
    }

    fun getClassName(): String{
        return className!!
    }

    fun initClassDataManager(context: Context){
        classDataManager = ClassDataManager(context)
//        clearDatabase()
    }

    fun readClassDataFromRoom(className: String, fileName: String){
        classDataManager.loadClassSchemeFromDatabase(className, academicYear!!, object:ClassDataManager.ClassDataQueryListener{
            override fun onClassDataAvailableFromRoom() {
                val classSchemeData = classDataManager.getSchemeForAcademicYear(academicYear!!)
                if (classSchemeData != null){
                    _classSchemeAvailable.value = classSchemeData
                }else{
                    loadSchemeFromAssert(className, fileName)
                }

            }

            override fun onClassDataUnAvailableFromRoom() {
                loadSchemeFromAssert(className, fileName)
            }

        })
    }

    private fun loadSchemeFromAssert(className: String, fileName: String){
        classDataManager.loadSchemeFromAssert(className, fileName, academicYear!!, object: ClassDataManager.OnReadSchemeFromAssertListener{
            override fun onClassSchemeAvailable() {
                val classSchemeData = classDataManager.getSchemeForAcademicYear(academicYear!!)
                _classSchemeAvailable.value = classSchemeData
            }

            override fun onClassSchemeUnavailable() {
                _classSchemeAvailable.value = null
            }

        })
    }

    fun updateSchemeDataForAcademicYear(schemeData: ClassSchemeData){
        classDataManager.updateSchemeDataAt(academicYearIndex!!, schemeData)
    }

    fun removeSchemeData(schemeData: ClassSchemeData){
    }

    fun clearDatabase(){
        classDataManager.clearDatabase()
    }

}