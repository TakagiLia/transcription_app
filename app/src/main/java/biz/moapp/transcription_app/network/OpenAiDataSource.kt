package biz.moapp.transcription_app.network

import biz.moapp.transcription_app.model.ChatCompletions

/**APIとの通信方法を定義**/
interface OpenAiDataSource {

    /**
     *ImageCompletions.RequestはAPIへのリクエスト(質問内容など)を表す型
     *ImageCompletions.ResponseはAPIからのレスポンス(回答など)を表す型
     * **/
    suspend fun imageCompletions(request: ChatCompletions.Request):ChatCompletions.Response
}