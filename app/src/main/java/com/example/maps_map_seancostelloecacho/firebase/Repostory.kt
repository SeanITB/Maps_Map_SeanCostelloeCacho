package com.example.maps_map_seancostelloecacho.firebase

import android.util.Log
import com.example.maps_map_seancostelloecacho.models.MarkerData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Repository {
    private val database = FirebaseFirestore.getInstance()

    val MARKERS_KEY = "markers"
    val NAME_KEY = "name"
    val TYPE_KEY = "type"
    val PHOTOS_KEY = "photos"
    val LATITUDE_KEY = "latitude"
    val LONGITUDE_KEY = "longitude"

    fun addMarker(marker: MarkerData, user:String, uriUrl: String){
        Log.i("addMarker", "addMarker in repository hihih")
        database.collection(MARKERS_KEY)
            .add(
                hashMapOf(
                    "owner" to user,
                    NAME_KEY to marker.name,
                    TYPE_KEY to marker.type,
                    PHOTOS_KEY to uriUrl,
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
                PHOTOS_KEY to editMarker.uriUrl,
                LATITUDE_KEY to editMarker.location.latitude,
                LONGITUDE_KEY to editMarker.location.longitude
            )
        )
        Log.i("nooo", "nooo ${hashMapOf(
            NAME_KEY to editMarker.name,
            TYPE_KEY to editMarker.type,
            PHOTOS_KEY to editMarker.uriUrl,
            LATITUDE_KEY to editMarker.location.latitude,
            LONGITUDE_KEY to editMarker.location.longitude
        )}")
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

}