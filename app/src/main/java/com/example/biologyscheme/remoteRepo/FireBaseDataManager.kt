package com.example.biologyscheme.remoteRepo

import com.example.biologyscheme.models.ClassSchemeData
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.values
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FireBaseDataManager {
    private val firebaseDatabase: FirebaseDatabase = Firebase.database
    private val dbRef = firebaseDatabase.reference


    fun loadScheme(databaseRef: String, className:String, onLoadSchemeListener: OnLoadSchemeListener) {
        val dbSchemeRef = dbRef.child(databaseRef)

        dbSchemeRef.child(className).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val classSchemeData = snapshot.getValue(ClassSchemeData::class.java)
//                println("Scheme from firebase: $classSchemeData")
                if(classSchemeData != null){
                    onLoadSchemeListener.onSchemeLoaded(classSchemeData!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("error...${error.message}")
            }

        })

    }

    interface OnLoadSchemeListener{
        fun onSchemeLoaded(result: ClassSchemeData)
        fun onError()
    }
}