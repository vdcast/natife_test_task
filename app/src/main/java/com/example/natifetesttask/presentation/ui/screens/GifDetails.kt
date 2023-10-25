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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.natifetesttask.R
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

    val pagerState = rememberPagerState(
        pageCount = {
            imagesToShowDetails.size
        },
        initialPage = selectedImageIndex
    )

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
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_xlarge)))
            Text("Page: $page")
            Icon(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.padding_xlarge))
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
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            onClick = { onDelete() },
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium)),
            elevation = ButtonDefaults.buttonElevation(dimensionResource(id = R.dimen.padding_xsmall)),
            colors = ButtonDefaults.buttonColors(Red20)
        ) {
            Text(
                text = stringResource(id = R.string.delete_gif),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}