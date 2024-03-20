package com.example.maps_map_seancostelloecacho.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.maps_map_seancostelloecacho.models.Category
import com.example.maps_map_seancostelloecacho.models.Location
import com.example.maps_map_seancostelloecacho.models.MarkerData
import java.util.SortedMap

class MarkerViewModel: ViewModel() {

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
    var typeMarker by mutableStateOf("")
        private set

    var expandedOptions by mutableStateOf(false)
        private set

    var expandedOptionsTopBar by mutableStateOf(false) //toDo: canviando expanded options
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


    // Methods
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
        Log.i("type marker", typeMarker)
        this.categoryMarkerList.value?.forEach {
            if (it.name == this.typeMarker) {
                Log.i("dentro en el type", typeMarker)
                this._filterMarkerList.value = it.items
            }
        }
    }

    fun changeNameMarker(value : String) {
        this.name = value
    }

    fun changeTypeMarker(value : String) {
        this.typeMarker = value
    }

    fun changeExpandedOptions(value: Boolean) {
        this.expandedOptions = value
    }

    fun changeExpandedOptionsTopBar(value: Boolean) {
        this.expandedOptions = value
    }

    fun changeDescription(value: String) {
        this.description = value
    }

    /* toDO: passar drawabel to bitmap
    fun startRowOfImages() {
        val context = LocalContext.current
        val bitmap = getBitymapFromImage()
    }

     */
    fun addPhoto(value: Bitmap) {
        this.photoList.add(value)
    }

    fun changeLatitude(value: Double) {
        this.latitude = value
    }

    fun changeLongitude(value: Double) {
        this.longitude = value
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
}