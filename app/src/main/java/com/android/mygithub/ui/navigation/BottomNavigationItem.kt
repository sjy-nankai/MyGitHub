package com.android.mygithub.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNaviItem(val title: String, val icon: ImageVector, val route: String) {
    data object Home : BottomNaviItem("Home", Icons.Default.Home, "Home")
    data object Search : BottomNaviItem("Search", Icons.Default.Search, "Search")
    data object Profile : BottomNaviItem("Profile", Icons.Default.Person, "Profile")
    data object Repositories : BottomNaviItem("Repos", Icons.Default.Person, "Repos")
}
