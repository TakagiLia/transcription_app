package biz.moapp.transcription_app.usecase

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log

class AudioUseCaseImpl :AudioUseCase {

    override fun recordingStart(recorder: MediaRecorder, filePath:String) {
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

    override fun audioPlay(filePath:String): MediaPlayer{
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