package com.example.biologyscheme

class Constants {
    companion object{
        const val NUMBER_OF_WEEKS = 33
        val CLASS_NAMES = arrayOf<String>("Form 1", "Form 2", "Form 3", "Form 4", "Form 5", "Lower sixth Science", "Upper sixth science")
        private val FILE_NAMES = arrayOf<String>("schemeForm1.json", "schemeForm2.json")

        val CLASS_SCHEME_FILE_NAMES = hashMapOf<String, String>(
            CLASS_NAMES[0] to FILE_NAMES[0],
            CLASS_NAMES[1] to FILE_NAMES[1]

        )

        val ACADEMIC_YEARS = arrayOf<String>("2024-2025", "2025-2026")
    }
}