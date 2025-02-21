/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.gogoma.presentation

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.gogoma.GlobalApplication
import com.example.gogoma.presentation.navigation.AppNavigation
import com.example.gogoma.presentation.screens.KeepScreenOn
import com.example.gogoma.presentation.theme.GogomaWatchTheme
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel

class MainActivity : FragmentActivity() {
    val marathonDataViewModel: MarathonDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            val navController = rememberNavController()

            (application as? GlobalApplication)?.navController = navController

            KeepScreenOn(activity = this)

            GogomaWatchTheme {
                AppNavigation(navController = navController, marathonDataViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
    }
}

@Composable
fun KeepScreenOn(activity: Activity) {
    DisposableEffect(activity) {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
//    AppNavigation(navController = navController)
}