package com.example.helperapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.helperapp.ui.theme.screens.splash.Splashscreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier,
               navController: NavHostController= rememberNavController(),
               startDestination: String=route_splash)

{
    NavHost(navController=navController,
        modifier = modifier,
        startDestination = startDestination){
        composable(route_splash) {
            Splashscreen(navController)
        }


    }
}
