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

    val mockText = "最近、少子高齢化が深刻化してるってニュースでよく見るけど、実際どんな問題があるんだろう？そうだな、まず少子化は労働力不足を引き起こす。将来の年金や社会保障制度の維持も難しくなるし、経済成長も鈍化する恐れがある。私は高齢者福祉の現場にいるけど、介護が必要な高齢者が増える一方で、介護人材が不足してるのが深刻な問題だよ。確かに、ニュースで見たことある。じゃあ、どんな対策が必要なのかな？国や自治体では、子育て支援策を充実させて出生率を上げる取り組みをしてる。例えば、児童手当の拡充や保育サービスの充実とかね。だけど、経済的な支援だけでは解決できない問題もあると思う。子育てしやすい社会の雰囲気づくりも大切なんじゃないかな。具体的にはどんなこと？例えば、育児休暇を取りやすい職場環境を作ったり、地域で子育てをサポートする仕組みを作ったりすることかな。そうだな。あとは、若い世代が将来に希望を持てる社会にすることも重要だ。安定した雇用や結婚、子育てをしやすい環境を整える必要がある。高齢化についてはどうすればいいんだろう？高齢者が安心して暮らせる社会にするためには、介護サービスの充実や住みやすい街づくりが欠かせない。それと同時に、高齢者が社会参加できる機会を増やすことも大切だ。健康寿命を延ばして、元気な高齢者が活躍できる社会を目指すべきだと思う。なるほど、少子高齢化って複雑な問題なんだね。でも、みんなで協力して解決していく必要があるんだと感じたよ。その通りだ。少子高齢化は日本社会全体で取り組むべき課題だからね。私たち一人ひとりができることから始めて、未来のためにより良い社会を作っていきたいね。うん、私も自分にできることを考えて行動してみようと思う。今日は貴重な話を聞かせてくれてありがとう。こちらこそ、ありがとう。"

    private val _mainScreenUiState = MutableStateFlow<UIState<TranscriptionResponse>>(UIState.NotYet)
    val mainScreenUiState: StateFlow<UIState<TranscriptionResponse>> = _mainScreenUiState.asStateFlow()

    var transcriptionText by mutableStateOf("")
    var mediaPlayer: MediaPlayer? by mutableStateOf(null)

    fun summary(message: String){
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
                    transcriptionText = response.text
                }

            }catch(e:Exception){
                _mainScreenUiState.value = UIState.Error(e.message ?: "Unknown error")
                    Log.d("--openAiAudioApi Error","Message:${e.message}",e)
            }
        }
    }


    fun recordingStart(recorder: MediaRecorder, filePath : String){
        audioUseCase.recordingStart(recorder,filePath)
    }

    fun recordingStop(recorder: MediaRecorder){
        audioUseCase.recordingStop(recorder)
    }


    fun audioPlay(filePath : String){
        mediaPlayer = audioUseCase.audioPlay(filePath)
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