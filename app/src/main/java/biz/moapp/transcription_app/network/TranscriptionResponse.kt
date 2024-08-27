package biz.moapp.transcription_app.network

import kotlinx.serialization.Serializable

@Serializable
data class TranscriptionResponse(
    val text: String
)
