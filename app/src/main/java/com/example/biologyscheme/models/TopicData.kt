package com.example.biologyscheme.models

data class TopicData(val topicName: String,
                     val moduleName: String,
                     val familyOfSituation: String,
                     val numberOfPeriods: Int,
                     var isTaught: Boolean,
                     val startDate: String,
                     val endDate: String,
                     val objectives: TitleAndListData,
                     val subTopics: TitleAndListData,
                     val exampleOfSituation: String,
                     val categoryOfAction: String,
                     val examplesOfActions: TitleAndListData,
                     val lifeSkills: TitleAndListData,
                     val otherResources: TitleAndListData
)
