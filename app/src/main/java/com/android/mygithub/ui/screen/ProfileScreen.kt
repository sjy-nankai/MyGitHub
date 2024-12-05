package com.android.mygithub.ui.screen

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.github.common.authUrl
import com.android.github.domain.model.AuthState
import com.android.mygithub.ui.component.AnonymousProfileView
import com.android.mygithub.ui.component.AuthenticatedProfileView
import com.android.mygithub.ui.component.MyTopBar
import com.android.mygithub.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    onRepositoriesClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel(),
) {
    val refreshState = rememberPullToRefreshState()
    val context = LocalContext.current
    val customTabsIntent = CustomTabsIntent.Builder().build()

    Scaffold(
        modifier = Modifier
            .nestedScroll(refreshState.nestedScrollConnection),
        topBar = {
            MyTopBar(
                title = "Profile",
                paddingValues = paddingValues,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize()
                .nestedScroll(refreshState.nestedScrollConnection),
        ) {
            val uiState by viewModel.uiState.collectAsState()
            LaunchedEffect(Unit) {
                viewModel.getAuthState(context)
            }

            when (val state = uiState.authState) {
                is AuthState.Anonymous -> {
                    AnonymousProfileView(
                        onLoginClick = {
                            customTabsIntent.launchUrl(context, Uri.parse(authUrl))
                        },
                    )
                }

                is AuthState.Authenticated -> {
                    AuthenticatedProfileView(
                        user = state.user,
                        onLogoutClick = { viewModel.signOut(context) },
                        onRepositoriesClick = {
                            onRepositoriesClick.invoke()
                        }
                    )
                }

                null -> {

                }
            }
            PullToRefreshContainer(
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                indicator = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("loading-indicator")
                    )
                },
            )
        }
    }
}