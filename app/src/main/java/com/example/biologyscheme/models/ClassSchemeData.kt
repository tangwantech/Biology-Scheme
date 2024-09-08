package com.example.biologyscheme.models

data class ClassSchemeData(
    var academicYear: String = "",
    var className: String = "",
    var topics: List<TopicData>? = null)
