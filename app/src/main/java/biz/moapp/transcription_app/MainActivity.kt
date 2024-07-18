package biz.moapp.transcription_app

import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import biz.moapp.transcription_app.ui.theme.MainScreen
import biz.moapp.transcription_app.ui.theme.MainScreenViewModel
import biz.moapp.transcription_app.ui.theme.Transcription_appTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var speechRecognizer : SpeechRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val mainScreenViewModel: MainScreenViewModel by viewModels()
            Transcription_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val granted = ContextCompat.checkSelfPermission(this, RECORD_AUDIO)

                    if (granted != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(RECORD_AUDIO), PERMISSIONS_RECORD_AUDIO)
                    }
                    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)

                    MainScreen(modifier = Modifier.padding(innerPadding), speechRecognizer,mainScreenViewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
    }

    companion object {
        private const val PERMISSIONS_RECORD_AUDIO = 1000
    }
}
