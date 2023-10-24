package com.example.natifetesttask.presentation.ui.navigation

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Details : Routes("details")
}
