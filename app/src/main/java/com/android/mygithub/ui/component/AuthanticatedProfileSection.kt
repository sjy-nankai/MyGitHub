package com.android.mygithub.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.github.domain.model.User

@Composable
fun AuthenticatedProfileView(
    user: User,
    onLogoutClick: () -> Unit,
    onRepositoriesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarLogo(url = user.avatarUrl)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = user.name ?: user.username,
                    style = MaterialTheme.typography.titleLarge
                )
                user.bio?.let { bio ->
                    Text(
                        text = bio,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRepositoriesClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                Text("My Repositories")
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Text("Sign Out")
            }
        }
    }
}