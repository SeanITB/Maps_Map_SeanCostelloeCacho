package com.example.maps_map_seancostelloecacho.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.maps_map_seancostelloecacho.firebase.Repository
import com.example.maps_map_seancostelloecacho.models.Category
import com.example.maps_map_seancostelloecacho.models.Location
import com.example.maps_map_seancostelloecacho.models.MarkerData
import java.util.EventListener
import java.util.Objects
import java.util.SortedMap

class MarkerViewModel: ViewModel() {

    // firabase values
    val repository = Repository()

    val NAME_KEY = "name"
    val TYPE_KEY = "type"
    val DESCRIPTION_KEY = "description"
    val PHOTOS_KEY = "photos"
    val LATITUDE_KEY = "latitude"
    val LONGITUDE_KEY = "longitude"
    val dataToSave = HashMap<String, Any>()



    // app values
    private val _camaeraPermissionGranted = MutableLiveData(false)
    val camaeraPermissionGranted = _camaeraPermissionGranted

    private val _shouldShowCameraPermissionRationale = MutableLiveData(false)
    val shouldShowCameraPermissionRationale = _shouldShowCameraPermissionRationale

    private val _showCameraPermissionDenied = MutableLiveData(false)
    val showCameraPermissionDenied = _showCameraPermissionDenied

    private val _mapPermissionGranted = MutableLiveData(false)
    val mapPermissionGranted = _mapPermissionGranted

    private val _shouldShowPermissionMapRationale = MutableLiveData(false)
    val shouldShowPermissionMapRationale = _shouldShowPermissionMapRationale

    private val _showMapPermissionDenied = MutableLiveData(false)
    val showMapPermissionDenied = _showMapPermissionDenied

    var actualScreen by mutableStateOf("mapScreen")
        private set

    var isFiltered by mutableStateOf(false)
        private set

    private val _markerList = MutableLiveData<MutableList<MarkerData>>(mutableListOf())
    val markerList = _markerList

    private var _categoryMarkrList = MutableLiveData<List<Category>>(mutableListOf())
    val categoryMarkerList = _categoryMarkrList

    private val _filterMarkerList = MutableLiveData<List<MarkerData>>(listOf())
    val filterMarkerList = _filterMarkerList

    var categoryMap: SortedMap<String, MutableList<MarkerData>>? by mutableStateOf( sortedMapOf())
        private set

    var newMarker by mutableStateOf(MarkerData("", "", "", mutableListOf(), Location(0.0, 0.0)))
        private set

    var name by mutableStateOf("")
        private set

    var typeMarker by mutableStateOf("All markers")
        private set

    var expandedOptions by mutableStateOf(false)
        private set

    var expandedOptionsTopBar by mutableStateOf(false)
        private set

    var description by mutableStateOf("")
        private set

    var photoList by mutableStateOf(mutableListOf<Bitmap>())
        private set

    var latitude by mutableStateOf(0.0)
        private set

    var longitude by mutableStateOf(0.0)
        private set

    var showBottomSheet by mutableStateOf(false)
        private set

    // Firebase Methods

    //toDo: no funciona
    /*
    fun getMarker() {
        repository.getMarkers().addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                    return
                }
                val tempList = mutableListOf<MarkerData>()
                for(dc: DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        val newUser = dc.document.toObject(MarkerData::class.java)
                        tempList.add(newUser)
                    }
                }
                _markerList.value = tempList
            }
        })
    }

     */

    fun addMarker() {
        repository.addMarkerYT(dataToSave)
    }

    /*
    fun onStart() {
        repository.onStart()
    }

     */

    /* // toDo: fata devolver
    fun getMarkers(): MarkerData {
        val makerHasMap = repository.getMarkers()

        return MarkerData(
           "",
            "",
            "",

        )
    }

     */



    // App Methods
    fun setCamaeraPermissionGranted(granted: Boolean) {
        this._camaeraPermissionGranted.value = granted
    }

