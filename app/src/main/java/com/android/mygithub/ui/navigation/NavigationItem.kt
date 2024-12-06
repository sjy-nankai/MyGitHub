package com.android.mygithub.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NaviItem(val title: String, val icon: ImageVector, val route: String) {
    data object Popular : NaviItem("Popular", Icons.Default.Star, "Popular")
    data object Search : NaviItem("Search", Icons.Default.Search, "Search")
    data object Profile : NaviItem("Profile", Icons.Default.Person, "Profile")
    data object Repositories : NaviItem("Repos", Icons.Default.Person, "Repos")
}
