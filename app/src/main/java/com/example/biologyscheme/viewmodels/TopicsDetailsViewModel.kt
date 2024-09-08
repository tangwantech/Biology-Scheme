package com.example.biologyscheme.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.biologyscheme.Constants
import com.example.biologyscheme.SchemeFileReader
import com.example.biologyscheme.localrepository.RoomDatabaseManager
import com.example.biologyscheme.models.ClassData
import com.example.biologyscheme.models.ClassSchemeData
import com.example.biologyscheme.models.ProgressionSheetData
import com.example.biologyscheme.models.TopicData
import com.example.biologyscheme.remoteRepo.FireBaseDataManager

class TopicsDetailsViewModel: ViewModel() {
    private var academicYear: String? = null
    private var className: String? = null
    private val progressionSheet = ArrayList<ProgressionSheetData>()
    private val _topicsDetailsDataAvailable = MutableLiveData<Boolean>()
    val topicsDetailsDataAvailable: LiveData<Boolean> = _topicsDetailsDataAvailable
    private lateinit var roomDatabaseManager: RoomDatabaseManager
    private  var classData: ClassData? = null
    private lateinit var firebaseDatabaseManager: FireBaseDataManager

    fun initRoomDatabaseManager(context: Context){
        roomDatabaseManager = RoomDatabaseManager(context)
        roomDatabaseManager.clearRoomDatabase()
        loadClassDataFromAssert(context, className!!, academicYear!!)

    }

    private fun initFireBaseDataManager(){
        firebaseDatabaseManager = FireBaseDataManager()
//        loadSchemeFromFirebase()
    }

    private fun loadSchemeFromFirebase(){
        firebaseDatabaseManager.loadScheme("Schemes", className!!, object: FireBaseDataManager.OnLoadSchemeListener{
            override fun onSchemeLoaded(result: ClassSchemeData) {
                val customId = className + academicYear
                classData = ClassData(0, customId, className!!, academicYear!!, result)
                _topicsDetailsDataAvailable.value = true
            }

            override fun onError() {
                _topicsDetailsDataAvailable.value = false
            }

        })
    }

    private fun loadClassDataFromAssert(context: Context, className: String, academicYear: String){
//        println("Loading class data from assert")
        val fileName = Constants.CLASS_SCHEME_FILE_NAMES[className]
        SchemeFileReader.readFromAssert(context, fileName!!, object: SchemeFileReader.OnResult{
            override fun result(result: ClassSchemeData) {
//                println("Class data loaded from assert")
                val customId = className + academicYear
                classData = ClassData(0, customId, className, academicYear, result)
//                println("Class data: $classData")
                _topicsDetailsDataAvailable.value = true
            }

            override fun error(error: String?) {
//                println("Error loading class data from assert")
                _topicsDetailsDataAvailable.value = false
            }
        })
    }
    fun setAcademicYear(academicYear: String,){
        this.academicYear = academicYear

    }

    fun setClassName(className: String){
        this.className = className
    }

    fun getClassName(): String{
        return className!!
    }

    fun getAcademicYear(): String{
        return academicYear!!
    }

    fun getTopicDetails(itemPosition: Int): TopicData {
        return classData?.classSchemeData?.topics!![itemPosition]
    }
}