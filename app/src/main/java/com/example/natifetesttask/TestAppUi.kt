package com.example.natifetesttask

import android.app.Application
import android.os.Build.VERSION.SDK_INT
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.example.natifetesttask.domain.AppViewModel
import com.example.natifetesttask.presentation.ui.navigation.Routes
import com.example.natifetesttask.presentation.ui.screens.GifDetails
import com.example.natifetesttask.presentation.ui.screens.Home
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class TestApp : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder()
            .memoryCachePolicy(CachePolicy.DISABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.1)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.03)
                    .directory(cacheDir)
                    .build()
            }
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .logger(DebugLogger())
            .build()
    }

}

@Composable
fun TestAppUi(
    appViewModel: AppViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {
        composable(Routes.Home.route) {
            Home(
                appViewModel = appViewModel,
                onDetailsClick = {
                    navController.navigate(Routes.Details.route)
                }
            )
        }
        composable(Routes.Details.route) {
            GifDetails(
                appViewModel = appViewModel,
                onCLose = { navController.popBackStack() }
            )
        }

    }
}