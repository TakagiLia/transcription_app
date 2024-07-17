package biz.moapp.transcription_app.ui.theme

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(modifier : Modifier, speechRecognizer : SpeechRecognizer?){
    var speechText = remember { mutableStateOf("Your speech will appear here.") }
    var speechStatus = remember { mutableStateOf("No Speech") }
    var isRecording by remember { mutableStateOf(false) }

    /**speechRecognizerの独自実装　オブジェクト式(object: 無名オブジェクト)　いちいちクラス作ったりしなくても実装可能**/
    /**インターフェースを実装するオブジェクトを簡潔かつ柔軟に生成可能。特定のメソッドのみに処理を追加したい場合や、オブジェクトを一時的に使用したい場合に効果的**/
    speechRecognizer?.setRecognitionListener(object : RecognitionListener {
        override fun onRmsChanged(rmsdB: Float) {
            /**音声の入力レベル（dB）の変化をUIに表示**/
//            Log.d("SpeechRecognizer", "RMS dB changed: $rmsdB")
        }
        override fun onReadyForSpeech(params: Bundle?) {
            /**1 音声認識の準備ができたことをUIに表示**/
            Log.d("--SpeechRecognizer", "onReadyForSpeech:${params}")
            speechStatus.value = "Ready for Speech!"
        }
        override fun onBeginningOfSpeech() {
            /**2 音声入力が開始されたことをUIに表示**/
            Log.d("--SpeechRecognizer", "onBeginningOfSpeech")
            speechStatus.value = "Start Speech!"
        }
        override fun onBufferReceived(buffer: ByteArray?) {
            /**音声データのバッファを受け取った際の処理**/
        }
        override fun onEndOfSpeech() {
            /**3 音声入力が終了したことをUIに表示**/
            Log.d("--SpeechRecognizer", "onEndOfSpeech")
            speechStatus.value = "finished Speech!"
        }
        override fun onError(error: Int) { /*onResult("onError")*/ }
        override fun onResults(results: Bundle?) {
            Log.d("--SpeechRecognizer", "onResults${results}")
            speechStatus.value = "Result Speech!"
            /**4 音声認識の結果を取得 UIに反映**/
            val stringArray = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            stringArray?.let {
                speechText.value = it[0] // 認識結果を UI に反映
            }
        }
        override fun onPartialResults(partialResults: Bundle?) {
            /**部分的な音声認識の結果を取得し、UIに表示**/
            Log.d("--SpeechRecognizer", "onPartialResults:${partialResults}")
            speechStatus.value = "Partial Speech!"
        }
        override fun onEvent(eventType: Int, params: Bundle?) {
            /**その他のイベントが発生した際の処理**/
        }
    })

    /**UI**/
    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top) {
        Text(text = speechStatus.value, color = Color.Magenta)
    }

    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {

        Text(text = speechText.value)

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Button(modifier = Modifier
                .weight(0.5f)
                .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black),
                enabled = !isRecording,
                onClick = {
                    Log.d("--Button","start")
                    if (!isRecording) {
                        speechRecognizer?.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH))
                        isRecording = true
                    }
                }) {
                Text(modifier = Modifier.padding(8.dp),text = "Start")
            }
            Button(modifier = Modifier
                .weight(0.5f)
                .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black),
                enabled = isRecording,
                onClick = {
                    Log.d("--Button","stop")
                    if (isRecording) {
                        speechRecognizer?.stopListening()
                        isRecording = false
                    }
                }) {
                Text(modifier = Modifier.padding(8.dp),text = "Stop")
            }
        }
    }
}