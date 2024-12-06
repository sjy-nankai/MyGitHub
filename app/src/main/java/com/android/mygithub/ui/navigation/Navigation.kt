package com.android.mygithub.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.android.mygithub.ui.screen.PopularScreen
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
        startDestination = NaviItem.Popular.route,
    ) {
        composable(NaviItem.Popular.route) {
            PopularScreen(paddingValues)
        }

        composable(NaviItem.Search.route) {
            SearchScreen(paddingValues)
        }

        composable(NaviItem.Profile.route) {
            ProfileScreen(
                paddingValues,
                { navController.navigate(route = NaviItem.Repositories.route) })
        }

        composable(NaviItem.Repositories.route) {
            RepositoriesScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}