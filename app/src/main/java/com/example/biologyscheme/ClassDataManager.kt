package com.example.biologyscheme

import android.content.Context
import com.example.biologyscheme.localrepository.RoomDatabaseManager
import com.example.biologyscheme.models.ClassData
import com.example.biologyscheme.models.ClassSchemeData
import com.example.biologyscheme.remoteRepo.FireBaseDataManager

class ClassDataManager(private val context: Context) {
    private var classData: ClassData? = null
    private var roomDatabaseManager = RoomDatabaseManager(context)
    private var fireBaseDataManager = FireBaseDataManager()

    fun loadClassSchemeFromDatabase(className: String, academicYear: String, onClassDataQueryListener: ClassDataQueryListener){
        roomDatabaseManager.loadClassDataFromRoom(className, academicYear, object: RoomDatabaseManager.OnRoomDatabaseQueryListener{
            override fun onClassDataAvailableFromRoom(result: ClassData) {
                classData = result
                onClassDataQueryListener.onClassDataAvailableFromRoom()

            }

            override fun onClassDataUnAvailableFromRoom() {
                onClassDataQueryListener.onClassDataUnAvailableFromRoom()
            }

        })
    }

//    fun loadSchemeFromFireBase(className: String, academicYear: String, onReadSchemeFromAssertListener: OnReadSchemeFromAssertListener){
//        fireBaseDataManager.loadScheme(className, object : FireBaseDataManager.OnLoadSchemeListener{
//            override fun onSchemeLoaded(result: ClassSchemeData) {
//                println("Scheme data available")
//                result.academicYear = academicYear
//
//                onReadSchemeFromAssertListener.onClassSchemeAvailable()
//                insertClassDataInRoom()
//            }
//
//            override fun onError() {
//                println("Scheme for $className is currently unavailable")
//                onReadSchemeFromAssertListener.onClassSchemeUnavailable()
//            }
//
//        })
//    }



    fun removeClassDataFromRoomDatabase(){
        roomDatabaseManager.removeClassDataFromRoom(classData!!)
    }

    fun getSchemeForAcademicYear(academicYear: String): ClassSchemeData?{
        return classData?.classSchemeData

    }

    fun loadSchemeFromAssert(className: String, fileName: String, academicYear: String, onReadSchemeFromAssertListener: OnReadSchemeFromAssertListener){
        SchemeFileReader.readFromAssert(context, fileName, object: SchemeFileReader.OnResult{
            override fun result(result: ClassSchemeData) {

                val customId = className + academicYear
                classData = ClassData(0, customId, className, academicYear, result)
                onReadSchemeFromAssertListener.onClassSchemeAvailable()
                insertClassDataInRoom()
            }

            override fun error(error: String?) {
                onReadSchemeFromAssertListener.onClassSchemeUnavailable()
            }
        })
    }


    fun updateSchemeData(classSchemeData: ClassSchemeData){
        classData?.classSchemeData = classSchemeData
        updateClassDataInRoom()
    }

    fun removeSchemeDataAt(academicYearIndex: Int, classSchemeData: ClassSchemeData){

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

    fun resetClassData() {
        classData = null
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