package biz.moapp.transcription_app.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TranscriptionResponse(
    val text: String
)
