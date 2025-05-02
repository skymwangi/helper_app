package com.example.helperapp.ui.theme.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.helperapp.navigation.AppNavHost
import com.example.helperapp.ui.theme.BottomNavBar

@Composable
fun Homescreen(navController: NavHostController) {
    val bottomBarScreens=listOf("home","search","dashboard","profile")
    val currentBackStackEntry=navController.currentBackStackEntryAsState()
    val currentRoute=currentBackStackEntry.value?.destination?.route
    Scaffold (bottomBar = {
        if (currentRoute in bottomBarScreens){
            BottomNavBar(navController)
        }
    }
    ){
        Box(modifier = Modifier.padding(it)){
        AppNavHost(navController=navController)



    }
    }

    
}