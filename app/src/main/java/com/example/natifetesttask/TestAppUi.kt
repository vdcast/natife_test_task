package com.example.natifetesttask

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.natifetesttask.presentation.ui.navigation.Routes
import com.example.natifetesttask.presentation.ui.screens.Home
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class TestApp : Application() {

}

@Composable
fun TestAppUi(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {
        composable(Routes.Home.route) {
            Home(
//                onGameCLick = { navController.navigate(Routes.Game.route) },
//                onRecordsClick = { navController.navigate(Routes.Records.route) },
//                onAboutClick = { navController.navigate(Routes.About.route) },
            )
        }

    }
}