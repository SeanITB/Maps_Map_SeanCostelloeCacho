package com.example.maps_map_seancostelloecacho.firebase

import com.example.maps_map_seancostelloecacho.models.MarkerData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Repository {
    private val database = FirebaseFirestore.getInstance()

    val MARKERS_KEY = "markers"
    val NAME_KEY = "name"
    val TYPE_KEY = "type"
    val DESCRIPTION_KEY = "description"
    val PHOTOS_KEY = "photos"
    val LATITUDE_KEY = "latitude"
    val LONGITUDE_KEY = "longitude"

    fun addMarker(marker: MarkerData){
        database.collection(MARKERS_KEY)
            .add(
                hashMapOf(
                    NAME_KEY to marker.name,
                    TYPE_KEY to marker.type,
                    DESCRIPTION_KEY to marker.description,
                    PHOTOS_KEY to marker.photo,
                    LATITUDE_KEY to marker.location.latitude,
                    LONGITUDE_KEY to marker.location.longitude
                )
            )
    }

    fun editMarker(editMarker: MarkerData) {
        database.collection(MARKERS_KEY).document(editMarker.id!!).set(
            hashMapOf(
                NAME_KEY to editMarker.name,
                TYPE_KEY to editMarker.type,
                DESCRIPTION_KEY to editMarker.description,
                //PHOTOS_KEY to editMarker.photos,
                LATITUDE_KEY to editMarker.location.latitude,
                LONGITUDE_KEY to editMarker.location.longitude
            )
        )
    }

    fun deleteMarker(markerId: String) {
        database.collection(MARKERS_KEY).document(markerId).delete()
    }

    fun getMarkers(): CollectionReference {
        return database.collection(MARKERS_KEY)
    }

    fun getMarker(markerId: String): DocumentReference {
        return database.collection(MARKERS_KEY).document(markerId)
    }

    /*
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

     */

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








    /* //toDo: el get del tutorial no funciona
    @Override
    fun onStart() {
        val name: String
        val type: String
        val description: String
        val photoList: MutableList<Bitmap>
        val latitude: Long
        val longitude: Long
        database.addSnapshotsInSyncListener(this, EventListener<DocumentSnapshot>() {
            @Override
            fun onEvent(documentSnapshot: DocumentSnapshot, e: FirebaseFirestoreException) {
                if (documentSnapshot.exists()){
                    name = documentSnapshot.getString(NAME_KEY)
                    type = documentSnapshot.getString(TYPE_KEY)
                    description = documentSnapshot.getString(DESCRIPTION_KEY)
                    //photoList = documentSnapshot.getBitmap(PHOTOS_KEY)
                    latitude = documentSnapshot.getLong(LATITUDE_KEY)
                    longitude = documentSnapshot.getLong(LONGITUDE_KEY)
                } else if (e != null){
                    Log.w(TAG, "Got an exception!", e)
                }
            }
        })
    }

     */

}