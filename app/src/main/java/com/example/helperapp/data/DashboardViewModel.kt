package com.example.helperapp.data

import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.firebase.firestore.FirebaseFirestore
import com.example.helperapp.model.QuickAction
import com.example.helperapp.model.DashboardStat


open class DashboardViewModel: ViewModel(){
    private val db = FirebaseFirestore.getInstance()
    open fun submitReport(centerName: String, duration: String, experience: String,onComplete:() -> Unit = {}) {
        if (centerName.isBlank() || duration.isBlank() || experience.isBlank()) {
            println("Error: All fields must be filled!")
            onComplete()
            return
        }

        val report = hashMapOf(
            "centerName" to centerName,
            "duration" to duration,
            "experience" to experience,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("reports")
            .add(report)
            .addOnSuccessListener {
                println("Report submitted successfully!")
                onComplete()
                // Handle success (e.g., show a success message)
            }
            .addOnFailureListener { e ->
                println("Error submitting report: $e")
                onComplete()
                // Handle failure (e.g., show an error message)
            }
    }
    open fun getReports(onResult: (List<Map<String, Any>>) -> Unit) {
        db.collection("reports")
            .get()
            .addOnSuccessListener { result ->
                val reports = result.map { it.data }
                onResult(reports)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

}



    val _stats= MutableStateFlow(
        listOf(
            DashboardStat("Revenue","12000", Icons.Default.Phone,Color.Magenta),
            DashboardStat("User","1456", Icons.Default.Person,Color.Cyan)
        )
    )
    val stats: StateFlow<List<DashboardStat>>get()=_stats
    private val _quickAction=MutableStateFlow(
        listOf(
            QuickAction("Add new user", Icons.Default.Person),
            QuickAction("Generate report", Icons.Default.AccountCircle),
            QuickAction("setting", Icons.Default.Settings),
        )
    )
    val quickAction: StateFlow<List<QuickAction>>get() =_quickAction

