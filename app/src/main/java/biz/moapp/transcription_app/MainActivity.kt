package biz.moapp.transcription_app

import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import biz.moapp.transcription_app.ui.BaseScreen
import biz.moapp.transcription_app.ui.main.MainScreenViewModel
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

        /**Homeの戻るの制御**/
        onBackPressedDispatcher.addCallback(callback)

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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BaseScreen(mainScreenViewModel)
                }
            }
        }
    }

    private val callback = object : OnBackPressedCallback(true) {
        /**handleOnBackPressedを呼び出して、戻るキーを押したときの処理を記述**/
        override fun handleOnBackPressed() {
            /**何も記述しないのでハードの戻るボタンで戻らない**/
            return
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private const val PERMISSIONS_RECORD_AUDIO = 1000
    }
}
