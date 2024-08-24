package com.example.biologyscheme.localrepository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.biologyscheme.models.ClassData


@Dao
interface ClassDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClassData(value: ClassData)

    @Query("SELECT* FROM class_data_table WHERE className= :className")
    fun getClassData(className: String): List<ClassData>

    @Delete
    fun removeClassData(value: ClassData)

    @Update
    fun updateClassData(value: ClassData)

    @Query("DELETE FROM CLASS_DATA_TABLE")
    fun clearDatabase()
}