package biz.moapp.transcription_app.model.child

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val role: String,
    val content: String? = null,
    val name: String? = null,
    @Json(name = "function_call")
    val functionCall: FunctionCall? = null
)
