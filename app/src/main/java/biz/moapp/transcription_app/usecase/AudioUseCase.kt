package biz.moapp.transcription_app.usecase

import android.media.MediaPlayer
import android.media.MediaRecorder

interface AudioUseCase {

    fun recordingStart(recorder: MediaRecorder, filePath : String)

    fun recordingStop(recorder: MediaRecorder)

    fun audioPlay(filePath:String): MediaPlayer

    fun audioStop(mediaPlayer: MediaPlayer)
}