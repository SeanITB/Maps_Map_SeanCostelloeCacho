package com.example.maps_map_seancostelloecacho.viewModel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.maps_map_seancostelloecacho.firebase.Repository
import com.example.maps_map_seancostelloecacho.models.Category
import com.example.maps_map_seancostelloecacho.models.Location
import com.example.maps_map_seancostelloecacho.models.MapEvent
import com.example.maps_map_seancostelloecacho.models.MapState
import com.example.maps_map_seancostelloecacho.models.MapStyle
import com.example.maps_map_seancostelloecacho.models.MarkerData
import com.example.maps_map_seancostelloecacho.models.RegisterValidationContent
import com.example.maps_map_seancostelloecacho.navigation.NavigationItems
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.SortedMap
import java.util.regex.Pattern

class MapViewModel : ViewModel() {

    // firebase values
    private val repository = Repository()

    private val auth = FirebaseAuth.getInstance()

    // FIREBASE CONSTANTS
    val NAME_KEY = "name"

    val TYPE_KEY = "type"
    val PHOTOS_KEY = "photos"
    val LATITUDE_KEY = "latitude"
    val LONGITUDE_KEY = "longitude"

    // APP CONSTANTS
    val MAP_SCREEN_KEY = "mapScreen"

    // app values
    var state by mutableStateOf(MapState())

    private val _darkThem = MutableLiveData(false)
    val darkThem = _darkThem

    private val _navigationItemsItems = MutableLiveData( //toDo: no hacer asi, poner un bucle
        mapOf(
            NavigationItems.GalleryScreen.label to NavigationItems.GalleryScreen.route,
            NavigationItems.MapGeolocalisationScreen.label to NavigationItems.MapGeolocalisationScreen.route,
            NavigationItems.MarkerCategoriesListScreen.label to NavigationItems.MarkerCategoriesListScreen.route,
            NavigationItems.CameraFromMapScreen.label to NavigationItems.CameraFromMapScreen.route,
            NavigationItems.CameraFromMarkerListScreen.label to NavigationItems.CameraFromMarkerListScreen.route,
            NavigationItems.MapScreen.label to NavigationItems.MapScreen.route,
            NavigationItems.TakePhotoFromMapScreen.label to NavigationItems.TakePhotoFromMapScreen.route,
            NavigationItems.TakePhotoFromMarkerListScreen.label to NavigationItems.TakePhotoFromMarkerListScreen.route
        )
    )
    val navigationItems = _navigationItemsItems

    private val _actualPosition = MutableLiveData(LatLng(0.0, 0.0))
    val actualPosition = _actualPosition

    private val _recentMarker = MutableLiveData<MarkerData?>(null)
    val recentMarker = _recentMarker

    private val _listMarkerType = MutableLiveData(
        mutableListOf(
            "All markers",
            "Park",
            "Bookstore",
            "Sports Center",
            "Museum",
            "Restaurant"
        )
    )
    val listMarkerType = _listMarkerType

    private val _turnOnSeconProcess = MutableLiveData(false)
    val turnOnSeconProcess = _turnOnSeconProcess

    private val _userName = MutableLiveData<String>("")
    val userName = _userName

    private val _uriUrl = MutableLiveData<String>("")
    val uriUrl = _uriUrl

    private val _password = MutableLiveData<String>("")
    val password = _password


    private val _nameMarker = MutableLiveData<String>("")
    val nameMarker = _nameMarker

    private val _goToNext = MutableLiveData<Boolean>(false)
    val goToNext = _goToNext

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading = _isLoading

    private val _finishSort = MutableLiveData(false)
    val finishSort = _finishSort

    private val _userId = MutableLiveData<String>("")
    val userId = _userId

    private val _loggedUser = MutableLiveData<String>("")
    val loggedUser = _loggedUser

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

    private val _actualScreen = MutableLiveData(MAP_SCREEN_KEY)
    val actualScreen = _actualScreen

    private val _showBottomSheetFromListSheet = MutableLiveData(false)
    val showBottomFromListSheet = _showBottomSheetFromListSheet

    private val _isFilitered = MutableLiveData(false)
    val isFiltered = _isFilitered

    private val _isUriUpload = MutableLiveData(false)
    val isUriUpload = _isUriUpload

    private val _markersComplet = MutableLiveData(false)
    val markersComplet = _markersComplet

    private val _markerList = MutableLiveData(emptyList<MarkerData>())
    val markerList = _markerList

    private var _categoryMarkrList = MutableLiveData<List<Category>>(mutableListOf())
    val categoryMarkerList = _categoryMarkrList

    var categoryMap: SortedMap<String, MutableList<MarkerData>>? by mutableStateOf(sortedMapOf())
        private set

