package biz.moapp.transcription_app.model.child

enum class ChatRole(val role: String) {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant"),
    FUNCTION("function"),
    ;
}

