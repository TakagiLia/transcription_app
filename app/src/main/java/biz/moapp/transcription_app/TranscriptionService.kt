package biz.moapp.transcription_app

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class TranscriptionService : Service(){

    /**startService() で Service が起動されると onCreate() → onStartCommand() の順で呼び出される**/

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("--TranscriptionService", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("--TranscriptionService", "onStartCommand")

        intent?.getStringExtra("text")?.let {
            Log.d("--TranscriptionService", "intent: $it")
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("--TranscriptionService", "onDestroy")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("--TranscriptionService", "onTaskRemoved")
    }
}