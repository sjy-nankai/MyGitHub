package com.android.mygithub.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.mygithub.ui.component.RepositoryItem
import com.android.mygithub.viewmodel.RepositoriesResult
import com.android.mygithub.viewmodel.RepositoriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoriesScreen(
    onBackClick: () -> Unit,
    viewModel: RepositoriesViewModel = viewModel(),
) {
    val repos by viewModel.repositories.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.loadRepositories(context)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Repositories") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val reposState = repos) {
            is RepositoriesResult.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is RepositoriesResult.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        count = reposState.data.size,
                        key = { reposState.data[it].id },
                        contentType = { "user-repository" }
                    ) { repo ->
                        RepositoryItem(reposState.data[repo])
                    }
                }
            }

            is RepositoriesResult.Error -> {
                Text(
                    text = "Error: ${reposState.message}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
