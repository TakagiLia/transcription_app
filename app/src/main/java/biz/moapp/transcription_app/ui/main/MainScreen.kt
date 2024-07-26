package biz.moapp.transcription_app.ui.main

import android.annotation.SuppressLint
import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import biz.moapp.transcription_app.AppUtils
import biz.moapp.transcription_app.ui.compose.OperationButton
import biz.moapp.transcription_app.ui.state.UIState
import kotlinx.coroutines.delay


@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(modifier : Modifier, mainScreenViewModel: MainScreenViewModel, onNavigateToSummary: () -> Unit){

    var isRecording by remember { mutableStateOf(false) }
    var isAudioButtonVisible by remember { mutableStateOf(true) }
    var isAudioPlayButtonVisible by remember { mutableStateOf(false) }
    val convertTextAreaState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    val context = LocalContext.current
    val recorder = remember { MediaRecorder(context) }
    val filePath : String = context.getExternalFilesDir(null)?.absolutePath + "/recording.m4a"
    val mainUiState by mainScreenViewModel.mainScreenUiState.collectAsState()
    val systemColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    /**カウントした秒数をもつ**/
    var recordedTime by remember { mutableLongStateOf(0L) }

    /**録音　時間を一秒ずつカウントアップ**/
    LaunchedEffect(key1 = isRecording) {
        while (isRecording) {
            delay(1000)
            recordedTime += 1000
        }
    }

    val maxModifierButton : Modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)

    /**UI**/
    Column(modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /**音声をテキスト変換時の結果表示**/
        when(mainUiState){
            is UIState.NotYet -> {}
            is UIState.Loading -> {CircularProgressIndicator()}
            is UIState.Success -> {
                AnimatedVisibility(visibleState = convertTextAreaState) {
                Spacer(modifier = Modifier.height(5.dp))
                    Column {
                        OutlinedCard(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                            ),
                            border = BorderStroke(1.dp, systemColor),
                            modifier = Modifier
                                .fillMaxSize(),
                        ) {

                            Text(
                                text = "録音した内容",
                                color = systemColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(4.dp)
                            )

                            Spacer(modifier = Modifier.height(1.dp))

                            Text(
                                text = mainScreenViewModel.audioText.value,
                                color = systemColor,
                                style = TextStyle.Default.copy(lineBreak = LineBreak.Paragraph),
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                        /**要約ボタン**/
                        OperationButton(
                            modifier = maxModifierButton,
                            buttonName = "Summary Text",
                            clickAction = {
                                /**要約表示画面に遷移**/
                                onNavigateToSummary()
                            }
                        )
                    }
                }
            }
            is UIState.Error -> {Text(text = "Error: ${(mainUiState as UIState.Error).message}")}
        }

        AnimatedVisibility(isAudioButtonVisible) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                 /**再生録音タイマー表示**/
                Text(text = AppUtils.formatTime(recordedTime),
                    color = systemColor,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    /**レコーディング操作ボタン**/
                    OperationButton(
                        modifier =maxModifierButton,
                        buttonName = if (!isRecording) "Recording Start" else " Recording Stop",
                        enabled = isAudioButtonVisible,
                        clickAction = {
                            isRecording = !isRecording
                            if (isRecording) {
                                /**録音時間リセット**/
                                recordedTime = 0L
                                mainScreenViewModel.recordingStart(recorder,filePath)
                            } else {
                                /**オーディオ操作ボタン表示**/
                                isAudioPlayButtonVisible = true
                                /**レコーディング停止**/
                                mainScreenViewModel.recordingStop(recorder)
                                /**録音した内容を文字起こし**/
                                mainScreenViewModel.openAiAudioApi(filePath)
                                /**文字起こしエリア表示**/
                                convertTextAreaState.targetState = !convertTextAreaState.currentState
                            }
                        }
                    )
                }
            }
        }
    }
}