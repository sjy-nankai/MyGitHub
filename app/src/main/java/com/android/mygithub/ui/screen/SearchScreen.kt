package com.android.mygithub.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.mygithub.ui.component.MySearchBar
import com.android.mygithub.ui.component.MyTopBar
import com.android.mygithub.ui.component.RepositoryItem
import com.android.mygithub.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    paddingValues: PaddingValues,
    viewModel: SearchViewModel = viewModel(),
) {
    Scaffold(
        topBar = {
            MyTopBar(
                title = "Search",
                paddingValues = paddingValues,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize(),
        ) {
            val uiState by viewModel.uiState.collectAsState()
            val languages = listOf("Kotlin", "Java", "Python", "JavaScript", "Swift", "Go")

            Column(modifier = Modifier.fillMaxSize()) {
                MySearchBar(
                    query = uiState.query,
                    onQueryChange = viewModel::onQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = uiState.selectedLanguage == null,
                            onClick = { viewModel.onLanguageSelect(null) },
                            label = { Text("All") }
                        )
                    }
                    items(languages.size) { language ->
                        FilterChip(
                            selected = uiState.selectedLanguage == languages[language],
                            onClick = { viewModel.onLanguageSelect(languages[language]) },
                            label = { Text(languages[language]) }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        count = uiState.repositories.size,
                        key = { uiState.repositories[it].id },
                        contentType = { "search-item" }
                    ) { repo ->
                        RepositoryItem(uiState.repositories[repo])
                    }

                    if (uiState.isLoading) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

                uiState.error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

        }
    }
}