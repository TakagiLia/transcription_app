package biz.moapp.transcription_app

import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import biz.moapp.transcription_app.ui.theme.MainScreen
import biz.moapp.transcription_app.ui.theme.Transcription_appTheme

class MainActivity : ComponentActivity() {

    private var speechRecognizer : SpeechRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Transcription_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val granted = ContextCompat.checkSelfPermission(this, RECORD_AUDIO)

                    if (granted != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(RECORD_AUDIO), PERMISSIONS_RECORD_AUDIO)
                    }
                    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)

                    MainScreen(modifier = Modifier.padding(innerPadding), speechRecognizer)
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
