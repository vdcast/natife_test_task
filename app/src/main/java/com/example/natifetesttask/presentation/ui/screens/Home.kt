package com.example.natifetesttask.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.example.natifetesttask.R
import com.example.natifetesttask.domain.AppViewModel
import com.example.natifetesttask.presentation.ui.util.AlertDialogNoInternet
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    appViewModel: AppViewModel,
    onDetailsClick: () -> Unit
) {
    val snackbarEmptyInput = stringResource(id = R.string.snackbar_input_empty)
    val snackbarOfflineMode = stringResource(id = R.string.snackbar_offline_mode)
    val context = LocalContext.current
    val imagesToShow = appViewModel.imagesToShowGrid.collectAsState()
    val inputText by appViewModel.inputText.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var searchBarText by remember { mutableStateOf("") }
    var limit by remember { mutableStateOf(25) }
    val offset = remember { mutableStateOf(0) }

    val buttonGetImagesEnabled = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(false) }
    val isLoadingNextFromNetwork = remember { mutableStateOf(false) }
    val alertDialogNoInternetIsVisible = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
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
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small)),
                value = inputText,
                onValueChange = { newValue ->
                    appViewModel.updateInputText(newValue)
                }
            )
            Button(
                modifier = Modifier,
                enabled = buttonGetImagesEnabled.value,
                onClick = {
                    searchBarText = inputText
                    if (!inputText.isNullOrBlank() || !inputText.isNullOrEmpty()) {
                        appViewModel.getImages(
                            search = inputText,
                            offset = 0,
                            limit = limit,
                            context = context,
                            alertDialogNoInternetIsVisible = alertDialogNoInternetIsVisible,
                            buttonGetImagesEnabled = buttonGetImagesEnabled
                        )
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(snackbarEmptyInput)
                        }
                    }
                }
            ) { Text(text = stringResource(id = R.string.get_images)) }

            if (isLoading.value) {
                if (!buttonGetImagesEnabled.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.clickable {
                            isLoading.value = false
                        }
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.home_hint_on_screen),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                AnimatedVisibility(visible = !imagesToShow.value.isNullOrEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        AnimatedVisibility(visible = !searchBarText.isNullOrEmpty()) {
                            Text(
                                text = searchBarText,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = dimensionResource(id = R.dimen.padding_small)
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                enabled = !isLoadingNextFromNetwork.value && !isLoading.value,
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
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))
                            Button(
                                enabled = !isLoadingNextFromNetwork.value && !isLoading.value,
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_five)),
                                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_five)),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = dimensionResource(id = R.dimen.padding_five))
                            ) {
                                items(imagesToShow.value) { imagePath ->
                                    GifImage(
                                        imagePath = imagePath,
                                        modifier = Modifier.clickable {
                                            appViewModel.updateImagesToShowDetails(
                                                context = context,
                                                onDetailsClick = {
                                                    onDetailsClick()
                                                },
                                                onShowNoInternetAlert = {
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(snackbarOfflineMode)
                                                    }
                                                },
                                                id = imagesToShow.value.indexOf(imagePath) + offset.value
                                            )
                                        }
                                    )
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
                alertDialogNoInternetIsVisible = alertDialogNoInternetIsVisible,
                buttonGetImagesEnabled = buttonGetImagesEnabled
            )
        }
    }
}

@Composable
fun GifImage(
    modifier: Modifier,
    imagePath: String
) {
    AsyncImage(
        model = imagePath,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.0f),
    )
}