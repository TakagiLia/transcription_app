package biz.moapp.transcription_app.usecase

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import javax.inject.Inject

class AudioUseCaseImpl@Inject constructor(
    context: Context
)  :AudioUseCase {

    private val filePath : String = context.getExternalFilesDir(null)?.absolutePath + "/recording.m4a"

    override fun recordingStart(recorder: MediaRecorder) {
        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(filePath)
            prepare()
            start()
        }
    }

    override fun recordingStop(recorder: MediaRecorder) {
        recorder.stop()
        recorder.reset()
    }

    override fun audioPlay(): MediaPlayer{
        return MediaPlayer().apply {
            try {
                setDataSource(filePath)
                prepare()
                start()
            } catch (e: Exception) {
                Log.d("--MediaPayer","playError: ${e.message}",e)
            }
        }
    }

    override fun audioStop(mediaPlayer: MediaPlayer){
        mediaPlayer.stop()
        mediaPlayer.reset()
    }
}