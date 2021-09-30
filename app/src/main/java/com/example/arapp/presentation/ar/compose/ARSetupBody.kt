package com.example.arapp.presentation.ar.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arapp.R

@Composable
internal fun ARSetupBody(
    isARSupported: Boolean, onARClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    Column(
        modifier = Modifier
            .padding(vertical = 32.dp)
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(168.dp),
            painter = painterResource(id = R.drawable.ic_ar),
            contentDescription = null,
            tint = Color.Magenta.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "If your phone supports AR, you can start an AR session by clicking the button " +
                    "below and place different 3d models to the horizontal plane. Also you can apply " +
                    "different textures to each model!",
            fontSize = 20.sp,
            color = Color.Gray
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
        )
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onARClick,
            enabled = isARSupported,
            interactionSource = interactionSource,
            border = BorderStroke(width = 2.dp, color = Color.DarkGray),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            colors = getButtonColors(isPressed.value)
        ) {
            Text(
                text = "Start the AR session",
                color = if (isARSupported) Color.Gray else Color.LightGray.copy(alpha = 0.5f),
                style = TextStyle.Default,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (!isARSupported) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "AR is not supported for your phone!",
                color = Color.Red.copy(alpha = 0.5f),
                style = TextStyle.Default,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun getButtonColors(isPressed: Boolean): ButtonColors {
    return ButtonDefaults.outlinedButtonColors(
        backgroundColor = if (isPressed) Color.Blue.copy(alpha = 0.25f) else Color.Transparent
    )
}

@Composable
@Preview
private fun ARSetupBodyPreview() {
    Box(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        ARSetupBody(
            isARSupported = false,
            onARClick = { }
        )
    }
}
