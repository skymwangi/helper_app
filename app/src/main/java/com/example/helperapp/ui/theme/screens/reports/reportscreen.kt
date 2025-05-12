package com.example.helperapp.ui.theme.screens.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentDataType.Companion.Date
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.helperapp.data.DashboardViewModel
import com.example.helperapp.ui.theme.BottomNavBar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun Reportscreen(navController: NavHostController) {
    val viewModel: DashboardViewModel = viewModel()
    var reports by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    LaunchedEffect(Unit) {
        viewModel.getReports { fetchedReports ->
            reports = fetchedReports.sortedByDescending {
                (it["timestamp"] as? Timestamp)?.toDate()?.time ?: 0L
            }
        }
    }



    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController = navController, isAdmin = false, selectedScreen = "reports") }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFDFCBB2),
                                Color(0xFFCBB080),
                                Color(0xFFB6724D),
                                Color(0xFFA64426),
                                Color(0xFF960C00)
                            )
                        )
                    )
            ) {
                Text(
                    text = "My Reports",
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 30.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn {
                    items(reports) { report ->
                        val timestamp=report["timestamp"]as? Timestamp
                        val date = timestamp?.toDate()?: Date()
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),

                            elevation = CardDefaults.cardElevation( 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Center Name: ${report["centerName"]?:"Unknown"}", color = Color.Black)
                                Text("Duration: ${report["duration"]?: "Unknown"}", color = Color.Black)
                                Text("Experience: ${report["experience"] ?:"No Experience Provided"}", color = Color.Black)
                                Text("Date: ${dateFormat.format(date)}", color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun BottomNavBar(navController: NavHostController, isAdmin: Boolean, selectedScreen: String) {
    NavigationBar(
        containerColor = Color.DarkGray,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedScreen == "home",
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = selectedScreen == "dashboard",
            onClick = { navController.navigate("dashboard") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Menu, contentDescription = "Reports") },
            label = { Text("Reports") },
            selected = selectedScreen == "reports",
            onClick = { navController.navigate("reports") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedScreen == "profile",
            onClick = { navController.navigate("profile") }
        )
    }
}
@Composable
fun MockDashboardViewModel(viewModel: DashboardViewModel=object : DashboardViewModel() {
        override fun getReports(callback: (List<Map<String, Any>>) -> Unit) {
            val mockReports = listOf(
                mapOf(
                    "centerName" to "Health Center A",
                    "duration" to "2 hours",
                    "experience" to "Very good",
                    "timestamp" to Timestamp(Date(System.currentTimeMillis() - 3600000))
                ),
                mapOf(
                    "centerName" to "Health Center B",
                    "duration" to "1 hour",
                    "experience" to "Satisfactory",
                    "timestamp" to Timestamp(Date(System.currentTimeMillis() - 7200000))
                ),
                mapOf(
                    "centerName" to "Health Center C",
                    "duration" to "3 hours",
                    "experience" to "Excellent",
                    "timestamp" to Timestamp(Date(System.currentTimeMillis() - 10800000))
                )
            )
            callback(mockReports)
        }
    }): DashboardViewModel{
    return viewModel
    }


@Preview(showBackground = true)
@Composable
fun ReportscreenPreview() {
    val navController = rememberNavController()
    val viewModel = MockDashboardViewModel()
    LaunchedEffect(Unit) {
        viewModel.getReports {  }
    }

    // Use the mock ViewModel in the Reportscreen
    Reportscreen(navController = navController)
}