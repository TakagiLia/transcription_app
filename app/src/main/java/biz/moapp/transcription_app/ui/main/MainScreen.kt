package biz.moapp.transcription_app.ui.main

import android.annotation.SuppressLint
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import biz.moapp.transcription_app.AppUtils
import biz.moapp.transcription_app.R
import biz.moapp.transcription_app.navigation.Nav
import biz.moapp.transcription_app.ui.compose.OperationButton
import biz.moapp.transcription_app.ui.compose.RecordingButton
import biz.moapp.transcription_app.ui.state.UIState
import kotlinx.coroutines.delay


@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(modifier : Modifier, mainScreenViewModel: MainScreenViewModel, navController: NavHostController){

    var isRecording by remember { mutableStateOf(false) }
    var isRecordingPause by remember { mutableStateOf(true) }
    var isRecordingComplete by remember { mutableStateOf(false) }
    val convertTextAreaState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    val context = LocalContext.current
    var recorder = remember { MediaRecorder(context) }
    val filePath : String = context.getExternalFilesDir(null)?.absolutePath + "/recording.m4a"
    val mainUiState by mainScreenViewModel.mainScreenUiState.collectAsState()
    val systemColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    var recordingHelpText by remember { mutableStateOf("") }
    var completeHelpText by remember { mutableStateOf("") }

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


    /**画面サイズの取得**/
    BoxWithConstraints {
        val width = maxWidth
        val height = maxHeight

        /**UI**/
        Column(
            modifier = modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /**音声をテキスト変換時の結果表示**/
            when (mainUiState) {
                is UIState.NotYet -> {
                    if (isRecording) {
                        recordingHelpText = stringResource(R.string.recording_help_stop)
                    } else {
                        recordingHelpText = stringResource(R.string.recording_help_start)
                    }
                    completeHelpText = if(isRecordingComplete) stringResource(R.string.recording_help_complete) else ""
                    Column(modifier = modifier.padding(top = (width * 0.4f),),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = recordingHelpText, textAlign = TextAlign.Center)
                            Text(text = completeHelpText,textAlign = TextAlign.Center)
                    }
                }

                is UIState.Loading -> {
                    Column(modifier = modifier.padding(top = (width * 0.4f),)) {
                        CircularProgressIndicator()
                    }
                }

                is UIState.Success -> {
                    AnimatedVisibility(visibleState = convertTextAreaState) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Top,
                        ) {

                            Spacer(modifier = Modifier.height(24.dp))

                            OutlinedCard(
                                border = BorderStroke(1.dp, systemColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {

                                Text(
                                    text = stringResource(R.string.recording_content_title),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(4.dp)
                                )

                                Spacer(modifier = Modifier.height(1.dp))

                                Text(
                                    text = mainScreenViewModel.audioText.value,
                                    style = TextStyle.Default.copy(lineBreak = LineBreak.Paragraph),
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.Bottom,
                            ) {
                            }
                        }
                    }
                }

                is UIState.Error -> {
                    Text(text = "Error: ${(mainUiState as UIState.Error).message}")
                }
            }
        }
    }

    Column(modifier = modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally) {

        /**再生録音タイマー表示**/
        Text(
            text = AppUtils.formatTime(recordedTime),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            /**レコーディング操作ボタン（New）**/
            RecordingButton(
                isEnable = isRecording,
                buttonName = if (!isRecording) stringResource(R.string.recording_start) else stringResource(
                    R.string.recording_stop
                ),
                icon = if (isRecording) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                onToggle = { newIsRecording ->
                    isRecording = newIsRecording
                    if (isRecording) {
                        if (isRecordingPause) {
                            Log.d("--recording", "Initial Start")
                            /**録音時間リセット**/
                            recordedTime = 0L
                            /**テキスト変換エリア非表示**/
                            convertTextAreaState.targetState = false
                            recorder = mainScreenViewModel.recordingStart(recorder, filePath)
                        } else {
                            Log.d("--recording", "Re Start")
                            /**レコーディング再開**/
                            recorder.resume()
                            /**レコードをポーズではない状態にする**/
                            isRecordingPause = !isRecordingPause
                            /**レコード完了ボタンを非活性**/
                            isRecordingComplete = !isRecordingComplete
                        }
                    } else {
                        Log.d("--recording", "Stop")
                        /**レコーディング一時停止**/
                        recorder.pause()
                        /**レコードをポーズにする**/
                        isRecordingPause = !isRecordingPause
                        /**レコード完了ボタンを活性**/
                        isRecordingComplete = !isRecordingComplete
                    }
                },
            )

            Spacer(modifier = Modifier.width(16.dp))

            /**録音完了(New)**/
            RecordingButton(isEnable = isRecordingComplete,
                buttonName = stringResource(R.string.recording_complete),
                icon = Icons.Filled.Stop,
                onToggle = {
                    /**レコーディング停止**/
                    mainScreenViewModel.recordingStop(recorder)
                    /**録音した内容を文字起こし**/
                    mainScreenViewModel.openAiAudioApi(filePath)
                    /**文字起こしエリア表示**/
                    convertTextAreaState.targetState = true
                    /**ボタンのフラグを元に戻す**/
                    isRecording = false
                    isRecordingPause = true
                    isRecordingComplete = false
                }
            )
        }
            /**要約ボタン**/
            OperationButton(
                modifier = maxModifierButton,
                buttonName = stringResource(R.string.recording_summarize),
                enabled = convertTextAreaState.currentState,
                clickAction = {
                    /**要約表示画面に遷移**/
//                mainScreenViewModel.summary(mainScreenViewModel.audioText.value,/*navController*/)
                    navController.navigate("${Nav.SummaryScreen.name}/summarize")
                }
            )
    }
}