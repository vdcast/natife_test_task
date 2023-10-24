package com.example.natifetesttask.domain

import com.example.natifetesttask.data.local.images.ImageCached
import com.example.natifetesttask.data.remote.Data
import com.example.natifetesttask.data.remote.Meta
import com.example.natifetesttask.data.remote.Pagination

data class GifsUiState(
    val images: List<Data> = emptyList(),
    val pagination: Pagination? = null,
    val meta: Meta? = null,
    val imagesCached: List<ImageCached> = emptyList()
) {

}