    fun setShouldShowCameraPermissionRationale(granted: Boolean) {
        this._shouldShowCameraPermissionRationale.value = granted
    }

    fun setShowCameraPermissionDenied(granted: Boolean) {
        this._showCameraPermissionDenied.value = granted
    }

    fun setMapPermissionGranted(granted: Boolean) {
        this._camaeraPermissionGranted.value = granted
    }

    fun setShouldShowMapPermissionRationale(granted: Boolean) {
        this._shouldShowCameraPermissionRationale.value = granted
    }

    fun setShowMapPermissionDenied(granted: Boolean) {
        this._showCameraPermissionDenied.value = granted
    }

    fun changeActualScreen(value: String) {
        this.actualScreen = value
    }

    fun addMarkerToList(marker: MarkerData) {
        this._markerList.value!!.add(marker)
        addMarkerToMap(marker)
        sortMarkerList()
    }

    private fun addMarkerToMap(marker: MarkerData) {
        if (this.categoryMap!!.contains(marker.tipe))
            this.categoryMap!![marker.tipe]!!.add(marker)
        else {
            this.categoryMap!![marker.tipe] = mutableListOf()
            this.categoryMap!![marker.tipe]!!.add(marker)
        }
    }

    private fun sortMarkerList() {
        this._categoryMarkrList.value = this.categoryMap?.map {
            Category(
                name = it.key,
                items = it.value
            )
        }
    }

    fun changeIsFiltred(value: Boolean) {
        this.isFiltered = value
    }
    fun createFilerList() {
        if (this.typeMarker == "All markers")
            this._filterMarkerList.value = this._markerList.value
        else {
            this.categoryMarkerList.value?.forEach {
                if (it.name == this.typeMarker) {
                    this._filterMarkerList.value = it.items
                }
            }
        }
    }

    fun changeExpandedOptions(value: Boolean) {
        this.expandedOptions = value
    }

    fun changeExpandedOptionsTopBar(value: Boolean) {
        this.expandedOptionsTopBar = value
    }

    fun changeNameMarker(value : String) {
        this.name = value
        dataToSave.put(NAME_KEY, this.name)
    }

    fun changeTypeMarker(value : String) {
        this.typeMarker = value
        dataToSave.put(TYPE_KEY, this.typeMarker)
    }

    fun changeDescription(value: String) {
        this.description = value
        dataToSave.put(DESCRIPTION_KEY, this.description)
    }

    fun addPhoto(value: Bitmap) {
        this.photoList.add(value)
        dataToSave.put(PHOTOS_KEY, this.photoList)
    }

    fun changeLatitude(value: Double) {
        this.latitude = value
        dataToSave.put(LATITUDE_KEY, this.latitude)
    }

    fun changeLongitude(value: Double) {
        this.longitude = value
        dataToSave.put(LONGITUDE_KEY, this.longitude)
    }

    fun addNewMarker() { //toDo: rebisar la photo por defectop q muestra
        this.newMarker = MarkerData(
            this.name,
            this.typeMarker,
            if (this.description.equals("")) "There isn't any description." else this.description,
            if (this.photoList.isEmpty()) mutableListOf(Bitmap.createBitmap(25, 25, Bitmap.Config.ARGB_8888)) else this.photoList,
            Location(
                this.latitude,
                this.longitude
            )
        )
        println("new marker: " + newMarker)
    }


    fun proveThatMarkerIsCorrect(): Boolean {
        var isCorrect = false
        if (
            this.name != "" &&
            this.typeMarker != ""
        )
            isCorrect = true
        return isCorrect
    }

    fun changeShowBottomSheet(value : Boolean) {
        this.showBottomSheet = value
    }

    fun restartMarkerAtributes() {
        this.name = ""
        this.typeMarker = "All markers"
        this.description = ""
        this.photoList = mutableListOf()
    }
}