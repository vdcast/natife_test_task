package com.example.natifetesttask.presentation.ui.screens

import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.natifetesttask.domain.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    appViewModel: AppViewModel = hiltViewModel()
) {
    val uiState by appViewModel.uiState.collectAsState()

    var inputText by remember { mutableStateOf("") }
    var offset by remember { mutableStateOf(0) }

    val LIMIT = 25

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { newValue ->
                inputText = newValue
            }
        )
        Button(
            onClick = {
                appViewModel.getImages(
                    search = inputText,
                    offset = offset
                )
            }
        ) { Text(text = "Get Images!") }
        Button(
            onClick = {
                if (offset >= 25) {
                    offset -= 25
                    appViewModel.getImages(
                        search = inputText,
                        offset = offset
                    )
                }
            }
        ) { Text(text = "Previous Page") }
        Button(
            onClick = {

//                val itemsToLoad = min(LIMIT, uiState - offset)

                offset += 25
                appViewModel.getImages(
                    search = inputText,
                    offset = offset
                )
            }
        ) { Text(text = "Next Page") }

        AnimatedVisibility(visible = uiState.images.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = inputText,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 5.dp)
                ) {
                    items(uiState.images) {
                        GifImage(imagePath = it.images.original.url)
                    }
                }
            }
        }
    }
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    imagePath: String
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = imagePath).apply(block = {
//                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.0f),
    )
}