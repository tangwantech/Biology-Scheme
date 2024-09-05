package com.example.biologyscheme.models

data class TopicData(val topicName: String,
                     val moduleName: String,
                     val familyOfSituation: String,
                     val numberOfPeriods: Int,
                     var isTaught: Boolean,
                     var startDate: String,
                     var endDate: String,
                     val abilities: DropDownItemData,
                     val subTopics: DropDownItemData,
                     val exampleOfSituation: String,
                     val categoryOfAction: String,
                     val examplesOfActions: DropDownItemData,
                     val lifeSkills: DropDownItemData,
                     val otherResources: DropDownItemData
)
