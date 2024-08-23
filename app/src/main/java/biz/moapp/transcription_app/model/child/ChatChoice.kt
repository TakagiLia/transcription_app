package biz.moapp.transcription_app.model.child

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class ChatChoice(
    val index: Int? = null,
    val message: ChatMessage? = null,
    val finishReason: String? = null,
)
