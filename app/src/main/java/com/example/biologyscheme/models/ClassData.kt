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

    @ColumnInfo(name = "customId")
    val customId: String,

    @ColumnInfo(name = "className")
    val className: String,

    @ColumnInfo(name = "academicYear")
    val academicYear: String,

    @ColumnInfo(name = "schemeData")
    var classSchemeData: ClassSchemeData
//    @ColumnInfo(name = "allAcademicYearsSchemes")
//    var schemesForAcademicYears: List<ClassSchemeData>
)

class ClassSchemeTypeConverter{


    @TypeConverter
    fun fromListToJson(value: ClassSchemeData) = Gson().toJson(value)

    @TypeConverter
    fun fromJsonToList(value: String) = Gson().fromJson(value, ClassSchemeData::class.java)
}





