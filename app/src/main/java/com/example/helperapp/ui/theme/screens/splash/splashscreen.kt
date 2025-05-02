package com.example.helperapp.ui.theme.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import com.example.helperapp.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.helperapp.navigation.route_home
import com.example.helperapp.navigation.route_login
import kotlinx.coroutines.delay
import com.example.helperapp.ui.theme.Nude
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Splashscreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(3000) //Splash screen duration
        navController.navigate(route_login)
        val isLoggedIn= FirebaseAuth.getInstance().currentUser !=null
        if (isLoggedIn){
            navController.navigate("home"){
                popUpTo("splash"){inclusive=true}
            }
        }
        else{
            navController.navigate("login"){
                popUpTo("splash"){inclusive=true}
            }
        }

    }
    Box  (
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
            .background(Nude)

    ) {
        //Splash screen content (logo or image)
        Image(
            painter = painterResource(id = R.drawable.splash_screen),
            contentDescription = "Splash Screen Logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}