    private val _actualMarker = MutableLiveData<MarkerData?>(null)
    val actualMarker = _actualMarker

    private val _isEditing = MutableLiveData(false)
    val isEditing = _isEditing

    private val _typeMarkerForFilter = MutableLiveData("All markers")
    val typeMarkerForFilter = _typeMarkerForFilter

    private val _typeMarker = MutableLiveData("")
    val typeMarker = _typeMarker

    private val _photoUrl = MutableLiveData("")
    val photoUrl = _photoUrl

    private var _expandedBottomSheet = MutableLiveData(false)
    val expandedBottomSheet = _expandedBottomSheet

    private var _expandedTopBar = MutableLiveData(false)
    val expandedTopBar = _expandedTopBar

    private var _showBottomSheetFromMap = MutableLiveData(false)
    val showBottomSheetFromMap = _showBottomSheetFromMap

    private val _uri = MutableLiveData<Uri?>(null)
    val uri = _uri

    // Firebase Methods
    fun addMarker() {
        //instanceActualMarker(newMarker)

        Log.i("addMarker", "addMarker in VM hihih: actualMarker: ${this.actualMarker.value!!}, currentUser: ${auth.currentUser?.email!!}, uriUrl: ${uriUrl}")

        repository.addMarker(this.actualMarker.value!!, auth.currentUser?.email!!, this._uriUrl.value!!)

        getMarkers()
    }

    /*
    fun instanceActualMarker(newMarker: MarkerData) {
        this.actualMarker.value = MarkerData(
            id = this.idForMarker.value,
            name = this.nameMarker.value!!,
            type = this.typeMarker.value!!,
            description = if (this.descriptionMarker.value.equals("")) "There isn't any description." else this.descriptionMarker.value!!,
            photo = this.uri.value.toString(),
            location = Location(
                this.latitudeMarker.value!!,
                this.longitudeMarker.value!!
            )
        )
    }

     */

    fun deleteMarker(idMarker: String) {
        repository.deleteMarker(idMarker)
        getMarkers()
    }

    fun getMarkers() {
        repository.getMarkers().whereEqualTo("owner", auth.currentUser?.email)
            .addSnapshotListener { value, error ->
                processOfGettingMarkerFormDataStore(error, value)
            }
        Log.i("markers", "values in all markers: ${categoryMarkerList.value}")

    }

    fun getFilterMarkers(typeMarker: String) {
        repository.getMarkers().whereEqualTo("owner", auth.currentUser?.email).whereEqualTo(TYPE_KEY, typeMarker)
            .addSnapshotListener { value, error ->
                processOfGettingMarkerFormDataStore(error, value)
            }
    }

    private fun processOfGettingMarkerFormDataStore(
        error: FirebaseFirestoreException?,
        value: QuerySnapshot?
    ) {
        if (error != null) {
            Log.e("Firestore error!", error.message.toString())
            return
        }
        val tempList = mutableListOf<MarkerData>()
        for (dc: DocumentChange in value?.documentChanges!!) {
            if (dc.type == DocumentChange.Type.ADDED) {
                Log.i("photo", "photo in VM ${dc.document.get(PHOTOS_KEY).toString()}")
                val newMarker = MarkerData(
                    id = dc.document.id,
                    name = dc.document.get(NAME_KEY).toString(),
                    type = dc.document.get(TYPE_KEY).toString(),
                    uriUrl = dc.document.get(PHOTOS_KEY).toString(),
                    location = Location(
                        latitude = dc.document.get(LATITUDE_KEY).toString().toDouble(),
                        longitude = dc.document.get(LONGITUDE_KEY).toString().toDouble()
                    )
                )
                tempList.add(newMarker)
            }
        }
        Log.i("markers", "temList: $tempList")
        _markerList.value = tempList
        _markersComplet.value = true
        _turnOnSeconProcess.value = if (_turnOnSeconProcess.value!!) false else true
    }

