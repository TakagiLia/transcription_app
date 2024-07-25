package biz.moapp.transcription_app.model.child

import kotlinx.serialization.Serializable

@Serializable
data class FunctionCall(
    val name: String? = null,
    val arguments: String? = null
)
