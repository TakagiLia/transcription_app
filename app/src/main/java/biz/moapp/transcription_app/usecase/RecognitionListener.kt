package biz.moapp.transcription_app.usecase

import org.vosk.android.RecognitionListener

class RecognitionListener : RecognitionListener {
    override fun onResult(hypothesis: String?) {
        // 音声認識結果の処理
    }

    override fun onFinalResult(hypothesis: String?) {
        TODO("Not yet implemented")
    }

    override fun onPartialResult(hypothesis: String?) {
        // 部分的な音声認識結果の処理
    }

    override fun onError(exception: Exception?) {
        // エラー処理
    }

    override fun onTimeout() {
        // タイムアウト処理
    }
}
