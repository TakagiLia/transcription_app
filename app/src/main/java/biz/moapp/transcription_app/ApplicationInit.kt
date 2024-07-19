package biz.moapp.transcription_app

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationInit : Application()  {
    override fun onCreate() {
        super.onCreate()
        Log.d("■ApplicationInit", "onCreate動く")
    }
}