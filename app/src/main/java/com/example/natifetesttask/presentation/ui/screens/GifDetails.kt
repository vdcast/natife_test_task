package com.example.natifetesttask.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.natifetesttask.domain.AppViewModel
import com.example.natifetesttask.presentation.ui.theme.Red20

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GifDetails(
    appViewModel: AppViewModel,
    onCLose: () -> Unit
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
            .fillMaxSize()
    ) { page ->
        GifDetailsPager(
            page = page,
            imagePath = imagesToShowDetails[page],
            onClose = onCLose,
            onDelete = {
                appViewModel.deleteImage(
                    imagePathNetworkOriginal = imagesToShowDetails[page],
                    onClose = onCLose
                )
            }
        )
    }
}

@Composable
fun GifDetailsPager(page: Int, imagePath: String, onClose: () -> Unit, onDelete: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(48.dp))
            Text("Page: $page")
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onClose() },
                imageVector = Icons.Default.Close,
                contentDescription = "close details"
            )
        }
        AsyncImage(
            model = imagePath,
            contentDescription = "image details",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.0f)
        )
        Button(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(16.dp),
            onClick = { onDelete() },
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(4.dp),
            colors = ButtonDefaults.buttonColors(Red20)
        ) {
            Text(
                text = "Delete GIF",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}