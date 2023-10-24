package com.example.natifetesttask.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.natifetesttask.domain.AppViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GifDetails(
    appViewModel: AppViewModel
) {
    val imagesToShowDetails by appViewModel.imagesToShowDetails.collectAsState()
    val selectedImageIndex by appViewModel.selectedImageIndex.collectAsState()

//    val pageCount = imagesToShow.size

    val pagerState = rememberPagerState(
        pageCount = {
            imagesToShowDetails.size
        },
        initialPage = selectedImageIndex
    )

    val coroutineScope = rememberCoroutineScope()

    var previousPageIteration by remember { mutableStateOf(0) }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(
//                        0.85f
                if (pagerState.currentPage == 5) 0.85f else 0.85f
            )
    ) { page ->
        GifDetailsPager(
            page = page,
            imagePath = imagesToShowDetails[page]
        )
    }
}

@Composable
fun GifDetailsPager(page: Int, imagePath: String) {
    Column {
        Text("Page: $page")
        AsyncImage(
            model = imagePath,
            contentDescription = "image details",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.0f)
        )
    }
}