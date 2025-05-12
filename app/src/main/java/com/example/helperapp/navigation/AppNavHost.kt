package com.example.helperapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.helperapp.ui.theme.screens.admin.Adminscreen
import com.example.helperapp.ui.theme.screens.dashboard.Dashboardscreen
import com.example.helperapp.ui.theme.screens.edit.Edit_Profilescreen
import com.example.helperapp.ui.theme.screens.home.Homescreen
import com.example.helperapp.ui.theme.screens.login.Loginscreen
import com.example.helperapp.ui.theme.screens.profile.Profilescreen
import com.example.helperapp.ui.theme.screens.register.Registerscreen
import com.example.helperapp.ui.theme.screens.reports.Reportscreen
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
        composable (route_login){
            Loginscreen(navController)
        }
        composable(route_register) {
            Registerscreen(navController)
        }
        composable (route_profile){
            Profilescreen(navController)
        }
        composable (route_home){
            Homescreen(navController)
        }
        composable (route_admin){
            Adminscreen(navController)
        }
        composable(route_dashboard) {
            Dashboardscreen(navController)
        }
        composable (route_edit){
            Edit_Profilescreen(navController)
        }
        composable (route_reports){
            Reportscreen(navController)
        }


    }
}
