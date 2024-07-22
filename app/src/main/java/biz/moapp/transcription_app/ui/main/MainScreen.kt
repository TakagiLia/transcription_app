package biz.moapp.transcription_app.ui.theme

import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import biz.moapp.transcription_app.ui.theme.compose.OperationButton

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(modifier : Modifier, mainScreenViewModel: MainScreenViewModel){
    var speechStatus = remember { mutableStateOf("No Speech") }
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val recorder = remember { MediaRecorder(context) }
    val filePath : String = context.getExternalFilesDir(null)?.absolutePath + "/recording.m4a"
    val mainUiState by mainScreenViewModel.mainScreenUiState.collectAsState()

    val maxModifierButton : Modifier = Modifier.fillMaxWidth().height(80.dp)

    /**UI**/
    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top) {
        Text(text = speechStatus.value, color = Color.Magenta)
    }

    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /**要約時の結果表示**/
        when (mainScreenViewModel.uiState.sendResultState) {
            is MainUiState.SendResultState.NotYet -> Unit
            is MainUiState.SendResultState.Loading -> {
                CircularProgressIndicator()
            }
            is MainUiState.SendResultState.Success -> {
                (mainScreenViewModel.uiState.sendResultState as MainUiState.SendResultState.Success).results.map { value ->
                    Log.d("--result response：　",value)
                    Text(text = value, color = Color.White)
                }
            }
            is MainUiState.SendResultState.Error -> {}
        }

        /**音声をテキスト変換時の結果表示**/
        when(mainUiState){
            is UIState.NotYet -> {}
            is UIState.Loading -> {CircularProgressIndicator()}
            is UIState.Success -> {
                OutlinedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier
                            .fillMaxSize(),
                ) {
                    Text(text = mainScreenViewModel.transcriptionText,color = Color.White)
                }

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){

                    /**要約ボタン**/
                    OperationButton(
                        modifier = Modifier.weight(0.5f),
                        buttonName = "Summary Text",
                        clickAction = {
                            mainScreenViewModel.summary(mainScreenViewModel.transcriptionText)
                        }
                    )

                    /**編集ボタン**/
                    OperationButton(
                        modifier = Modifier.weight(0.5f),
                        buttonName = "Edit Text",
                        clickAction = {

                        }
                    )
                }
            }
            is UIState.Error -> {Text(text = "Error: ${(mainUiState as UIState.Error).message}")}
        }

        /**テキスト変換ボタン**/
        OperationButton(
            modifier = maxModifierButton,
            buttonName = "Convert Text",
            clickAction = { mainScreenViewModel.openAiAudioApi(filePath) }
        )

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            /**レコード操作ボタン**/
            OperationButton(
                modifier = Modifier.weight(0.5f),
                buttonName = if (!isRecording) "Recording Start" else " Recording Stop",
                clickAction = {
                    isRecording = !isRecording
                    if (isRecording) {
                        mainScreenViewModel.recordingStart(recorder)
                    }else{
                        mainScreenViewModel.recordingStop(recorder)
                    }
                })

            /**オーディオ操作ボタン**/
            OperationButton(
                modifier = Modifier.weight(0.5f),
                buttonName = if (!isPlaying) "Audio Play" else "Audio Stop",
                clickAction = {
                    mainScreenViewModel.audioPlay()
                }
            )
        }
    }
}