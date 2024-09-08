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
import com.example.biologyscheme.models.HoursCoverageStatistics
import com.example.biologyscheme.models.ProgressionSheetData
import com.example.biologyscheme.models.TopicData
import com.example.biologyscheme.models.WorkCoverageStatistics
import com.example.biologyscheme.remoteRepo.FireBaseDataManager

class ProgressionSheetViewModel : ViewModel() {
    private var academicYear: String? = null
    private var className: String? = null
    private val progressionSheet = ArrayList<ProgressionSheetData>()
    private val _progressionSheetDataAvailable = MutableLiveData<Boolean>()
    val progressionSheetDataAvailable: LiveData<Boolean> = _progressionSheetDataAvailable
    private lateinit var roomDatabaseManager: RoomDatabaseManager
    private  var classData: ClassData? = null
    private lateinit var firebaseDatabaseManager: FireBaseDataManager

    fun initRoomDatabaseManager(context: Context){
        roomDatabaseManager = RoomDatabaseManager(context)
        roomDatabaseManager.clearRoomDatabase()
        loadClassDataFromRoom(context)

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
                setProgressionSheetData()
            }

            override fun onError() {
                _progressionSheetDataAvailable.value = false
            }

        })
    }

    private fun loadClassDataFromRoom(context: Context){
        roomDatabaseManager.loadClassDataFromRoom(className!!, academicYear!!, object : RoomDatabaseManager.OnRoomDatabaseQueryListener{
            override fun onClassDataAvailableFromRoom(result: ClassData) {
//                println("Class data available from room")
                classData = result
                setProgressionSheetData()
            }

            override fun onClassDataUnAvailableFromRoom() {
//                println("Class data unavailable from room")
               loadClassDataFromAssert(context, className!!, academicYear!!)
//                loadSchemeFromFirebase()
            }

        })
    }

    fun loadClassDataFromAssert(context: Context, className: String, academicYear: String){
//        println("Loading class data from assert")
        val fileName = Constants.CLASS_SCHEME_FILE_NAMES[className]
        SchemeFileReader.readFromAssert(context, fileName!!, object: SchemeFileReader.OnResult{
            override fun result(result: ClassSchemeData) {
//                println("Class data loaded from assert")
                val customId = className + academicYear
                classData = ClassData(0, customId, className, academicYear, result)
//                println("Class data: $classData")
                setProgressionSheetData()
            }

            override fun error(error: String?) {
//                println("Error loading class data from assert")
                _progressionSheetDataAvailable.value = false
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

    private fun setProgressionSheetData(){
        progressionSheet.clear()
        classData?.classSchemeData?.topics?.forEach {
            progressionSheet.add(ProgressionSheetData(it.topicName, it.startDate, it.endDate, it.isTaught))
        }
        _progressionSheetDataAvailable.value = progressionSheet.isNotEmpty()


    }

    fun getProgressionSheet():ArrayList<ProgressionSheetData>{
        return progressionSheet
    }

    fun updateTopicTaughtState(itemPosition: Int, state: Boolean){
        progressionSheet[itemPosition].isTaught = state
        classData?.classSchemeData?.topics!![itemPosition].isTaught = state
    }

    fun updateTopicDate(itemPosition: Int, dateEditButtonIndex: Int, date: String){
        if(dateEditButtonIndex == 0){
            progressionSheet[itemPosition].startDate = date
            classData?.classSchemeData?.topics!![itemPosition].startDate = date
        }else{
            progressionSheet[itemPosition].endDate = date
            classData?.classSchemeData?.topics!![itemPosition].endDate = date
        }
    }

    fun getTopicDate(itemPosition: Int, dateEditButtonIndex: Int): String{
        return if(dateEditButtonIndex == 0){
            progressionSheet[itemPosition].startDate
        }else{
            progressionSheet[itemPosition].endDate
        }
    }

    fun getClassSchemeData(): ClassSchemeData{
        return classData?.classSchemeData!!
    }

    fun getTopicDetails(itemPosition: Int): TopicData {
        return classData?.classSchemeData?.topics!![itemPosition]
    }

    fun getWorkCoverageStatistics(): WorkCoverageStatistics{
        val numberOfTopicsProgrammed = classData?.classSchemeData?.topics?.size
        val numberOfTopicsDone = classData?.classSchemeData?.topics?.count { it.isTaught }
        val percentageCovered = ((numberOfTopicsDone?.toDouble()!!/ numberOfTopicsProgrammed?.toDouble()!!) * 100)
        val strPercentageDone = "%.2f".format(percentageCovered)
        val workCoverageStatistics = WorkCoverageStatistics(numberOfTopicsProgrammed.toString(), numberOfTopicsDone.toString(), strPercentageDone)
//        println(workCoverageStatistics)
        return workCoverageStatistics
    }

    fun getHoursCoverageStatistics(): HoursCoverageStatistics{
        val numberOfHoursProgramed = classData?.classSchemeData?.topics?.sumOf { topicData -> topicData.numberOfPeriods }
        val numberOfHoursDone = classData?.classSchemeData?.topics?.filter { topicData -> topicData.isTaught  }?.sumOf {topicData ->
            topicData.numberOfPeriods
        }
        val percentageDone = ((numberOfHoursDone?.toDouble()!! / numberOfHoursProgramed?.toDouble()!!) * 100)
        val strPercentageDone = "%.2f".format(percentageDone)

        val hoursCoverageStatistics = HoursCoverageStatistics(numberOfHoursProgramed.toString(), numberOfHoursDone.toString(), strPercentageDone)
//        println(hoursCoverageStatistics)
        return hoursCoverageStatistics
    }

    fun updateClassDataInRoom() {
        classData?.let {
            roomDatabaseManager.updateClassDataInRoom(it)
        }
    }



}