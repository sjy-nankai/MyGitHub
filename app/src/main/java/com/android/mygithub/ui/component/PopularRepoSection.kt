package com.android.mygithub.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun PopularRepoSection(
    author: String,
    avatarUrl: String?,
    fullName: String,
    description: String,
    stars: String,
    language: String? = null,
) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier
                .fillMaxHeight()
                .width(50.dp)) {
                avatarUrl?.let {
                    AvatarLogo(modifier = Modifier.size(48.dp), url = it)
                }
                Text(
                    modifier = Modifier.width(48.dp),
                    text = author,
                    maxLines = 1,
                )
            }

            Spacer(Modifier.width(8.dp))

            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    text = fullName,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    text = description,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 6,
                )
                Row {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "")
                    Spacer(Modifier.width(8.dp))
                    Text(text = stars)
                    language?.let {
                        Spacer(Modifier.width(16.dp))
                        Text(text = it)
                    }
                }
            }
        }
    }
}