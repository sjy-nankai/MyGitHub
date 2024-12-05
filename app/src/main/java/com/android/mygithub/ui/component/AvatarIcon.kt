package com.android.mygithub.ui.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.invisibleToUser
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AvatarLogo(
    modifier: Modifier = Modifier,
    url: String?,
) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(url).memoryCacheKey(url).apply {
            transformations(CircleCropTransformation())
        }.build()
    )
    Image(
        modifier = modifier.clearAndSetSemantics { invisibleToUser() },
        painter = painter,
        contentDescription = null
    )
}