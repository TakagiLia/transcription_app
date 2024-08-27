package biz.moapp.transcription_app.model.child

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
data class FunctionCall(
    val name: String? = null,
    val arguments: String? = null
)