    fun getMarker(markerId: String) { //toDo: transportarese a la posicion del marker
        repository.getMarker(markerId).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("Markers", "Listen failed!", error)
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                val marker = value.toObject(MarkerData::class.java)
                if (marker != null) {
                    marker.id = markerId
                    marker.uriUrl = value.get("photos").toString()
                }
                _actualMarker.value = marker
                _uri.value = marker!!.uriUrl.toUri()
            } else {
                Log.d("Markers", "Current data; null")
            }
        }
    }

    fun registerValidation(
        validation: RegisterValidationContent
    ) {
        if (!password.equals(validation.passwordCheck)) {
            Toast.makeText(validation.context, "Passwords are not the same.", Toast.LENGTH_LONG).show()
        } else if (!passwordVerification(validation.password)) {
            Toast.makeText(validation.context, "Incorrect password.", Toast.LENGTH_LONG).show()
        } else if (!proveThatItsAEmail(validation.email)) {
            Toast.makeText(validation.context, "Incorrect email.", Toast.LENGTH_LONG).show()
        } else {
            register()
        }
    }

    fun editMarker(actualMarker: MarkerData) {
        repository.editMarker(actualMarker)
    }

    // Firebase Authentication
    fun register() {
        auth.createUserWithEmailAndPassword(this.userName.value!!, this.password.value!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _goToNext.value = true
                } else {
                    _goToNext.value = false
                    Log.d("ERROR", "Ups, user already exists.")
                }
                modifiyProcessing()
            }
    }

    fun modifiyProcessing() {
        this._isLoading.value = false
    }

    fun changeNameMarke(value: String) {
        this._nameMarker.value = value
    }

    var hola = auth.currentUser

    fun login(context: Context) {
        auth.signInWithEmailAndPassword(this.userName.value!!, this.password.value!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userId.value = task.result.user?.uid
                    _loggedUser.value = task.result.user?.email?.split("@")?.get(0)
                    _goToNext.value = true
                } else {
                    _goToNext.value = false
                }
                modifiyProcessing()
            }
            .addOnFailureListener { task ->
                Toast.makeText(context, "Incorrect email or password.", Toast.LENGTH_LONG).show()
            } // toDo: si contra no esta bien
    }

    fun logout() {
        auth.signOut()
    }


    // App Methods
    fun changeDarkThem(value: Boolean) {
        this._darkThem.value = value
    }

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.ToggleFalloutMap -> {
                state = state.copy(
                    properties = state.properties.copy(
                        mapStyleOptions = if (state.isFollautMap) {
                            null
                        } else MapStyleOptions(MapStyle.json)
                    ),
                    isFollautMap = !state.isFollautMap
                )
            }
        }
    }

    fun changeIsEditing(value: Boolean) {
        this._isEditing.value = value
    }

    fun changeActualPosition(value: LatLng) {
        this._actualPosition.value = value
    }

    fun changeGoToNext(value: Boolean) {
        this._goToNext.value = value
    }

    fun changeMarkerComplete(value: Boolean) {
        this._markersComplet.value = value
    }

    fun changeFinishSort(value: Boolean) {
        this._finishSort.value = value
    }

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
        this._mapPermissionGranted.value = granted
    }

    fun setShouldShowMapPermissionRationale(granted: Boolean) {
        this._shouldShowPermissionMapRationale.value = granted
    }

    fun setShowMapPermissionDenied(granted: Boolean) {
        this._showMapPermissionDenied.value = granted
    }

    fun changeActualScreen(value: String) {
        this._actualScreen.value = value
    }

    fun changeUserName(value: String) {
        this._userName.value = value
    }

    fun changePassword(value: String) {
        this._password.value = value
    }

    private fun addMarkerToMap(marker: MarkerData) {
        if (this.categoryMap!!.contains(marker.type))
            this.categoryMap!![marker.type]!!.add(marker)
        else {
            this.categoryMap!![marker.type] = mutableListOf()
            this.categoryMap!![marker.type]!!.add(marker)
        }
    }

    fun sortMarkerList() {
        this._categoryMarkrList.value = emptyList()
        if (markerList.value!!.isNotEmpty()) {
            this._categoryMarkrList.value = this.categoryMap?.map {
                Category(
                    name = it.key,
                    items = it.value
                )
            }
        }
    }

    fun createMapOfMarkers() {
        if (this.categoryMap!!.isNotEmpty()) {
            this.categoryMap!!.clear()
        }
        Log.i("markers", "value of create map: ${markerList.value} ")
        if (markerList.value!!.isNotEmpty()) {
            for (m in markerList.value!!) {
                addMarkerToMap(m)
            }
        }
    }

    fun changeIsFiltred(value: Boolean) {
        this.isFiltered.value = value
    }


    /*
    fun whenMarkerTypedChanged() { //toDO: rebisar si sobra
        if (this.actualScreen.value != "BottomSheet" && this.typeMarker.value != "All markers") {
            this.changeIsFiltred(true)
            //this.createFilerList()
            //this.getFilterMarkers()
            this.changeExpandedTopBar(false)
        } else if (this.typeMarker.value == "All markers"){
            this.changeIsFiltred(false)
            //this.getMarkers()
        } else {
            this.changeExpandedBottomSheet(false)
        }
    }

     */

    fun changeExpandedBottomSheet(value: Boolean) {
        this._expandedBottomSheet.value = value
    }

    fun changeExpandedTopBar(value: Boolean) {
        this._expandedTopBar.value = value
    }


    fun changeTypeMarker(value: String) {
        this._typeMarker.value = value
    }

    fun changeTypeMarkerForFilter(value: String) {
        this._typeMarkerForFilter.value = value
        Log.i("typeMarker", "typeMarker is: $value")
        if (value.equals("All markers")) {
            getMarkers()
        } else {
            getFilterMarkers(value)
        }
    }

    fun changeUri(value: Uri?) {
        this._uri.value = value
    }

    fun proveThatMarkerIsCorrect(newMarker: MarkerData): Boolean {
        return (newMarker.name != "" && newMarker.type != "" && newMarker.uriUrl != "")
    }

    fun changeShowBottomFromMapSheet(value: Boolean) {
        this.showBottomSheetFromMap.value = value
    }

    fun whenAddMarkerFromMap(
        context: Context,
    ) {
        if (this.proveThatMarkerIsCorrect(this.actualMarker.value!!)) { //toDo: la comprovacion mal
            this.changeShowBottomFromMapSheet(false)
            if (this.uri.value != null) {

                //Log.i("addMarker", "addMarker uriURl: $uriURL")
                this.addMarker()

            }
            this.restartMarkerAtributes()
        } else
            Toast.makeText(context, "There are unfinished fields.", Toast.LENGTH_LONG)
                .show()
    }

    fun uploadImage(/*onUriUpload: (String) -> Unit*/) {
        val formatter = SimpleDateFormat("yyyy_MM_DD_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("images/$fileName")
        storage.putFile(this.uri.value!!)
            .addOnSuccessListener {
                storage.downloadUrl.addOnSuccessListener {
                    val uriUrl = it.toString()
                    Log.i("IMAGE UPLOAD", "Image uploaded successfully! with image: ${uriUrl}")
                    //changeUriUrl(uriUrl)
                    changeUriUrl(uriUrl)
                    changeIsUriUrlUpload(true)
                }
            }
            .addOnFailureListener {
                Log.i("IMAGE UPLOAD", "Ups, image upload failed!")
            }

    }

    fun changeIsUriUrlUpload(b: Boolean) {
        this._isUriUpload.value = b
    }

    fun changeUriUrl(uriUrl: String) {
        this._uriUrl.value = uriUrl
    }


    /*
    fun changeImageUrl(value: String) {
        this._imageUrl.value = value
    }

     */
    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? { //toDo: passarlo al storage con url
        val filename = "${System.currentTimeMillis()}.jpg"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, filename)
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        }

        val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            val outstream: OutputStream? = context.contentResolver.openOutputStream(it)
            outstream?.let { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
            outstream?.close()
        }

        return uri
    }


    fun restartMarkerAtributes() {
        this._actualMarker.value = null
        this._typeMarker.value = ""
        this._uri.value = null
        this._nameMarker.value = ""
    }

    fun chngeUserId(value: String) {
        this._userId.value = value
    }

    fun changeLoggedUser(value: String) {
        this._loggedUser.value = value
    }

    fun proveThatItsAEmail(userName: String): Boolean {
        var isCorrect = false
        val email = "@"
        if (email in userName) {
            val emailNameInput = userName.split("@").get(0)
            val emailOrgagnitzationInput = userName.split("@").get(1)

            val emailNameRegex = Regex("[a-zA-Z0-9._%+-]+")
            val emailrgagnitzationRegex = Regex("[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")

            if (
                emailNameRegex.matches(emailNameInput) &&
                emailrgagnitzationRegex.matches(emailOrgagnitzationInput)
            ) {
                isCorrect = true
            }
        }

        return isCorrect
    }

    fun passwordVerification(password: String): Boolean {
        // Regex expression that verify that the password, at less, has:
        // 1 digit and 6 characters.
        val regualarExpresionPasword = "(?=.*[\\d])(?=.*[a-z]).{6,}"
        val p: Pattern = Pattern.compile(regualarExpresionPasword)
        return p.matcher(password).matches()
    }

    fun initializeRecentMarker(
        name: String,
        type: String,
        latitude: Double,
        longitude: Double
    ) {
        this._recentMarker.value = MarkerData(
            id = null,
            name = name,
            type = type,
            uriUrl = "",
            location = Location(latitude = latitude, longitude = longitude)
        )
    }

    fun changeNewMarker(newMarker: MarkerData) {
        this._actualMarker.value = newMarker
    }

    fun changeShowBottomSheetFromList(value: Boolean) {
        this._showBottomSheetFromListSheet.value = value
    }

    fun whenAddMarkerFromList(it: Context) {
        this.editMarker(actualMarker.value!!)
        this._showBottomSheetFromListSheet.value = false
    }

}