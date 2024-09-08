package com.example.biologyscheme.localrepository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.biologyscheme.models.ClassData
import com.example.biologyscheme.models.ClassSchemeTypeConverter


@Database (entities = [ClassData::class], version = 1)
@TypeConverters (ClassSchemeTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun classDataDao(): ClassDataDao

    companion object{

        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "biology_scheme"
                ).build()
                INSTANCE = instance
                return instance
            }



        }
    }

}