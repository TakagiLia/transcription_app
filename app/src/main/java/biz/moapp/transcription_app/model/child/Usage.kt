package biz.moapp.transcription_app.model.child

import kotlinx.serialization.Serializable

@Serializable
data class Usage(
    val promptTokens: Int? = null,
    val completionTokens: Int? = null,
    val totalTokens: Int? = null,
)
