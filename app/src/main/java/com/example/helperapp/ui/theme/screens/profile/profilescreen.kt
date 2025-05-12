package com.example.helperapp.ui.theme.screens.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Scaffold
import com.example.helperapp.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.helperapp.data.AuthViewModel
import com.example.helperapp.ui.theme.Pink40
import com.example.helperapp.ui.theme.Purple80
import java.io.File

@Composable
fun ProfileContent(
    navController: NavHostController,
    userData: Map<String, String>,
    profileImageUri: Uri?,
    onSelectGallery: () -> Unit = {},
    onSelectCamera: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onLogout: () -> Unit = {},
    isAdmin: Boolean = false
) {

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController, isAdmin = isAdmin) }
    ) { padding ->
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Pink40)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Personal Information",
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            var expanded by remember { mutableStateOf(false) }

            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) {
                // Circular image container
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    if (profileImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = profileImageUri
                            ),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .clickable { expanded = true },
                            contentScale = ContentScale.Crop
                        )
                    } else {
//                        if (isAdmin) {
                            // Default image for admin
                            Image(
                                painter = painterResource(id = R.drawable.img), // Replace with your admin image resource
                                contentDescription = " Default Profile Picture",
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                            )
//                        }
//                        else {
                            // Default gray circle for normal users
//                            Box(
//                                modifier = Modifier
//                                    .size(150.dp)
//                                    .clip(CircleShape)
//                                    .background(Color.Gray)
//                                    .clickable { expanded = true }
//                            )
//                        }
                    }


                }

                // Pencil icon
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .offset(x = (-24).dp, y = (-16).dp) // bottom-end corner of circle
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Black
                    )
                }

                // Dropdown menu
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Select from Gallery") },
                        onClick = {
                            expanded = false
                            onSelectGallery()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Take Photo") },
                        onClick = {
                            expanded = false
                            onSelectCamera()
                        }
                    )
                }
            }
        }


        item {
            OutlinedTextField(
                value = "${userData["firstName"] ?: ""} ${userData["lastName"] ?: ""}",
                onValueChange = {},
                label = { Text("Full Name", color = Color.Black) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = userData["email"] ?: "",
                onValueChange = {},
                label = { Text("Email", color = Color.Black) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = userData["county"] ?: "",
                onValueChange = {},
                label = { Text("County", color = Color.Black) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            PersonalNotes(userId = userData["uid"] ?: "")
        }

        item {
            Button(onClick = onEditProfile, modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.Black)) {
                Text("Edit Profile", color = Color.White)
            }
        }

        item {
            val context=LocalContext.current
            Button(
                onClick = {

                    val authViewModel = AuthViewModel(navController,context)
                    authViewModel.logout()
                    navController.navigate("login")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Purple80),
                modifier = Modifier.fillMaxWidth() // Fixed the modifier prefix
            ) {
                Text("Log Out", color = Color.Black)
            }
        }

//        item {
//            Spacer(modifier = Modifier.height(40.dp))
//            Image(
//                painter = painterResource(id = R.drawable.img),
//                contentDescription = "App Logo",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(100.dp)
//                    .width(100.dp)
//                    .padding(top = 24.dp)
//            )
//        }
    }
}
}
@Composable
fun PersonalNotes(userId: String) {
    val context = LocalContext.current
    var personalNotes by remember { mutableStateOf("") }

    // Fetch the existing personal notes from Firestore
    LaunchedEffect(userId) {
        FirebaseFirestore.getInstance().collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                personalNotes = document.getString("personalNotes") ?: ""
            }
    }

    OutlinedTextField(
        value = personalNotes,
        onValueChange = { personalNotes = it },
        label = { Text("Personal Notes", color = Color.Black) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("You can write something about yourself...", color = Color.Black) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // Save the personal notes to Firestore
                FirebaseFirestore.getInstance().collection("users")
                    .document(userId)
                    .update("personalNotes", personalNotes)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Information has been updated", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Information failed to update", Toast.LENGTH_SHORT).show()
                    }
            }
        )
    )
}
fun uploadProfileImageToFirebase(
    imageUri: Uri,
    onSuccess: (String) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val storageRef = FirebaseStorage.getInstance().reference
        .child("profilePictureUrl/$userId.jpg")

    storageRef.putFile(imageUri)
        .addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                val profilePicUrl = downloadUrl.toString()
                // Save the URL to Firestore
                FirebaseFirestore.getInstance().collection("users")
                    .document(userId)
                    .update("profilePictureUrl", profilePicUrl)
                    .addOnSuccessListener {
                        onSuccess(profilePicUrl)
                    }
                    .addOnFailureListener { onFailure(it) }
            }
        }
        .addOnFailureListener { onFailure(it) }
}


