package com.example.maps_map_seancostelloecacho.firebase

import android.util.Log
import com.example.maps_map_seancostelloecacho.models.MarkerData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import org.checkerframework.checker.nullness.qual.NonNull

class Repository {
    private val database = FirebaseFirestore.getInstance()

    val TAG = "Marker"

    fun addMarker(marker: MarkerData){
        database.collection("markers")
            .add(
                hashMapOf(
                    "mame" to marker.name,
                    "tipe" to marker.tipe,
                    "description" to marker.description,
                    "photos" to marker.photos,
                    "location" to marker.location
                )
            )
    }


    fun addMarkerYT(dataToSave: HashMap<String, Any>){
        println("add marker to firebase")
        database.document("markers/markers").set(dataToSave).addOnCompleteListener( OnCompleteListener() {
            println("add marker to firebase INNN")
            @Override
            fun onComplete(task: @NonNull Task<Void>) {
                if (task.isSuccessful) {
                    Log.d(TAG, "Hooray! Document was saved!")
                } else {
                    Log.d(TAG, "Oh no!", task.exception)
                }
            }

        })

        /*
        database.document("markers/markers").set(dataToSave).addOnSuccessListener { OnSuccessListener<Void>() {
            @Override
            fun onSuccess(aVoid: Void) {
                Log.d(TAG, "Document has been saved!")
            }
        } }.addOnFailureListener( OnFailureListener() {
            @Override
            fun onFailure(e: @NonNull Exception) {
                Log.w(TAG, "Document was no saved!", e)
            }
        })


         */
    }



    fun getMarkers(): CollectionReference {
        return database.collection("markers")
    }
}