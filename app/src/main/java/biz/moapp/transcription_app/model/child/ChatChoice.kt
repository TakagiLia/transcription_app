package biz.moapp.transcription_app.model.child

import kotlinx.serialization.Serializable

@Serializable
data class ChatChoice(
    val index: Int? = null,
    val message: ChatMessage? = null,
    val finishReason: String? = null,
)
