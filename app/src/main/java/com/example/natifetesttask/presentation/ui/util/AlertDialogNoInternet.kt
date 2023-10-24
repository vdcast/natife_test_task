package com.example.natifetesttask.presentation.ui.util

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.natifetesttask.R

@Composable
fun AlertDialogNoInternet(
    alertDialogNoInternetIsVisible: MutableState<Boolean>,
    tryAgain: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = stringResource(id = R.string.no_internet_title)) },
        text = {
            Text(
                text = stringResource(
                    id = R.string.no_internet_description,
                ),
                fontSize = 16.sp
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    tryAgain()
                    alertDialogNoInternetIsVisible.value = false
                }
            ) { Text(stringResource(id = R.string.no_internet_confirm)) }
        },
        dismissButton = {
            Button(
                onClick = {
                    alertDialogNoInternetIsVisible.value = false
                }
            ) { Text(text = stringResource(id = R.string.no_internet_dismiss)) }
        }
    )
}