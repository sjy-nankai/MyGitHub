package com.android.mygithub.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.mygithub.ui.component.MyTopBar
import com.android.mygithub.ui.component.RepositoryItem
import com.android.mygithub.viewmodel.PopularViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularScreen(
    paddingValues: PaddingValues,
    viewModel: PopularViewModel = viewModel(),
) {
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadPopularRepos()
    }

    if (refreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            viewModel.loadPopularRepos()
            refreshState.endRefresh()
        }
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(refreshState.nestedScrollConnection),
        topBar = {
            MyTopBar(
                title = "Popular",
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
            when {
                uiState.isRefreshing -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(44.dp)
                            .align(Alignment.Center)
                            .testTag("loading-indicator")
                    )
                }

                uiState.isError -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Error loading repositories")
                        Button(onClick = { viewModel.loadPopularRepos() }) {
                            Text("Retry")
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.padding(bottom = 80.dp),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            count = uiState.data.size,
                            key = { uiState.data[it].id },
                            contentType = { "popular-item" },
                        ) {
                            RepositoryItem(
                                uiState.data[it],
                                modifier = Modifier.padding()
                            )
                        }
                    }

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