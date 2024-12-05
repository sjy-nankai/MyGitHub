package com.android.mygithub.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.android.mygithub.ui.screen.MainScreen
import com.android.mygithub.ui.screen.ProfileScreen
import com.android.mygithub.ui.screen.RepositoriesScreen
import com.android.mygithub.ui.screen.SearchScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = BottomNaviItem.Home.route,
    ) {
        composable(BottomNaviItem.Home.route) {
            MainScreen(paddingValues)
        }

        composable(BottomNaviItem.Search.route) {
            SearchScreen(paddingValues)
        }

        composable(BottomNaviItem.Profile.route) {
            ProfileScreen(
                paddingValues,
                { navController.navigate(route = BottomNaviItem.Repositories.route) })
        }

        composable(BottomNaviItem.Repositories.route) {
            RepositoriesScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}