package biz.moapp.transcription_app.hilt

import android.content.Context
import biz.moapp.transcription_app.network.OpenAiDataSource
import biz.moapp.transcription_app.network.RetrofitOpenAiNetwork
import biz.moapp.transcription_app.usecase.AudioUseCase
import biz.moapp.transcription_app.usecase.AudioUseCaseImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindsModule {

    @Binds
    @Singleton
    abstract fun bindAudioUseCase(impl: AudioUseCaseImpl): AudioUseCase

}

@Module
@InstallIn(SingletonComponent::class)
class AppProvideModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext app: Context): Context {
        return app
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            addInterceptor(interceptor)
        }.build()
    }

    @Provides
    @Singleton // シングルトンとして提供
    fun provideRetrofitOpenAiNetwork(moshi: Moshi): OpenAiDataSource {
        return RetrofitOpenAiNetwork(moshi)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideIoDispatchers(): CoroutineDispatcher = Dispatchers.IO

}