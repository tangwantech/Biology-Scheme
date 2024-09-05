package com.example.biologyscheme.remoteRepo

import com.example.biologyscheme.SchemeFileReader
import com.example.biologyscheme.models.ClassSchemeData
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FireBaseDataManager {
    private val firebaseDatabase: FirebaseDatabase = Firebase.database
    private val dbRef = firebaseDatabase.getReference("BiologySchemes")


    fun loadScheme(className:String, onLoadSchemeListener: OnLoadSchemeListener) {

        CoroutineScope(Dispatchers.IO).launch {
            var scheme: ClassSchemeData? = null
            dbRef.child(className).get().addOnSuccessListener {
                println(it.value)
//                scheme = Gson().fromJson(it.value.toString(), ClassSchemeData::class.java)
//                println(scheme)
            }
            withContext(Dispatchers.Main){
                if (scheme != null) {

                    onLoadSchemeListener.onSchemeLoaded(scheme!!)
                } else {
                    onLoadSchemeListener.onError()
                }
            }
        }

    }

    interface OnLoadSchemeListener{
        fun onSchemeLoaded(result: ClassSchemeData)
        fun onError()
    }
}