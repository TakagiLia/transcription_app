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
import biz.moapp.transcription_app.ui.compose.EditField
import biz.moapp.transcription_app.ui.state.MainUiState
import biz.moapp.transcription_app.ui.state.UIState
import biz.moapp.transcription_app.ui.compose.OperationButton
import kotlinx.coroutines.delay


@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(modifier : Modifier, mainScreenViewModel: MainScreenViewModel,){

    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var isAudioButtonVisible by remember { mutableStateOf(true) }
    var isAudioPlayButtonVisible by remember { mutableStateOf(false) }
    var isEditable by remember { mutableStateOf(false) }
    val convertTextButtonState = remember {
        MutableTransitionState(true).apply {
            targetState = false
        }
    }
    val convertTextAreaState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    val summaryAreaState = remember {
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

    /**再生　時間を一秒ずつカウントアウト**/
    LaunchedEffect(key1 = isPlaying) {
        Log.d("--isPlaying", "recordedTime；$recordedTime")
        while (isPlaying && recordedTime > 0) {
            delay(1000)
            recordedTime -= 1000
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

        /**要約時の結果表示**/
        when (mainScreenViewModel.uiState.sendResultState) {
            is MainUiState.SendResultState.NotYet -> Unit
            is MainUiState.SendResultState.Loading -> {
                CircularProgressIndicator()
            }
            is MainUiState.SendResultState.Success -> {
                (mainScreenViewModel.uiState.sendResultState as MainUiState.SendResultState.Success).results.map { value ->
                    Log.d("--result response：　", value)
                    AnimatedVisibility(visibleState = summaryAreaState) {
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
                                    text = "要約した内容",
                                    color = systemColor,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(4.dp)
                                )
                                EditField(mainScreenViewModel,isEditable)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {

                                /**編集ボタン**/
                                OperationButton(
                                    modifier = Modifier.weight(0.5f),
                                    buttonName = if(!isEditable) "Edit Text" else "Change Text",
                                    clickAction = {
                                        isEditable = !isEditable
                                    }
                                )

                                /**リセットボタン**/
                                OperationButton(
                                    modifier = Modifier.weight(0.5f),
                                    buttonName = "Reset",
                                    clickAction = {
                                        isAudioButtonVisible = true
                                        /**要約エリア非表示**/
                                        summaryAreaState.targetState = !summaryAreaState.currentState
                                    }
                                )
                            }
                            /**要約した内容の保存**/
                            OperationButton(
                                modifier = maxModifierButton,
                                buttonName = "save",
                                clickAction = { mainScreenViewModel.summarySave(value) }
                            )
                        }
                    }
                }
            }
            is MainUiState.SendResultState.Error -> {}
        }

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
                                mainScreenViewModel.summary(mainScreenViewModel.audioText.value)
                                /**文字起こしエリア非表示**/
                                convertTextAreaState.targetState = !convertTextAreaState.currentState
                                /**要約エリア表示**/
                                summaryAreaState.targetState = !summaryAreaState.currentState
                            }
                        )
                    }
                }
            }
            is UIState.Error -> {Text(text = "Error: ${(mainUiState as UIState.Error).message}")}
        }

        /**テキスト変換ボタン**/
        AnimatedVisibility(visibleState = convertTextButtonState) {
            OperationButton(
                modifier = maxModifierButton,
                buttonName = "Convert Text",
                clickAction = {
                    mainScreenViewModel.openAiAudioApi(filePath)
                    /**レコーディング操作ボタン非表示**/
                    isAudioButtonVisible = false
                    /**オーディオ操作ボタン非表示**/
                    isAudioPlayButtonVisible = false
                    /**テキスト変換ボタン非表示**/
                    convertTextButtonState.targetState = !convertTextButtonState.currentState
                    /**文字起こしエリア表示**/
                    convertTextAreaState.targetState = !convertTextAreaState.currentState
                }
            )
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
                        modifier = Modifier.weight(0.5f),
                        buttonName = if (!isRecording) "Recording Start" else " Recording Stop",
                        enabled = isAudioButtonVisible,
                        clickAction = {
                            isRecording = !isRecording
                            if (isRecording) {
                                /**Convert Textボタン非表示**/
                                convertTextButtonState.targetState = false
                                /**録音時間リセット**/
                                recordedTime = 0L
                                mainScreenViewModel.recordingStart(recorder,filePath)
                            } else {
                                /**Convert Textボタン表示**/
                                convertTextButtonState.targetState = true
                                /**オーディオ操作ボタン表示**/
                                isAudioPlayButtonVisible = true
                                /**レコーディング停止**/
                                mainScreenViewModel.recordingStop(recorder)
                            }
                        })

                    /**オーディオ操作ボタン**/
                    OperationButton(
                        modifier = Modifier.weight(0.5f),
                        buttonName = if (!isPlaying) "Audio Play" else "Audio Stop",
                        enabled = isAudioPlayButtonVisible,
                        clickAction = {
                            isPlaying = !isPlaying
                            /**Convert Textボタン非表示**/
                            convertTextButtonState.targetState = false
                            if(isPlaying){
                                val mediaPlayer = mainScreenViewModel.audioPlay(filePath)
                                /**再生時間を取得**/
                                mediaPlayer?.duration?.let {
                                    if(recordedTime <= 0L){
                                        recordedTime =  it.toLong()
                                    }
                                }
                            }else{
                                /**Convert Textボタン表示**/
                                convertTextButtonState.targetState = true
                                mainScreenViewModel.audioStop()
                            }
                        }
                    )
                }
            }
        }
    }
}