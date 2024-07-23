package biz.moapp.transcription_app.usecase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference

interface FirebaseUseCase {

    fun saveSummary(summary : String) : Task<DocumentReference>

}