package com.example.biologyscheme

class Constants {
    companion object{
        const val NUMBER_OF_WEEKS = 33
        val CLASS_NAMES = arrayOf<String>("Form 1", "Form 2", "Form 3", "Form 4", "Form 5", "Lower sixth Science", "Upper sixth science")
        private val FILE_NAMES = arrayOf<String>(
            "schemeForm1.json", "schemeForm2.json",
            "schemeForm3.json", "schemeForm4.json",
            "schemeForm5.json", "schemeLowerSixth.json", "schemeUpperSixth.json")

        val CLASS_SCHEME_FILE_NAMES = hashMapOf<String, String>(
            CLASS_NAMES[0] to FILE_NAMES[0],
            CLASS_NAMES[1] to FILE_NAMES[1],
            CLASS_NAMES[2] to FILE_NAMES[2],
            CLASS_NAMES[3] to FILE_NAMES[3],
            CLASS_NAMES[4] to FILE_NAMES[4],
            CLASS_NAMES[5] to FILE_NAMES[5],
            CLASS_NAMES[6] to FILE_NAMES[6],

        )

        val ACADEMIC_YEARS = arrayOf<String>("2024-2025", "2025-2026")
        const val ACADEMIC_YEAR = "ACADEMIC YEAR"
        const val CLASS_NAME = "CLASS NAME"
        const val ITEM_POSITION = "ITEM_POSITION"
        const val DATE_FORMAT = "d/M/yyyy"
    }
}