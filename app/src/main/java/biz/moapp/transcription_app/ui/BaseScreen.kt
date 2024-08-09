package biz.moapp.transcription_app.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import biz.moapp.transcription_app.navigation.Nav
import biz.moapp.transcription_app.ui.common.TopBar
import biz.moapp.transcription_app.ui.common.bottombar.BottomBar
import biz.moapp.transcription_app.ui.main.MainScreen
import biz.moapp.transcription_app.ui.main.MainScreenViewModel
import biz.moapp.transcription_app.ui.summary.SummaryScreen

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BaseScreen(mainScreenViewModel: MainScreenViewModel) {
    val navController = rememberNavController()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = { TopBar(navController) }, bottomBar = { BottomBar(navController) }) { innerPadding ->
        NavHost(
            navController = navController, startDestination = Nav.MainScreen.name,
            enterTransition = {
                slideIn { fullSize -> IntOffset(fullSize.width, 0) }
            },
            popEnterTransition = {
                slideIn { fullSize -> IntOffset(-fullSize.width, 0) }
            },
            exitTransition = {
                slideOut { fullSize -> IntOffset(-fullSize.width, 0) }
            },
            popExitTransition = {
                slideOut { fullSize -> IntOffset(fullSize.width, 0) }
            },
        ) {
            composable(route = Nav.MainScreen.name,) {
                MainScreen(
                    Modifier.padding(innerPadding),
                    mainScreenViewModel,
                    navController
                )
            }
            composable(
                route = "${Nav.SummaryScreen.name}/{action}",
                arguments = listOf(navArgument("action") { type = NavType.StringType })
            ) { backStackEntry ->
                val action = backStackEntry.arguments?.getString("action")
                SummaryScreen(
                    Modifier.padding(innerPadding),
                    mainScreenViewModel,
                    action ?: "",
                    navController
                )
            }
        }
    }
}