package com.example.natifetesttask.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natifetesttask.data.local.images.ImageCached
import com.example.natifetesttask.data.local.images_deleted.ImageDeleted
import com.example.natifetesttask.data.remote.Data
import com.example.natifetesttask.domain.local.LocalImageRepository
import com.example.natifetesttask.domain.network.NetworkImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val networkImageRepository: NetworkImageRepository,
    private val localImageRepository: LocalImageRepository
) : ViewModel() {
    private val _imagesToShowGrid = MutableStateFlow(emptyList<String>())
    val imagesToShowGrid = _imagesToShowGrid.asStateFlow()

    private val _imagesToShowDetails = MutableStateFlow(emptyList<String>())
    val imagesToShowDetails = _imagesToShowDetails.asStateFlow()

    private val _selectedImageIndex = MutableStateFlow(0)
    val selectedImageIndex = _selectedImageIndex.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    override fun onCleared() {
        networkImageRepository.closeClient()
    }

    fun updateInputText(inputText: String) {
        _inputText.value = inputText
    }

    fun updateImagesToShowGrid(cachedImagesToShow: List<ImageCached>) {
        val listOfImagesToShow = cachedImagesToShow.map { it.pathLocalSmall }
        _imagesToShowGrid.value = listOfImagesToShow
    }

    fun updateImagesToShowDetails(
        context: Context,
        onDetailsClick: () -> Unit,
        onShowNoInternetAlert: () -> Unit,
        id: Int
    ) {
        if (isNetworkAvailable(context = context)) {
            updateSelectedImageIndex(id = id) {
                onDetailsClick()
            }
            viewModelScope.launch(Dispatchers.IO) {
                val cachedImages = localImageRepository.getAllImagesFromLocal().first()
                val listOfImagesToShowDetails = cachedImages.map { it.pathNetworkOriginal }
                _imagesToShowDetails.value = listOfImagesToShowDetails
            }
        } else {
            onShowNoInternetAlert()
        }
    }

    private fun updateSelectedImageIndex(id: Int, onDetailsClick: () -> Unit) {
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
        alertDialogNoInternetIsVisible: MutableState<Boolean>,
        buttonGetImagesEnabled: MutableState<Boolean>,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNetworkAvailable(context = context)) {
                buttonGetImagesEnabled.value = false
                localImageRepository.deleteAll()
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
                buttonGetImagesEnabled.value = true
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
        val deletedImagesPaths =
            localImageRepository.getAllImageDeleted().firstOrNull()
                ?.map { it.pathNetworkOriginal } ?: listOf()
        images.forEach { image ->
            val imagePath = image.images.original.url
            val needToSave = imagePath !in deletedImagesPaths
            if (needToSave) {
                networkImageRepository.downloadGifAndSave(
                    pathOriginal = image.images.original.url,
                    pathSmall = image.images.fixedWidthSmall?.url ?: "",
                    context = context
                )
            }
        }
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
        offset.value += 25
        viewModelScope.launch(Dispatchers.IO) {
            val cachedImages = localImageRepository.getGifs(offset.value, limit)
            if (!cachedImages.isNullOrEmpty()) {
                resetImagesToShow()
                updateImagesToShowGrid(cachedImages)
            } else {
                if (isNetworkAvailable(context = context)) {
                    if (!inputText.isNullOrBlank() || !inputText.isNullOrEmpty()) {
                        isLoadingNextFromNetwork.value = true
                        val giphyResponse = networkImageRepository.getGiphyResponse(
                            search = search,
                            offset = offset.value,
                            limit = limit
                        )
                        cacheImageUrlAndData(
                            images = giphyResponse.data,
                            context = context,
                            offset = offset.value,
                            limit = limit
                        )
                    } else {
                        offset.value -= 25
                    }
                } else {
                    offset.value -= 25
                }
            }
        }
    }

    fun getImagesPrevious(offset: MutableState<Int>, limit: Int) {
        if (offset.value >= 25) {
            offset.value -= 25
            viewModelScope.launch(Dispatchers.IO) {
                val cachedImages = localImageRepository.getGifs(offset.value, limit)
                updateImagesToShowGrid(cachedImages)
            }
        }
    }

    fun updateImagesFromLocalStorage(
        offset: Int,
        limit: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val cachedImagesToShow = localImageRepository.getGifs(offset, limit)
            updateImagesToShowGrid(cachedImagesToShow)
        }
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

    fun deleteImage(
        imagePathNetworkOriginal: String,
        onClose: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val imageCached =
                localImageRepository.getImageByPathNetworkOriginal(imagePathNetworkOriginal)
            val imagePathLocalSmall = imageCached?.pathLocalSmall

            if (imagePathLocalSmall != null) {
                deleteFile(imagePathLocalSmall)

                val currentDate = Date(System.currentTimeMillis())
                val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val dateStr = dateFormat.format(currentDate)
                val timeStr = timeFormat.format(currentDate)
                val timeDeleted = "$dateStr | $timeStr"

                val newImageDeleted = ImageDeleted(
                    pathNetworkOriginal = imagePathNetworkOriginal,
                    pathLocalSmall = imagePathLocalSmall,
                    timeDeleted = timeDeleted
                )
                localImageRepository.insertImageDeleted(newImageDeleted)
                localImageRepository.deleteImageByPathNetworkOriginal(imagePathNetworkOriginal)
                withContext(Dispatchers.Main) {
                    onClose()
                }
            }
        }
    }

    private fun deleteFile(filePath: String): Boolean {
        val file = File(filePath)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }
}