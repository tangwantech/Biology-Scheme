package com.example.biologyscheme

import android.content.Context
import com.example.biologyscheme.models.ClassSchemeData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.charset.Charset

class SchemeFileReader {
    companion object{
        private fun getJsonFromAssets(context: Context, fileName: String): String? {
            var json: String? = null
            val charset: Charset = Charsets.UTF_8

            try {
                val jsonFile = context.assets.open(fileName)
                val size = jsonFile.available()
                val buffer = ByteArray(size)

                jsonFile.read(buffer)
                jsonFile.close()
                json = String(buffer, charset)

            } catch (e: IOException) {

                return null
            }
            return json
        }
        fun readFromAssert(context: Context, fileName:String, onResult: OnResult){

            CoroutineScope(Dispatchers.IO).launch {
//                val t = ClassSubjectGeneralScheme::class.java
                val jsonString = getJsonFromAssets(context, fileName)
                if (jsonString != null){
                    val temp = Gson().fromJson(jsonString, ClassSchemeData::class.java)
                    withContext(Dispatchers.Main){
//                        println(temp)
                        onResult.result(temp)}
                }else{
                    withContext(Dispatchers.Main) {
                        onResult.error("Error reading from assert")
                    }
                }
            }
        }

    }
    interface OnResult{
        fun result(result: ClassSchemeData)
        fun error(error: String?)
    }
}