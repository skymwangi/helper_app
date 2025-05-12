package com.example.helperapp.ui.theme.screens.home
import android.content.pm.PackageManager
import android.location.Location
import android.Manifest
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.helperapp.ui.theme.Nude
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.location.LocationServices
import java.lang.Exception


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Homescreen(navController: NavHostController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var volunteerCenters by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var isAdmin by remember { mutableStateOf(false) }
//    // Request permissions and get location
//    val locationPermissionState = rememberPermissionState(
//        permission = android.Manifest.permission.ACCESS_FINE_LOCATION
//    )
    LaunchedEffect(Unit) {
        val permissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    userLocation = location
                }
        }
    }







    // Get user info
    LaunchedEffect(Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId).get().addOnSuccessListener { document ->
                isAdmin = document.getBoolean("admin") ?: false
            }
        }
    }

    // Get user location
    LaunchedEffect(Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            userLocation = location
        }
            .addOnFailureListener { e ->
                userLocation = null

            }
    }

    // Fetch volunteer centers
    LaunchedEffect(Unit) {
        firestore.collection("volunteer_centers").get().addOnSuccessListener { snapshot ->
            val centers = snapshot.documents.mapNotNull { doc ->
                val name = doc.getString("name") ?: return@mapNotNull null
                val county = doc.getString("county") ?: ""
                val latitude = doc.getDouble("latitude") ?: 0.0
                val longitude = doc.getDouble("longitude") ?: 0.0
                val imageUrl = doc.getString("imageUrl") ?: ""
                val contact = doc.getString("contact") ?: "N/A" // <- Here's the contact field

                mapOf(
                    "name" to name,
                    "county" to county,
                    "latitude" to latitude,
                    "longitude" to longitude,
                    "contact" to contact,
                    "imageUrl" to imageUrl
                )
            }
            volunteerCenters = centers
        }
    }


    Scaffold(
        bottomBar = { BottomNavBar(navController = navController, isAdmin = isAdmin) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .background(Color.White)
        ) {

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Volunteer Centers") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(volunteerCenters.filter {
                    val name = it["name"] as? String ?: ""
                    val county = it["county"] as? String ?: ""
                    name.contains(searchQuery.text, ignoreCase = true) || county.contains(
                        searchQuery.text,
                        ignoreCase = true
                    )
                }) { center ->
                    VolunteerCenterCard(center = center, userLocation = userLocation)
                }

            }
        }
    }
    Homescreencontent(
        volunteerCenters = volunteerCenters,
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        userLocation = userLocation,
        navController = navController
    )

}

@Composable
fun Homescreencontent(
    volunteerCenters: List<Map<String, Any>>,
    searchQuery: TextFieldValue,
    onSearchQueryChange: (TextFieldValue) -> Unit,
    userLocation: Location?,
    navController: NavHostController
) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController = navController, isAdmin = false) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .background(Nude)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text("Search Volunteer Centers", color = Color.Black) },
                colors = TextFieldDefaults.colors(unfocusedIndicatorColor = Color.Black,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(volunteerCenters.filter {
                    val name = it["name"] as? String ?: ""
                    val county = it["county"] as? String ?: ""
                    val contact = it["contact"] as? String ?:""

                    name.contains(searchQuery.text, ignoreCase = true) || county.contains(
                        searchQuery.text, ignoreCase = true) || contact.contains(searchQuery.text,ignoreCase = true)
                }) { center ->
                    VolunteerCenterCard(center = center, userLocation = userLocation)
                }
            }
        }
    }
}










@Composable
fun VolunteerCenterCard(center: Map<String, Any>, userLocation: Location?) {
    val name = center["name"] as? String ?: ""
    val county = center["county"] as? String ?: ""
    val contact = center["contact"] as? String ?:""
    val latitude = center["latitude"] as? Double ?: 0.0
    val longitude = center["longitude"] as? Double ?: 0.0
    val imageUrl = center["imageUrl"] as? String

    val distance = userLocation?.let {
        calculateDistance(it.latitude, it.longitude,latitude,longitude)
    } ?: 0.0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(Color.White, contentColor = Color.Black),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Name: $name", style = MaterialTheme.typography.bodyMedium)
            Text(text = "County: $county", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Contact: $contact", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Distance: %.2f km".format(distance), style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            if (imageUrl?.isNotEmpty()==true) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Volunteer Center Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController, isAdmin: Boolean) {
    NavigationBar (
        containerColor = Color.DarkGray,
        contentColor = Color.White

    ){
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
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
            selected = false,
            onClick = { navController.navigate("profile") }
        )

    }
}
@Composable
fun RequestLocationPermission(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationUpdated:(Location?) -> Unit
) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show rationale (optional)
            } else {
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        } else {
            permissionGranted = true
        }
    }

    if (permissionGranted) {
        // Permission granted logic
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                onLocationUpdated(location)}
            .addOnFailureListener { e: Exception ->
                onLocationUpdated(null)
            }
    } else {
        // Permission not granted logic
        // Show a message to the user explaining why the permission is needed
        Toast.makeText(
            context,
            "Location permission is required to show distance to volunteer centers.",
            Toast.LENGTH_LONG
        ).show()
    }
}

fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    return R * c
}

@Preview(showBackground = true)
@Composable
private fun Homeprev() {
    val mockNavController = rememberNavController()

    val mockCenters = listOf(
        mapOf(
            "name" to "Hope Center",
            "county" to "Nairobi",
            "contact" to "0750546697",
            "latitude" to -1.2921,
            "longitude" to 36.8219,
            "imageUrl" to ""
        ),
        mapOf(
            "name" to "Unity Volunteers",
            "county" to "Kiambu",
            "latitude" to -1.1,
            "longitude" to 36.9,
            "imageUrl" to ""
        )
    )

    val mockLocation = Location("mock").apply {
        latitude = -1.2921
        longitude = 36.8219
    }

    Homescreencontent(
        volunteerCenters = mockCenters,
        searchQuery = TextFieldValue(""),
        onSearchQueryChange = {},
        userLocation = mockLocation,
        navController = mockNavController
    )



}


