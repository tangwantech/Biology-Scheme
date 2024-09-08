package com.example.biologyscheme.models

data class TopicData(
     var topicName: String = "",
     var moduleName: String = "",
     var familyOfSituation: String = "",
     var numberOfPeriods: Int = 0,
     var isTaught: Boolean = false,
     var startDate: String = "",
     var endDate: String = "",
     var abilities: DropDownItemData? = null,
     var subTopics: DropDownItemData? = null,
     var exampleOfSituation: String = "",
     var categoryOfAction: String = "",
     var examplesOfActions: DropDownItemData? = null,
     var lifeSkills: DropDownItemData? = null,
     var otherResources: DropDownItemData? = null
)
