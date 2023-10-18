package com.example.natifetesttask.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natifetesttask.domain.network.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GifsUiState())
    val uiState = _uiState.asStateFlow()

//    init {
//        getImages()
//    }
    fun getImages(search: String, offset: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val images = networkRepository.getImages(
                search = search,
                offset = offset
            )
            _uiState.update {
                it.copy(
                    images = images
                )
            }
        }
    }

}