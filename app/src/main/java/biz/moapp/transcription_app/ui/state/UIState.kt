package biz.moapp.transcription_app.ui.state

sealed class UIState<out T> {
    data object NotYet : UIState<Nothing>()
    data object Loading : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Error(val message: String) : UIState<Nothing>()
}
