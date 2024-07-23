package biz.moapp.transcription_app.usecase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class FirebaseUseCaseImpl :FirebaseUseCase{

    /**summaryを保存**/
    override fun saveSummary(summary: String): Task<DocumentReference>{

        val record = hashMapOf(
            "content" to summary,
            "user" to "",
            "createdate" to FieldValue.serverTimestamp(),
        )

        return Firebase.firestore.collection("summary")
            .add(record).addOnSuccessListener { documentReference ->
                Log.d("--saveSummary　fb", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("--saveSummary　fb", "Error adding document", e)
            }
    }
}