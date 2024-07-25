package biz.moapp.transcription_app

import android.annotation.SuppressLint

object AppUtils {
    @SuppressLint("DefaultLocale")
    fun formatTime(timeInMillis: Long): String {
        val totalSeconds = timeInMillis / 1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds / 60) % 60
        val hours = totalSeconds / 3600
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
