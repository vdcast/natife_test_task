package com.example.natifetesttask.presentation.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.natifetesttask.domain.AppViewModel
import com.example.natifetesttask.presentation.ui.util.AlertDialogNoInternet
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    appViewModel: AppViewModel = hiltViewModel(),
    onDetailsClick: () -> Unit
) {
    val context = LocalContext.current

    val uiState by appViewModel.uiState.collectAsState()
    val uiStateImages = uiState.images
    val uiStatePagination = uiState.pagination
    val imagesFromLocalState by appViewModel.imagesFromLocalStorage.collectAsState()

    val imagesToShow = appViewModel.imagesToShow.collectAsState()

    var inputText by remember { mutableStateOf("") }
    var searchBarText by remember { mutableStateOf("") }
    val offset = remember { mutableStateOf(0) }
    var limit by remember { mutableStateOf(25) }

    val alertDialogNoInternetIsVisible = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
//    val firstSearchAttempt = remember { mutableStateOf(true) }
    val firstSearchAttempt by appViewModel.firstOpen.collectAsState()
    val isLoadingNextFromNetwork = remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        Log.d("MYLOG", "~ ~ ~ KEK 1 ~ ~ ~")
        Log.d("MYLOG", "LaunchedEffect (imagesToShow.value)")

        appViewModel.updateImagesFromLocalStorage(
            offset = offset.value,
            limit = limit
        )
    }

    LaunchedEffect(key1 = imagesToShow.value) {
        isLoading.value = imagesToShow.value.isNullOrEmpty()
        isLoadingNextFromNetwork.value = imagesToShow.value.isNullOrEmpty()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
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
//                    firstSearchAttempt.value = true
//                    isLoading.value = true
                    searchBarText = inputText
                    if (!inputText.isNullOrBlank() || !inputText.isNullOrEmpty()) {
                        appViewModel.getImages(
                            search = inputText,
                            offset = 0,
                            limit = limit,
                            context = context,
                            alertDialogNoInternetIsVisible = alertDialogNoInternetIsVisible
                        )
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please input something")
                        }
                    }
                }
            ) { Text(text = "Get Images!") }


            if (!firstSearchAttempt) {
                Text("Input something to search some gifs.")
            } else if (isLoading.value) {
                CircularProgressIndicator(
                    modifier = Modifier.clickable {
                        isLoading.value = false
                    }
                )
            } else {
                AnimatedVisibility(visible = !imagesToShow.value.isNullOrEmpty()) {
                    Log.d("MYLOG", "~ ~ ~ KEK 4 ~ ~ ~")
                    Log.d("MYLOG", "imagesToShow.value: ${imagesToShow.value}")



                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = searchBarText,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    appViewModel.getImagesPrevious(
                                        offset = offset,
                                        limit = limit
                                    )

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "button previous"
                                )
                            }
                            Button(
                                onClick = {
                                    appViewModel.getImagesNext(
                                        search = inputText,
                                        offset = offset,
                                        limit = limit,
                                        context = context,
                                        inputText = inputText,
                                        isLoadingNextFromNetwork = isLoadingNextFromNetwork
                                    )

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "button next"
                                )
                            }
                        }


                        if (isLoadingNextFromNetwork.value) {
                            CircularProgressIndicator()
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 5.dp)
                            ) {
                                items(imagesToShow.value) { imagePath ->
                                    GifImage(
                                        imagePath = imagePath,
                                        modifier = Modifier.clickable {
                                            onDetailsClick()
                                        }
                                    )
                                    Log.d("MYLOG", "~ ~ ~ KEK 5 ~ ~ ~")
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    if (alertDialogNoInternetIsVisible.value) {
        AlertDialogNoInternet(
            alertDialogNoInternetIsVisible = alertDialogNoInternetIsVisible
        ) {
            appViewModel.getImages(
                search = inputText,
                offset = 0,
                limit = limit,
                context = context,
                alertDialogNoInternetIsVisible = alertDialogNoInternetIsVisible
            )
        }
    }
}

@Composable
fun GifImage(
    modifier: Modifier,
    imagePath: String
) {
//    val context = LocalContext.current
//    val imageLoader = ImageLoader.Builder(context)
//    val imageLoader = LocalImageLoader.current

    AsyncImage(
        model = imagePath,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.0f),
    )
}