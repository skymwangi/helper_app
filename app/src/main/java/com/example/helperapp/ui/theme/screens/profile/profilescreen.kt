package com.example.helperapp.ui.theme.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Profilescreen(navController: NavHostController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    var userData by remember { mutableStateOf<Map<String, String>?>(null) }

    LaunchedEffect(userId) {
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        userData = document.data as Map<String, String>
                    }
                }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        userData?.let {
        item {
            Text(
                text = "Personal Information",
                style = MaterialTheme.typography.titleMedium
            )
        }

        item {
            OutlinedTextField(
                value = userData?.get("firstName") + " " + userData?.get("lastName"),
                onValueChange = {},
                label = { Text("Full Name") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = userData?.get("email") ?: "",
                onValueChange = {},
                label = { Text("Email") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = userData?.get("county") ?: "",
                onValueChange = {},
                label = { Text("County") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Personal Notes") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("You can write something about yourself...") }
            )
        }

        item {
            Button(
                onClick = { navController.navigate("edit_profile") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Profile")
            }
        }

        item {
            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate("login")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log Out", color = Color.White)
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                painter = painterResource(id = R.drawable.img), // Replace with your actual logo ID
                contentDescription = "App Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .width(100.dp)
                    .padding(top = 24.dp)
            )
        }
    }

    }
}

@Preview
@Composable
private fun Profileprev() {
    Profilescreen(rememberNavController())


}
