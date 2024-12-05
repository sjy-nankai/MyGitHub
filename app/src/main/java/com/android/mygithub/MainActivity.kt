package com.android.mygithub

import android.app.ComponentCaller
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.github.utils.AuthPreferences
import com.android.mygithub.ui.navigation.BottomNaviItem
import com.android.mygithub.ui.navigation.NavigationGraph
import com.android.mygithub.ui.theme.MyGitHubTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.let { handleIntent(lifecycleScope, this, it) }
        enableEdgeToEdge()
        setContent {
            MyGitHubTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            listOf(
                                BottomNaviItem.Home,
                                BottomNaviItem.Search,
                                BottomNaviItem.Profile,
                            ).forEach { bottomNaviItem ->
                                NavigationBarItem(
                                    selected = bottomNaviItem.route == currentDestination?.route,
                                    label = {
                                        Text(bottomNaviItem.title)
                                    },
                                    icon = {
                                        Icon(
                                            bottomNaviItem.icon,
                                            contentDescription = bottomNaviItem.title
                                        )
                                    },
                                    onClick = {
                                        navController.navigate(bottomNaviItem.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }) { innerPadding ->
                    NavigationGraph(
                        navController = navController,
                        paddingValues = innerPadding,
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        handleIntent(lifecycleScope, this, intent)
    }

    private fun handleIntent(scope: CoroutineScope, context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            intent.data?.let { uri ->
                if (uri.scheme == "mygithub" && uri.host == "oauth") {
                    val code = uri.getQueryParameter("code") ?: ""
                    scope.launch {
//                        AuthPreferences.saveAccessCode(context, code)
                        AuthPreferences.code = code
                    }
                }
            }
        }
    }
}

