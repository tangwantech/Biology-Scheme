package com.example.biologyscheme.localrepository

import android.content.Context
import com.example.biologyscheme.models.ClassData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomDatabaseManager(private val context: Context) {
    private val roomDB = AppDatabase.getDatabase(context)

    fun loadClassDataFromRoom(className: String, academicYear: String, onRoomDatabaseQueryListener: OnRoomDatabaseQueryListener){
        val customId = className + academicYear
        CoroutineScope(Dispatchers.IO).launch {
            val temp = roomDB.classDataDao().getClassData(customId)

            withContext(Dispatchers.Main){

                if (temp.isNotEmpty()){
                    onRoomDatabaseQueryListener.onClassDataAvailableFromRoom(temp[0])

                }else{
                    onRoomDatabaseQueryListener.onClassDataUnAvailableFromRoom()
                }
            }
        }
    }


    fun insertClassDataToRoom(value: ClassData){
        CoroutineScope(Dispatchers.IO).launch {
            roomDB.classDataDao().insertClassData(value)
        }
    }

    fun updateClassDataInRoom(value: ClassData){
        CoroutineScope(Dispatchers.IO).launch {
            roomDB.classDataDao().updateClassData(value)
        }
    }

    fun removeClassDataFromRoom(value: ClassData){
        CoroutineScope(Dispatchers.IO).launch {
            roomDB.classDataDao().removeClassData(value)
        }
    }

    fun clearRoomDatabase(){
        CoroutineScope(Dispatchers.IO).launch {
            roomDB.classDataDao().clearDatabase()
        }
    }

    interface OnRoomDatabaseQueryListener{
        fun onClassDataAvailableFromRoom(result: ClassData)
        fun onClassDataUnAvailableFromRoom()
    }
}