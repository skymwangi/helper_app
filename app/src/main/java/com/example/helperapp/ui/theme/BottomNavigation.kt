package com.example.helperapp.ui.theme

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController

data class NavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun BottomNavBar(navController: NavHostController, isAdmin: Boolean) {
    val items = if (isAdmin) {
        listOf(
            NavItem("Admin", "admin", Icons.Default.Add),
            NavItem("Profile", "profile", Icons.Default.Person)
        )
    } else {
        listOf(
            NavItem("Home", "home", Icons.Default.Home),
            NavItem("Dashboard", "dashboard", Icons.Default.Dashboard),
            NavItem("Reports", "reports", Icons.Default.Menu),
            NavItem("Profile", "profile", Icons.Default.Person)
        )
    }

    BottomNavigation {
        val currentDestination = navController.currentBackStackEntry?.destination
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    if (currentDestination?.route != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}