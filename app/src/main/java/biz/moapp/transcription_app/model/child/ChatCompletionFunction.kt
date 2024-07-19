package biz.moapp.transcription_app.model.child

import kotlinx.serialization.Serializable

@Serializable
//@JsonClass(generateAdapter = true)
data class ChatCompletionFunction(
    val name: String? = null,
    val description: String? = null,
    //TODO: create json decode data class
    val parameters: String? = null,
)
