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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import biz.moapp.transcription_app.navigation.Nav
import biz.moapp.transcription_app.ui.common.TopBar
import biz.moapp.transcription_app.ui.common.bottombar.BottomBar
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
                    ActivityCompat.requestPermissions(this, arrayOf(RECORD_AUDIO), PERMISSIONS_RECORD_AUDIO)
                }
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = { TopBar(navController) }, bottomBar = { BottomBar(navController) }) { innerPadding ->
                    NavHost(navController = navController, startDestination = Nav.MainScreen.name) {
                        composable(route = Nav.MainScreen.name) {
                            MainScreen(
                                modifier = Modifier.padding(innerPadding), mainScreenViewModel, navController)
                        }
                        composable(route = Nav.SummaryScreen.name) {
                            SummaryScreen(modifier = Modifier.padding(innerPadding), mainScreenViewModel)
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
