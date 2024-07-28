package biz.moapp.transcription_app.network

import biz.moapp.transcription_app.OPENAI_API_KEY
import com.squareup.moshi.Moshi
import kotlinx.serialization.Serializable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface OpenAIService {
    /**
     * @Multipart:リクエスト形式をmultipart/form-dataに設定
     * @Part("model") model: RequestBody:model パラメータをリクエストボディに含めるために必要
     * **/
    @Multipart
    @Headers("Authorization: Bearer $OPENAI_API_KEY")
    @POST("v1/audio/transcriptions")
    suspend fun transcribeAudio(
        @Part file: MultipartBody.Part,
        @Part("model") model: RequestBody,
    ): Response<TranscriptionResponse>
}

@Serializable
data class TranscriptionResponse(
    val text: String
)

@Singleton
class OpenAiAudioApi @Inject constructor(moshi: Moshi){

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    private val openAIService = retrofit.create(OpenAIService::class.java)

    suspend fun completions(filePath : String) : TranscriptionResponse? {
        val audioFile = File(filePath)
        val requestBody = audioFile.asRequestBody("audio/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", audioFile.name, requestBody)

        /**文字列"whisper-1"をRequestBody型に変換**/
        val model = "whisper-1".toRequestBody("text/plain".toMediaTypeOrNull())
        val response= openAIService.transcribeAudio(filePart,model)
        return response.body()
    }
}