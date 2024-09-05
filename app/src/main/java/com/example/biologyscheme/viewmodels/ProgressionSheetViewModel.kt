package com.example.biologyscheme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.biologyscheme.models.ClassSchemeData
import com.example.biologyscheme.models.HoursCoverageStatistics
import com.example.biologyscheme.models.ProgressionSheetData
import com.example.biologyscheme.models.TopicData
import com.example.biologyscheme.models.WorkCoverageStatistics
import kotlin.math.round

class ProgressionSheetViewModel : ViewModel() {
    private var academicYearIndex: Int? = null
    private var academicYear: String? = null
    private var className: String? = null
    private val progressionSheet = ArrayList<ProgressionSheetData>()

    private var classSchemeData: ClassSchemeData? = null

    private val _progressionSheetDataAvailable = MutableLiveData<Boolean>()
    val progressionSheetDataAvailable: LiveData<Boolean> = _progressionSheetDataAvailable


    fun setAcademicYearAndIndex(academicYear: String, yearIndex: Int){
        this.academicYear = academicYear
        academicYearIndex = yearIndex
    }

    fun setClassName(className: String){
        this.className = className
    }

    fun setClassSchemeData(classSchemeData: ClassSchemeData?){
        this.classSchemeData = classSchemeData
        setProgressionSheetData()
    }

    private fun setProgressionSheetData(){
        progressionSheet.clear()
        this.classSchemeData?.topics?.forEach {
            progressionSheet.add(ProgressionSheetData(it.topicName, it.startDate, it.endDate, it.isTaught))
        }
        _progressionSheetDataAvailable.value = progressionSheet.isNotEmpty()


    }

    fun getProgressionSheet():ArrayList<ProgressionSheetData>{
        return progressionSheet
    }

    fun updateTopicTaughtState(itemPosition: Int, state: Boolean){
        progressionSheet[itemPosition].isTaught = state
        classSchemeData?.topics!![itemPosition].isTaught = state
    }

    fun updateTopicDate(itemPosition: Int, dateEditButtonIndex: Int, date: String){
        if(dateEditButtonIndex == 0){
            progressionSheet[itemPosition].startDate = date
            classSchemeData?.topics!![itemPosition].startDate = date
        }else{
            progressionSheet[itemPosition].endDate = date
            classSchemeData?.topics!![itemPosition].endDate = date
        }
    }

    fun getTopicDate(itemPosition: Int, dateEditButtonIndex: Int): String{
        return if(dateEditButtonIndex == 0){
            progressionSheet[itemPosition].startDate
        }else{
            progressionSheet[itemPosition].endDate
        }
    }

    fun getClassSchemeData(): ClassSchemeData?{
        return classSchemeData
    }

    fun getTopicDetails(itemPosition: Int): TopicData {
        return classSchemeData?.topics!![itemPosition]
    }

    fun getWorkCoverageStatistics(): WorkCoverageStatistics{
        val numberOfTopicsProgrammed = classSchemeData?.topics?.size
        val numberOfTopicsDone = classSchemeData?.topics?.count { it.isTaught }
        val percentageCovered = ((numberOfTopicsDone?.toDouble()!!/ numberOfTopicsProgrammed?.toDouble()!!) * 100)
        val strPercentageDone = "%.2f".format(percentageCovered)
        val workCoverageStatistics = WorkCoverageStatistics(numberOfTopicsProgrammed.toString(), numberOfTopicsDone.toString(), strPercentageDone)
//        println(workCoverageStatistics)
        return workCoverageStatistics
    }

    fun getHoursCoverageStatistics(): HoursCoverageStatistics{
        val numberOfHoursProgramed = classSchemeData?.topics?.sumOf { topicData -> topicData.numberOfPeriods }
        val numberOfHoursDone = classSchemeData?.topics?.filter { topicData -> topicData.isTaught  }?.sumOf {topicData ->
            topicData.numberOfPeriods
        }
        val percentageDone = ((numberOfHoursDone?.toDouble()!! / numberOfHoursProgramed?.toDouble()!!) * 100)
        val strPercentageDone = "%.2f".format(percentageDone)

        val hoursCoverageStatistics = HoursCoverageStatistics(numberOfHoursProgramed.toString(), numberOfHoursDone.toString(), strPercentageDone)
//        println(hoursCoverageStatistics)
        return hoursCoverageStatistics
    }


}