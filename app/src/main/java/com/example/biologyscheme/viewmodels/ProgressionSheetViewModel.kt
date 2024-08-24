package com.example.biologyscheme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.biologyscheme.models.ClassSchemeData
import com.example.biologyscheme.models.HoursCoverageStatistics
import com.example.biologyscheme.models.ProgressionSheetData
import com.example.biologyscheme.models.TopicData
import com.example.biologyscheme.models.WorkCoverageStatistics

class ProgressionSheetViewModel : ViewModel() {
    private var academicYearIndex: Int? = null
    private var academicYear: String? = null
    private var className: String? = null
    private val progressionSheet = ArrayList<ProgressionSheetData>()

    private lateinit var classSchemeData: ClassSchemeData

    private val _progressionSheetDataAvailable = MutableLiveData<Boolean>()
    val progressionSheetDataAvailable: LiveData<Boolean> = _progressionSheetDataAvailable


    fun setAcademicYearAndIndex(academicYear: String, yearIndex: Int){
        this.academicYear = academicYear
        academicYearIndex = yearIndex
    }

    fun setClassName(className: String){
        this.className = className
    }

    fun setClassSchemeData(classSchemeData: ClassSchemeData){
        this.classSchemeData = classSchemeData
        setProgressionSheetData()
    }

    private fun setProgressionSheetData(){
        progressionSheet.clear()
        this.classSchemeData.topics.forEach {
            progressionSheet.add(ProgressionSheetData(it.topicName, it.startDate, it.endDate, it.isTaught))
        }
        _progressionSheetDataAvailable.value = progressionSheet.isNotEmpty()


    }

    fun getProgressionSheet():ArrayList<ProgressionSheetData>{
        return progressionSheet
    }

    fun updateTopicTaughtState(itemPosition: Int, state: Boolean){
        progressionSheet[itemPosition].isTaught = state
        classSchemeData.topics[itemPosition].isTaught = state
    }

    fun getClassSchemeData(): ClassSchemeData{
        return classSchemeData
    }

    fun getTopicDetails(itemPosition: Int): TopicData {
        return classSchemeData.topics[itemPosition]
    }

    fun getWorkCoverageStatistics(): WorkCoverageStatistics{
        val numberOfTopicsProgrammed = classSchemeData.topics.size
        val numberOfTopicsDone = classSchemeData.topics.count { it.isTaught }
        val percentageCovered = ((numberOfTopicsDone.toDouble()/ numberOfTopicsProgrammed.toDouble()) * 100).toString().format(2)
        val workCoverageStatistics = WorkCoverageStatistics(numberOfTopicsProgrammed.toString(), numberOfTopicsDone.toString(), percentageCovered)
        println(workCoverageStatistics)
        return workCoverageStatistics
    }

    fun getHoursCoverageStatistics(): HoursCoverageStatistics{
        val numberOfHoursProgramed = classSchemeData.topics.sumOf { topicData -> topicData.numberOfPeriods }
        val numberOfHoursDone = classSchemeData.topics.filter { topicData -> topicData.isTaught  }.sumOf {topicData ->
            topicData.numberOfPeriods
        }
        val percentageDone = ((numberOfHoursDone.toDouble() / numberOfHoursProgramed.toDouble()) * 100).toString().format()
        val hoursCoverageStatistics = HoursCoverageStatistics(numberOfHoursProgramed.toString(), numberOfHoursDone.toString(), percentageDone)
        println(hoursCoverageStatistics)
        return hoursCoverageStatistics
    }


}