package com.example.arapp.presentation.ar.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.arapp.R

@Composable
internal fun CloseButton(onClick: () -> Unit) {
    Image(
        modifier = Modifier
            .size(24.dp)
            .clickable(onClick = onClick),
        painter = painterResource(id = R.drawable.ic_close),
        contentDescription = null
    )
}