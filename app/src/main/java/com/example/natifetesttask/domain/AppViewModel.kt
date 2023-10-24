package com.example.natifetesttask.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natifetesttask.data.local.images.ImageCached
import com.example.natifetesttask.data.local.prefs.SharedPrefs
import com.example.natifetesttask.data.remote.Data
import com.example.natifetesttask.domain.local.LocalImageRepository
import com.example.natifetesttask.domain.network.NetworkImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val networkImageRepository: NetworkImageRepository,
    private val localImageRepository: LocalImageRepository,
    private val prefs: SharedPrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow(GifsUiState())
    val uiState = _uiState.asStateFlow()

    private val _imagesFromLocalStorage = MutableStateFlow(emptyList<ImageCached>())
    val imagesFromLocalStorage = _imagesFromLocalStorage.asStateFlow()

    private val _imagesToShowGrid = MutableStateFlow(emptyList<String>())
    val imagesToShowGrid = _imagesToShowGrid.asStateFlow()

    private val _imagesToShowDetails = MutableStateFlow(emptyList<String>())
    val imagesToShowDetails = _imagesToShowDetails.asStateFlow()

    private val _firstOpen = MutableStateFlow(prefs.getFirstOpen())
    val firstOpen = _firstOpen.asStateFlow()

    private val _selectedImageIndex = MutableStateFlow(0)
    val selectedImageIndex = _selectedImageIndex.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    init {
//        restoreImages()
    }

    override fun onCleared() {
        Log.d("MYLOG", "onCleared")
        networkImageRepository.closeClient()
    }

    private fun restoreImages() {
//        _uiState.update { currentState ->
//            currentState.copy()
//        }
    }

    fun updateInputText(inputText: String) {
        _inputText.value = inputText
    }

    fun updateImagesToShowGrid(images: List<String>) {
        _imagesToShowGrid.value = images
    }

    fun updateImagesToShowDetails(
        context: Context,
        onDetailsClick: () -> Unit,
        onShowNoInternetAlert: () -> Unit,
        imagePath: String,
        id: Int
    ) {
        Log.d("MYLOG", "selectedImageIndex.value: ${selectedImageIndex.value}")

        if (isNetworkAvailable(context = context)) {
            updateSelectedImageIndex(id = id) {
                onDetailsClick()
            }
            viewModelScope.launch(Dispatchers.IO) {
                val cachedImages = localImageRepository.getAllImagesFromLocal().first()
                val listOfImagesToShowDetails = cachedImages.map { it.pathNetworkOriginal }
                _imagesToShowDetails.value = listOfImagesToShowDetails
//                withContext(Dispatchers.Main) {
//                    onDetailsClick()
//                }
            }
        } else {
            onShowNoInternetAlert()
        }
    }
    private fun updateSelectedImageIndex(id: Int, onDetailsClick: () -> Unit,) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedImageIndex.value = id
            withContext(Dispatchers.Main) {
                onDetailsClick()
            }
        }
    }

    fun getImages(
        search: String,
        offset: Int,
        limit: Int,
        context: Context,
        alertDialogNoInternetIsVisible: MutableState<Boolean>
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            if (isNetworkAvailable(context = context)) {
                localImageRepository.deleteAll()
//            restoreImages()
                updateFirstOpenAndMarkAsDone()
                resetImagesToShow()

                val giphyResponse = networkImageRepository.getGiphyResponse(
                    search = search,
                    offset = offset,
                    limit = limit
                )

                cacheImageUrlAndData(
                    images = giphyResponse.data,
                    context = context,
                    offset = offset,
                    limit = limit
                )

                /* TODO delete update of uiState */
                _uiState.update {
                    it.copy(
                        images = giphyResponse.data,
                        pagination = giphyResponse.pagination,
                        meta = giphyResponse.meta
                    )
                }
            } else {
                alertDialogNoInternetIsVisible.value = true
            }
        }
    }

    private suspend fun cacheImageUrlAndData(
        images: List<Data>,
        context: Context,
        offset: Int,
        limit: Int
    ) {
        images.forEach { image ->
            networkImageRepository.downloadGifAndSave(
                pathOriginal = image.images.original.url,
                pathSmall = image.images.fixedWidthSmall.url,
                context = context
            )
        }
//
        updateImagesFromLocalStorage(
            offset = offset,
            limit = limit
        )

    }

    fun getImagesNext(
        search: String,
        offset: MutableState<Int>,
        limit: Int,
        context: Context,
        inputText: String,
        isLoadingNextFromNetwork: MutableState<Boolean>
    ) {
        Log.d("MYLOG", "1 offset.value: ${offset.value}")

        offset.value += 25

        Log.d("MYLOG", "2 offset.value: ${offset.value}")

        viewModelScope.launch(Dispatchers.IO) {
            val cachedImages = localImageRepository.getGifs(offset.value, limit)
            Log.d("MYLOG", "cachedImages: ${cachedImages.firstOrNull()}")

            if (!cachedImages.isNullOrEmpty()) {

                resetImagesToShow()
                Log.d("MYLOG", "~ ~ ~ KEK 8 ~ ~ ~")

                resetUiState()
                _uiState.update {
                    it.copy(imagesCached = cachedImages)
                }

                val listOfImagesToShow = cachedImages.map { it.pathLocalSmall }
                updateImagesToShowGrid(listOfImagesToShow)
            } else {
                if (isNetworkAvailable(context = context)) {
                    if (!inputText.isNullOrBlank() || !inputText.isNullOrEmpty()) {

                        isLoadingNextFromNetwork.value = true


                        val giphyResponse = networkImageRepository.getGiphyResponse(
                            search = search,
                            offset = offset.value,
                            limit = limit
                        )
                        _uiState.update {
                            it.copy(
                                images = giphyResponse.data,
                                pagination = giphyResponse.pagination,
                                meta = giphyResponse.meta
                            )
                        }
                        cacheImageUrlAndData(
                            images = giphyResponse.data,
                            context = context,
                            offset = offset.value,
                            limit = limit
                        )
                    } else {

                        Log.d("MYLOG", "7 offset.value: ${offset.value}")

                        offset.value -= 25

                        Log.d("MYLOG", "8 offset.value: ${offset.value}")
                    }
                } else {
                    Log.d("MYLOG", "3 offset.value: ${offset.value}")

                    offset.value -= 25

                    Log.d("MYLOG", "4 offset.value: ${offset.value}")
                }
            }
        }
    }

    fun getImagesPrevious(offset: MutableState<Int>, limit: Int) {
        if (offset.value >= 25) {
            offset.value -= 25
            viewModelScope.launch(Dispatchers.IO) {
                val cachedImages = localImageRepository.getGifs(offset.value, limit)


                val listOfImagesToShow = cachedImages.map { it.pathLocalSmall }
                updateImagesToShowGrid(listOfImagesToShow)

//                val giphyResponse = networkImageRepository.getGiphyResponse(
//                    search = search,
//                    offset = offset.value,
//                    limit = limit
//                )
//                _uiState.update {
//                    it.copy(
//                        images = giphyResponse.data,
//                        pagination = giphyResponse.pagination,
//                        meta = giphyResponse.meta
//                    )
//                }
            }
        }
    }

    fun updateImagesFromLocalStorage(
        offset: Int,
        limit: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("MYLOG", "~ ~ ~ KEK 2 ~ ~ ~")

            val cachedImagesToShow = localImageRepository.getGifs(offset, limit)


            if (cachedImagesToShow.isNotEmpty()) {
                Log.d("MYLOG", "~ ~ ~ KEK 6 ~ ~ ~")
                resetUiState()
                _uiState.update {
                    it.copy(imagesCached = cachedImagesToShow)
                }
            }

            val listOfImagesToShow = cachedImagesToShow.map { it.pathLocalSmall }

            updateImagesToShowGrid(listOfImagesToShow)
        }
    }

    private fun resetUiState() {
        _uiState.update { it.copy() }
    }

    private fun resetImagesToShow() {
        _imagesToShowGrid.value = emptyList<String>()
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR
        ))
    }

    private fun updateFirstOpenAndMarkAsDone() {
        prefs.setFirstOpen(firstOpen = true)
        _firstOpen.value = prefs.getFirstOpen()
    }

    private fun setFirstOpen(firstOpen: Boolean) {
        prefs.setFirstOpen(firstOpen)
    }
}

