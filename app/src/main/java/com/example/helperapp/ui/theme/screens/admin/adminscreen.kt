package com.example.helperapp.ui.theme.screens.admin


import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.helperapp.ui.theme.screens.home.BottomNavBar
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}
@Composable

fun Adminscreen(navController: NavHostController) {
        val auth = FirebaseAuth.getInstance()
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(LocalContext.current)
        val context = LocalContext.current
        val activity = context as? ComponentActivity ?: return


        var centerName by remember { mutableStateOf("") }

        var selectedCounty by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
        var contact by remember { mutableStateOf("") }
        var latitude by remember { mutableStateOf(0.0) }
        var longitude by remember { mutableStateOf(0.0) }
        var errorMessage by remember { mutableStateOf("") }
        var counties = listOf(
            "Mombasa",
            "Kwale",
            "Kilifi",
            "Tana River",
            "Lamu",
            "Taita-Taveta",
            "Garissa",
            "Wajir",
            "Mandera",
            "Marsabit",
            "Isiolo",
            "Meru",
            "Tharaka-Nithi",
            "Embu",
            "Kitui",
            "Machakos",
            "Makueni",
            "Nyandarua",
            "Nyeri",
            "Kirinyaga",
            "Murang'a",
            "Kiambu",
            "Turkana",
            "West Pokot",
            "Samburu",
            "Trans-Nzoia",
            "Uasin Gishu",
            "Elgeyo-Marakwet",
            "Nandi",
            "Baringo",
            "Laikipia",
            "Nakuru",
            "Narok",
            "Kajiado",
            "Kericho",
            "Bomet",
            "Kakamega",
            "Vihiga",
            "Bungoma",
            "Busia",
            "Siaya",
            "Kisumu",
            "Homa Bay",
            "Migori",
            "Kisii",
            "Nyamira",
            "Nairobi"
        )
        val firestore = FirebaseFirestore.getInstance()
        var volunteerCenters by remember {
            mutableStateOf<List<Pair<String, Map<String, Any>>>>(
                emptyList()
            )
        }

        LaunchedEffect(Unit) {
            firestore.collection("volunteer_centers")
                .get()
                .addOnSuccessListener { documents ->
                    volunteerCenters = documents.map { doc ->
                        doc.id to doc.data // Pair of (centerId, centerData)
                    }
                }
        }


        // Get current location
        LaunchedEffect(Unit) {
            if (hasLocationPermission(context)) {
                try {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        location?.let {
                            latitude = it.latitude
                            longitude = it.longitude
                        }
                    }
                } catch (e: SecurityException) {
                    e.printStackTrace()
                    // Optionally show a message or handle denial
                }
            }
        }








        // Handle form submission
        fun saveVolunteerCenter() {
            if (centerName.isNotEmpty() && selectedCounty.isNotEmpty()) {
                // Check if the county is valid
                val isValidCounty =
                    counties.any { it.equals(selectedCounty.trim(), ignoreCase = true) }
                if (!isValidCounty) {
                    errorMessage = "Invalid county. Please enter a valid Kenyan county."
                    return
                }

                // Save to Firestore
                val newCenter = hashMapOf(
                    "name" to centerName,
                    "county" to selectedCounty,
                    "latitude" to latitude,
                    "contact" to contact,
                    "longitude" to longitude,
                    "imageUrl" to "" // Optional image URL if you want to add images in the future
                )


                FirebaseFirestore.getInstance().collection("volunteer_centers")
                    .add(newCenter)
                    .addOnSuccessListener {
                        // Successfully added new volunteer center
                        navController.navigate("home") // Navigate back to home or anywhere you'd like
                    }
                    .addOnFailureListener { e ->
                        errorMessage = "Error adding center: ${e.message}"
                    }
            } else {
                errorMessage = "All fields are required!"
            }
        }


        // UI layout

        Scaffold(modifier = Modifier.fillMaxSize(),
            bottomBar = { AdminBottomNavBar(navController = navController,isAdmin=true) }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets(0.dp,5.dp,0.dp,0.dp)).padding(padding),
                contentAlignment = Alignment.TopStart) {
                val purple_200= Color(0xFFBB86FC)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(purple_200),
                verticalArrangement = Arrangement.Top
            ) {
                Text("Add New Volunteer Center", color = Color.Black, textDecoration = TextDecoration.Underline,
                    fontSize = 30.sp)

                Spacer(modifier = Modifier.height(16.dp))

                // Center Name
                OutlinedTextField(
                    value = centerName,
                    onValueChange = { centerName = it },
                    label = { Text("Volunteer Center Name", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )

                )

                Spacer(modifier = Modifier.height(16.dp))

                // County Input
                OutlinedTextField(
                    value = selectedCounty,
                    onValueChange = { selectedCounty = it },
                    label = { Text("County", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { /* Close keyboard */ }
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                // Contact Input
                OutlinedTextField(
                    value = contact,
                    onValueChange = { contact = it },
                    label = { Text("Contact", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { /* Close keyboard */ }
                    )
                )
                OutlinedTextField(
                    value = image,
                    onValueChange = { image = it },
                    label = { Text("imageURL", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )

                )


                // Latitude & Longitude Display
                Text("Latitude: $latitude", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                Text("Longitude: $longitude", style = MaterialTheme.typography.bodyMedium, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp))



                // Save Button
                Button(
                    onClick = { saveVolunteerCenter() },
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Volunteer Center",
                        color = Color.White)
                }

                // Error Message
                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = Color.Red)
                }
            }
            }

        }
    }


@Composable
fun AdminBottomNavBar(navController: NavHostController, isAdmin: Boolean) {
    val currentRoute by navController.currentBackStackEntryFlow
        .collectAsState(initial = navController.currentBackStackEntry?.destination?.route)

    NavigationBar (
        containerColor = Color.DarkGray,
        contentColor = Color.White

    ){
        if (isAdmin) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Add, contentDescription = "Admin") },
                label = { Text("Admin") },
                selected = currentRoute == "admin",
                onClick = {
                    if (currentRoute != "admin") {
                        navController.navigate("admin") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        }
                    }
                }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile") },
                selected = currentRoute == "profile",
                onClick = {
                    if (currentRoute != "profile") {
                        navController.navigate("profile") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        }
                    }
                }
            )
        }
    }
}





@Preview(showBackground = true)
@Composable
fun Adminscreenpreviewmock() {
    // A dummy screen that mimics the UI without real Firebase or GPS logic.
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Add New Volunteer Center")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "Mock Center",
            onValueChange = {},
            label = { Text("Volunteer Center Name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "Nairobi",
            onValueChange = {},
            label = { Text("County") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )



        Spacer(modifier = Modifier.height(16.dp))

        Text("Latitude: -1.286389")
        Text("Longitude: 36.817223")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {}, modifier = Modifier.fillMaxWidth(), enabled = false) {
            Text("Save Volunteer Center")
        }

        // No error
    }
}


