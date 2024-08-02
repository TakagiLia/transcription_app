package biz.moapp.transcription_app.ui.main

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import biz.moapp.transcription_app.model.ChatCompletions
import biz.moapp.transcription_app.network.OpenAiAudioApi
import biz.moapp.transcription_app.network.RetrofitOpenAiNetwork
import biz.moapp.transcription_app.network.TranscriptionResponse
import biz.moapp.transcription_app.ui.state.MainUiState
import biz.moapp.transcription_app.ui.state.UIState
import biz.moapp.transcription_app.usecase.AudioUseCase
import biz.moapp.transcription_app.usecase.FirebaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel@Inject constructor(
    private val retrofitOpenAiNetwork: RetrofitOpenAiNetwork,
    private val openAiAudioApi: OpenAiAudioApi,
    private val ioDispatcher: CoroutineDispatcher,
    private val audioUseCase: AudioUseCase,
    private val firebaseUseCase: FirebaseUseCase
): ViewModel() {

    var uiState by mutableStateOf(MainUiState())
        private set

    private val _mainScreenUiState = MutableStateFlow<UIState<TranscriptionResponse>>(UIState.NotYet)
    val mainScreenUiState: StateFlow<UIState<TranscriptionResponse>> = _mainScreenUiState.asStateFlow()

    private val _audioText = MutableStateFlow<String>("")
    val audioText: StateFlow<String> = _audioText.asStateFlow()
    fun setAudioText(text : String){
        _audioText.value = text
    }

    private val _summaryText = MutableStateFlow<String>("")
    val summaryText: StateFlow<String> = _summaryText.asStateFlow()
    fun setSummaryText(text : String){
        _summaryText.value = text
    }

    var mediaPlayer: MediaPlayer? by mutableStateOf(null)

    fun summary(message: String){
        /**ローディング**/
        uiState = uiState.copy(sendResultState = MainUiState.SendResultState.Loading)
        viewModelScope.launch {
            try{
                val result = withContext(ioDispatcher) {
                    retrofitOpenAiNetwork.imageCompletions( retrofitOpenAiNetwork.createJson(message))
                }

                /**UIに反映**/
                uiState = when (result) {
                    /**成功時**/
                    is ChatCompletions.Response.Success -> {
                        result.choices.map { value -> value.message?.content?.let{ _summaryText.value = it} }

                        Log.d("--result response１-","${result.choices.map { it.message?.content }}")
                        uiState.copy(
                            sendResultState = MainUiState.SendResultState.Success(
                                result.choices.map { it.message?.content ?: "No Text...." }
                            )
                        )
                    }
                    /**失敗時時**/
                    is ChatCompletions.Response.Failure -> {
                        uiState.copy(
                            sendResultState = MainUiState.SendResultState.Error(
                                result.exception.message ?: "unknown error"
                            )
                        )
                    }
                }
            }catch(e : Exception){
                Log.d("--summary Error","Message:${e.message}",e)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun openAiAudioApi(filePath : String){
        viewModelScope.launch {
            _mainScreenUiState.value = UIState.Loading
            try{
                openAiAudioApi.completions(filePath)?.let { response ->
                    _mainScreenUiState.value = UIState.Success(response)
                    _audioText.value = response.text
                }

            }catch(e:Exception){
                _mainScreenUiState.value = UIState.Error(e.message ?: "Unknown error")
                    Log.d("--openAiAudioApi Error","Message:${e.message}",e)
            }
        }
    }

    fun recordingStart(recorder: MediaRecorder, filePath : String) : MediaRecorder{
        return audioUseCase.recordingStart(recorder,filePath)
    }

    fun recordingStop(recorder: MediaRecorder){
        audioUseCase.recordingStop(recorder)
    }

    fun audioPlay(filePath : String) : MediaPlayer?{
        mediaPlayer = audioUseCase.audioPlay(filePath)
        return mediaPlayer
    }

    fun audioStop(){
        mediaPlayer?.let { audioUseCase.audioStop(it) }
    }

    fun summarySave(summary : String){

        viewModelScope.launch {
            try{
                firebaseUseCase.saveSummary(summary)
            }catch(ex : Exception){
                Log.d("--summarySave","Failure: ${ex.message}",ex)
            }
        }
    }
}