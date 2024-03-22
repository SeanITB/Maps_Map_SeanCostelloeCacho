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
import com.google.firebase.firestore.DocumentChange
import java.util.SortedMap

class MarkerViewModel : ViewModel() {

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

    private val _markerList = MutableLiveData(emptyList<MarkerData>())
    val markerList = _markerList

    private var _categoryMarkrList = MutableLiveData<List<Category>>(mutableListOf())
    val categoryMarkerList = _categoryMarkrList

    private val _filterMarkerList = MutableLiveData<List<MarkerData>>(listOf())
    val filterMarkerList = _filterMarkerList

    var categoryMap: SortedMap<String, MutableList<MarkerData>>? by mutableStateOf(sortedMapOf())
        private set

    private val _actualMarker = MutableLiveData<MarkerData>(MarkerData("", "", "", "", mutableListOf(), Location(0.0, 0.0)))
    val actualMarker = _actualMarker

    private val _idMarker = MutableLiveData<String>("")
    val idMarker = _idMarker

    private val _nameMarker = MutableLiveData<String>("")
    val nameMarker = _nameMarker

    private val _typeMarker = MutableLiveData<String>("")
    val typeMarker = _typeMarker

    private val _descriptionMarker = MutableLiveData<String>("")
    val descriptionMarker = _descriptionMarker

    private val _photosMarker = MutableLiveData<MutableList<Bitmap>>(mutableListOf())
    val photosMarker = _photosMarker

    private val _latitudeMarker = MutableLiveData<Double>(0.0)
    val latitudeMarker = _latitudeMarker

    private val _longitudeMarker = MutableLiveData<Double>(0.0)
    val longitudeMarker = _longitudeMarker

    /*
    var newMarker by mutableStateOf(MarkerData("", "", "", "", mutableListOf(), Location(0.0, 0.0)))
        private set

    var name by mutableStateOf("")
        private set

    var typeMarker by mutableStateOf("All markers")
        private set

    var description by mutableStateOf("")
        private set

    var photoList by mutableStateOf(mutableListOf<Bitmap>())
        private set

    var latitude by mutableStateOf(0.0)
        private set

    var longitude by mutableStateOf(0.0)
        private set

     */
    var expandedOptions by mutableStateOf(false)
        private set

    var expandedOptionsTopBar by mutableStateOf(false)
        private set
    var showBottomSheet by mutableStateOf(false)
        private set

    // Firebase Methods

    fun addMarker() {
        instanceActualMarker()
        repository.addMarker(this.actualMarker.value!!)
    }

    fun instanceActualMarker() {
        this.actualMarker.value = MarkerData(
            id = this.idMarker.value,
            name = this.nameMarker.value!!,
            type = this.typeMarker.value!!,
            description = if (this.descriptionMarker.value.equals("")) "There isn't any description." else this.descriptionMarker.value!!,
            photos = if (this.photosMarker.value!!.isEmpty()) mutableListOf(
                Bitmap.createBitmap(
                    25,
                    25,
                    Bitmap.Config.ARGB_8888
                )
            ) else this.photosMarker.value!!,
            location = Location(
                this.latitudeMarker.value!!,
                this.longitudeMarker.value!!
            )
        )
    }

    fun getMarkers() {
        repository.getMarkers().addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore error!", error.message.toString())
                return@addSnapshotListener
            }
            val tempList = mutableListOf<MarkerData>()
            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    val newMarker = dc.document.toObject(MarkerData::class.java)
                    newMarker.id = dc.document.id
                    newMarker.location.latitude = dc.document.get(LATITUDE_KEY).toString().toDouble()
                    newMarker.location.longitude = dc.document.get(LONGITUDE_KEY).toString().toDouble()
                    println("id new marker: " +newMarker.id)
                    tempList.add(newMarker)
                }
            }
            _markerList.value = tempList

        }
    }

    fun getMarker(markerId: String) {
        repository.getMarker(markerId).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("Markers", "Listen failed!", error)
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                val marker = value.toObject(MarkerData::class.java)
                if (marker != null) {
                    marker.id = markerId
                }
                _nameMarker.value = _actualMarker.value!!.name
                _idMarker.value = _actualMarker.value!!.id
                _actualMarker.value = marker
                _typeMarker.value = _actualMarker.value!!.type
                _descriptionMarker.value = _actualMarker.value!!.description
                //_photoList.valut = _actualMarker.value!!.photos
                _latitudeMarker.value = _actualMarker.value!!.location.latitude
                _longitudeMarker.value = _actualMarker.value!!.location.longitude
            } else {
                Log.d("Markers", "Current data; null")
            }
        }
    }

    /*
        fun addMarker() {
            repository.addMarkerYT(dataToSave)
        }

     */

    /*
    fun getMarker() {
        val info = repository.getMarkers().document("markers")
        changeNameMarker(info.)
    }

     */

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



    private fun addMarkerToMap(marker: MarkerData) {
        if (this.categoryMap!!.contains(marker.type))
            this.categoryMap!![marker.type]!!.add(marker)
        else {
            this.categoryMap!![marker.type] = mutableListOf()
            this.categoryMap!![marker.type]!!.add(marker)
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
        if (this.typeMarker.value == "All markers") {
            this._filterMarkerList.value = this._markerList.value
        } else {
            this.categoryMarkerList.value?.forEach {
                if (it.name == this.typeMarker.value) {
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

    fun changeNameMarker(value: String) {
        this.nameMarker.value = value
        //dataToSave.put(NAME_KEY, this.nameMarker)
    }

    fun changeTypeMarker(value: String) {
        this.typeMarker.value = value
        //dataToSave.put(TYPE_KEY, this.typeMarker)
    }

    fun changeDescription(value: String) {
        this.descriptionMarker.value = value
        //dataToSave.put(DESCRIPTION_KEY, this.description)
    }

    fun addPhoto(value: Bitmap) {
        this.photosMarker.value!!.add(value)
        //dataToSave.put(PHOTOS_KEY, this.photoList)
    }

    fun changeLatitude(value: Double) {
        this.latitudeMarker.value = value
        //dataToSave.put(LATITUDE_KEY, this.latitude)
    }

    fun changeLongitude(value: Double) {
        this.longitudeMarker.value = value
        //dataToSave.put(LONGITUDE_KEY, this.longitude)
    }

    /*
    fun addNewMarker() { //toDo: rebisar la photo por defectop q muestra
        this.actualMarker.value = MarkerData(
            id = this.idMarker.value,
            name = this.nameMarker.value,
            type = this.typeMarker.value,
            description = if (this.descriptionMarker.value.equals("")) "There isn't any description." else this.descriptionMarker.value,
            photos = if (this.photosMarker.value!!.isEmpty()) mutableListOf(
                Bitmap.createBitmap(
                    25,
                    25,
                    Bitmap.Config.ARGB_8888
                )
            ) else this.photosMarker.value,
            location = Location(
                this.latitudeMarker.value!!,
                this.longitudeMarker.value!!
            )
        )
    }

     */


    fun proveThatMarkerIsCorrect(): Boolean {
        var isCorrect = false
        if (
            this.nameMarker.value != "" &&
            this.typeMarker.value != ""
        )
            isCorrect = true
        return isCorrect
    }

    fun changeShowBottomSheet(value: Boolean) {
        this.showBottomSheet = value
    }

    fun restartMarkerAtributes() {
        this.nameMarker.value = ""
        this.typeMarker.value = "All markers"
        this.descriptionMarker.value = ""
        this.photosMarker.value = mutableListOf()
    }
}