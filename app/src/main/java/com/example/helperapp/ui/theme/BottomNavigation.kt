package com.example.helperapp.ui.theme
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController

data class BottomNavigationItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items=listOf(
        BottomNavigationItem("Home","home",Icons.Default.Home),
        BottomNavigationItem("Search","search",Icons.Default.Search),
        BottomNavigationItem("Dashboard","dashboard", Icons.Default.Menu),
        BottomNavigationItem("Profile","profile", Icons.Default.Person)
    )
    BottomNavigation {
        val currentDestination=navController.currentBackStackEntry?.destination
        items.forEach { item ->
            BottomNavigationItem(icon = {Icon(item.icon,
                contentDescription = item.label)},
                label = { Text(item.label) },
                selected=currentDestination?.hierarchy?.any { it.route == item.route }
            ==true,
                onClick={
                    if (currentDestination?.route !=item.route){
                        navController.navigate(item.route){
                            popUpTo(navController.graph.startDestinationId){
                                saveState=true
                            }
                            launchSingleTop=true
                            restoreState=true
                        }
                    }
                }
                )
        }

    }

}