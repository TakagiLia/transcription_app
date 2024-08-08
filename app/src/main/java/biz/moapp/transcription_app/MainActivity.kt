package biz.moapp.transcription_app

import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import biz.moapp.transcription_app.navigation.Nav
import biz.moapp.transcription_app.ui.main.MainScreen
import biz.moapp.transcription_app.ui.main.MainScreenViewModel
import biz.moapp.transcription_app.ui.summary.SummaryScreen
import biz.moapp.transcription_app.ui.theme.Transcription_appTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        /**アプリが起動している間は画面がロックされないようにしている**/
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            val mainScreenViewModel: MainScreenViewModel by viewModels()

            Transcription_appTheme {

                val granted = ContextCompat.checkSelfPermission(this, RECORD_AUDIO)

                if (granted != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(RECORD_AUDIO),
                        PERMISSIONS_RECORD_AUDIO
                    )
                }
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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
                                mainScreenViewModel,
                                navController
                            )
                        }
                        composable(route = "${Nav.SummaryScreen.name}/{action}",
                            arguments = listOf(navArgument("action"){ type = NavType.StringType})
                        ) {backStackEntry ->
                            val action = backStackEntry.arguments?.getString("action")
                            SummaryScreen(
                                mainScreenViewModel,
                                action ?: "",
                                navController
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private const val PERMISSIONS_RECORD_AUDIO = 1000
    }
}