@Composable
fun Profilescreen(navController: NavHostController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    var userData by remember { mutableStateOf<Map<String, String>?>(null) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(true) }





    LaunchedEffect(userId) {
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users")
                .document( userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val data = document.data?.mapValues { it.value?.toString() ?: "" }
                        userData = data
                        val isAdmin = document.getBoolean("isAdmin") == true
                        val imageUrl = data?.get("profilePictureUrl")
                        profileImageUri = if (!imageUrl.isNullOrEmpty() && imageUrl.startsWith("http")) {
                            imageUrl.toUri()
                        } else {
                            null
                        }
                    }else{
                        Log.e("ProfileScreen", "No user data found for uid: $userId")
                    }
                    isLoading = false
                }
                .addOnFailureListener { e ->
                    Log.e("ProfileScreen", "Error fetching user data: ${e.message}")
                    isLoading = false
                }
        }else{
            isLoading = false
        }
    }



    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri = data?.data

            if (uri == null) {
                Toast.makeText(context, "Camera result failed or unsupported", Toast.LENGTH_SHORT).show()
                return@rememberLauncherForActivityResult
            }

            profileImageUri = uri

            // Upload to Firebase and save URL to Firestore
            uploadProfileImageToFirebase(
                imageUri = uri,
                onSuccess = { url ->
                    Toast.makeText(context, "Profile image saved!", Toast.LENGTH_SHORT).show()
                },
                onFailure = { e ->
                    Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }




    val openGallery = {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    val openCamera = {
        val photoFile = File(context.cacheDir, "temp_profile_picture.jpg")
        val photoUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        pickImageLauncher.launch(intent)
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...", color = Color.Black)
        }
    } else {
        userData?.let {
            ProfileContent(
                userData = it,
                profileImageUri = profileImageUri,
                onSelectGallery = openGallery,
                onSelectCamera = openCamera,
                onEditProfile = { navController.navigate("edit_profile") },
                onLogout = {
                    auth.signOut()
                    navController.navigate("login")
                },
                navController = navController,
                isAdmin = it["isAdmin"] == "true" // Check if user is admin
            )
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No user data found", color = Color.Black)
            }
        }
    }
}
@Composable
fun BottomNavBar(navController: NavHostController,isAdmin: Boolean) {
    NavigationBar (
        containerColor = Color.DarkGray,
        contentColor = Color.White

    ){
        if (isAdmin) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Add, contentDescription = "Admin") },
                label = { Text("Admin") },
                selected = navController.currentBackStackEntry?.destination?.route=="admin",
                onClick = { navController.navigate("admin") }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile") },
                selected = navController.currentBackStackEntry?.destination?.route=="profile",
                onClick = { navController.navigate("profile") }
            )
        } else {


        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = false,
            onClick = { navController.navigate("dashboard") }
        )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Menu, contentDescription = "Reports") },
                label = { Text("Reports") },
                selected = false,
                onClick = { navController.navigate("reports") }
            )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = true,
            onClick = { navController.navigate("profile") }
        )
    }


    }
}

    @Preview(showBackground = true)
    @Composable
    fun Profileprev() {
        val fakeUserData = mapOf(
            "firstName" to "Jane",
            "lastName" to "Doe",
            "email" to "jane.doe@example.com",
            "county" to "Nairobi"
        )

        ProfileContent(
            userData = fakeUserData,
            profileImageUri = null,
            navController = TODO() // or add a fake Uri if you want
        )
    }



