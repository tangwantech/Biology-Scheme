package com.example.biologyscheme.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity(tableName = "class_data_table")
data class ClassData(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "className")
    val className: String,

    @ColumnInfo(name = "allAcademicYearsSchemes")
    var schemesForAcademicYears: List<ClassSchemeData>){

}

class SchemesForAcademicYearsConverter{

    @TypeConverter
    fun fromListToJson(value: List<ClassSchemeData>) = Gson().toJson(value)

    @TypeConverter
    fun fromJsonToList(value: String) = Gson().fromJson(value, Array<ClassSchemeData>::class.java).toList()
}




