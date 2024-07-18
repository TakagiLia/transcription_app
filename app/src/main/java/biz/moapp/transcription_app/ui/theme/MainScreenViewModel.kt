package biz.moapp.transcription_app.ui.theme

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import biz.moapp.transcription_app.model.ChatCompletions
import biz.moapp.transcription_app.network.RetrofitOpenAiNetwork
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
    private val ioDispatcher: CoroutineDispatcher,
): ViewModel() {

    var uiState by mutableStateOf(MainUiState())
        private set

    fun Summary(message: String){
        viewModelScope.launch {
            try{
                val result = withContext(ioDispatcher) {
                    retrofitOpenAiNetwork.imageCompletions( retrofitOpenAiNetwork.createJson(message))
                }

                /**UIに反映**/
                uiState = when (result) {
                    /**成功時**/
                    is ChatCompletions.Response.Success -> {
                        Log.d("--result response１-","${result.choices.map { it.message?.content }}")
                        uiState.copy(
                            sendResultState = MainUiState.SendResultState.Success(
                                result.choices.map { it.message?.content ?: "No Text...."}
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
                Log.d("--capture Error","Message:${e.message}",e)
            }
        }
    }
}