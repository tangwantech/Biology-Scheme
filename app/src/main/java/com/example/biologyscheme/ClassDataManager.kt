package com.example.biologyscheme

import android.content.Context
import com.example.biologyscheme.localrepository.RoomDatabaseManager
import com.example.biologyscheme.models.ClassData
import com.example.biologyscheme.models.ClassSchemeData

class ClassDataManager(private val context: Context) {
    private var classData: ClassData? = null
    private var roomDatabaseManager = RoomDatabaseManager(context)
    private val schemesForAllYears = mutableMapOf<String, ClassSchemeData>()

    fun loadClassSchemeFromDatabase(className: String, academicYear: String, onClassDataQueryListener: ClassDataQueryListener){
        roomDatabaseManager.loadClassDataFromRoom(className, academicYear, object: RoomDatabaseManager.OnRoomDatabaseQueryListener{
            override fun onClassDataAvailableFromRoom(result: ClassData) {
                classData = result
                setSchemesForAllYears()
                onClassDataQueryListener.onClassDataAvailableFromRoom()

            }

            override fun onClassDataUnAvailableFromRoom() {
                onClassDataQueryListener.onClassDataUnAvailableFromRoom()
            }

        })
    }

    fun insertClassDataToRoomDatabase(){
        roomDatabaseManager.insertClassDataToRoom(classData!!)
    }


    fun removeClassDataFromRoomDatabase(){
        roomDatabaseManager.removeClassDataFromRoom(classData!!)
    }

    fun getSchemeForAcademicYear(academicYear: String): ClassSchemeData?{
//        val temp = classData?.schemesForAcademicYears?.find { it.academicYear == academicYear}
//        println(classData?.schemesForAcademicYears)
        return schemesForAllYears[academicYear]

    }

    fun loadSchemeFromAssert(className: String, fileName: String, academicYear: String, onReadSchemeFromAssertListener: OnReadSchemeFromAssertListener){
        SchemeFileReader.readFromAssert(context, fileName, object: SchemeFileReader.OnResult{
            override fun result(result: ClassSchemeData) {

                result.academicYear = academicYear
                val temp = ArrayList<ClassSchemeData>()
                if (classData?.schemesForAcademicYears != null){
                    temp.addAll(classData?.schemesForAcademicYears!!)
                    temp.add(result)
                    classData?.schemesForAcademicYears = temp
                }else{
                    temp.add(result)
                    classData = ClassData(0, className, temp)
                }
//                println("class data $classData")
                setSchemesForAllYears()
                onReadSchemeFromAssertListener.onClassSchemeAvailable()
                insertClassDataInRoom()
            }

            override fun error(error: String?) {
                onReadSchemeFromAssertListener.onClassSchemeUnavailable()
            }
        })
    }

    fun updateSchemeDataAt(academicYearIndex: Int, classSchemeData: ClassSchemeData){
        val temp = ArrayList<ClassSchemeData>()
         temp.addAll(classData?.schemesForAcademicYears!!)
        temp[academicYearIndex] = classSchemeData
        classData?.schemesForAcademicYears = temp
        setSchemesForAllYears()
        updateClassDataInRoom()
    }

    fun removeSchemeDataAt(academicYearIndex: Int, classSchemeData: ClassSchemeData){
        if (academicYearIndex in classData?.schemesForAcademicYears!!.indices &&
            classData?.schemesForAcademicYears!![academicYearIndex].academicYear == classSchemeData.academicYear){
            val temp = ArrayList<ClassSchemeData>()
            temp.addAll( classData?.schemesForAcademicYears!!)
            temp.remove(classSchemeData)
            classData?.schemesForAcademicYears = temp
            updateClassDataInRoom()


        }
    }

    private fun insertClassDataInRoom(){
        roomDatabaseManager.insertClassDataToRoom(classData!!)
    }

    private fun updateClassDataInRoom(){
        roomDatabaseManager.updateClassDataInRoom(classData!!)
    }

    fun clearDatabase(){
        roomDatabaseManager.clearRoomDatabase()
    }

    fun setSchemesForAllYears(){
        schemesForAllYears.clear()
        classData!!.schemesForAcademicYears.forEach {
            classSchemeData ->
            val year = classSchemeData.academicYear
            schemesForAllYears[year] = classSchemeData
        }
    }
    interface ClassDataQueryListener{
        fun onClassDataAvailableFromRoom()
        fun onClassDataUnAvailableFromRoom()
    }

    interface OnReadSchemeFromAssertListener{
        fun onClassSchemeAvailable()
        fun onClassSchemeUnavailable()
    }

}