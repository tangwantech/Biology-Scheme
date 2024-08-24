package com.example.biologyscheme.models

data class ClassSchemeData(
    var academicYear: String,
    val className: String,
    val topics: List<TopicData>)
