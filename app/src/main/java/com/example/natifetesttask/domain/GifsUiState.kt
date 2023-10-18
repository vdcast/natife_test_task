package com.example.natifetesttask.domain

import com.example.natifetesttask.data.Data

data class GifsUiState(
    val images: List<Data> = emptyList(),
    val selectedSearch: String? = null
) {
}
