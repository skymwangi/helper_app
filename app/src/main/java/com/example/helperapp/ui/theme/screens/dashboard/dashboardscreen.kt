package com.example.helperapp.ui.theme.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.helperapp.data.DashboardViewModel
import com.example.helperapp.ui.theme.Nude
import com.example.helperapp.ui.theme.screens.home.BottomNavBar

@Composable
fun Dashboardscreen(navController: NavHostController) {
    val viewModel: DashboardViewModel=viewModel()
    var centerName by remember { mutableStateOf(TextFieldValue("")) }
    var duration by remember { mutableStateOf(TextFieldValue("")) }
    var experience by remember { mutableStateOf(TextFieldValue("")) }
    var isSubmitting by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController = navController, isAdmin = false, selectedScreen = "dashboard") }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.TopStart) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Text(
                    text = "Write a Volunteer Report",
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Volunteer Center Name
                OutlinedTextField(
                    value = centerName,
                    onValueChange = { centerName = it },
                    label = { Text("Volunteer Center Name", color = Nude) },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedIndicatorColor = Nude,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Duration
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedIndicatorColor = Nude,
                        unfocusedContainerColor = Color.White
                    ),
                    label = { Text("Duration (e.g., 2 weeks)", color = Nude) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Experience
                OutlinedTextField(
                    value = experience,
                    onValueChange = { experience = it },
                    label = { Text("Experience", color = Nude) },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        focusedIndicatorColor = Nude,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(16.dp))



                // Submit Button
                Button(
                    onClick = {
                        isSubmitting = true
                        viewModel.submitReport(
                            centerName.text,
                            duration.text,
                            experience.text
                        )
                        {
                            isSubmitting = false
                            showMessage = true
                            centerName = TextFieldValue("")
                            duration = TextFieldValue("")
                            experience = TextFieldValue("")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Nude),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSubmitting
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Submit Report")
                    }
                }

                if (showMessage) {
                    Snackbar(
                        action = {
                            TextButton(onClick = { showMessage = false }) {
                                Text("Dismiss", color = Color.White)
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Report submitted successfully!")
                    }
                }
            }
        }
    }

}




// Updated BottomNavBar for Dashboard
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
@Preview(showBackground = true)
@Composable
fun Dashboardscreenprev() {
    val mockNavController = rememberNavController()

    Dashboardscreen(
        navController = mockNavController
    )
}


