package com.example.textprivacy

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.privacySensitive(effect: PrivacyEffect = PrivacyEffect.Redact()): Modifier {

    val windowInfo = LocalWindowInfo.current

    val isInRecentApps by rememberUpdatedState(!windowInfo.isWindowFocused)

    return if(isInRecentApps) {
        when(effect) {
            is PrivacyEffect.Redact -> applyRedact(effect.color)
            is PrivacyEffect.Blur -> applyBlur(effect.blurRadius)
        }
    } else {
        this
    }
}

private fun Modifier.applyRedact(color: Color = Color.Black) = drawWithContent {
    drawContent()
    drawRect(color)
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.applyBlur(blurRadius: Dp = 15.dp): Modifier = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        this.blur(blurRadius)
    }
    else -> this.applyRedact(Color.LightGray)
}

sealed class PrivacyEffect {
    data class Redact(val color: Color = Color.Black): PrivacyEffect()

    data class Blur(val blurRadius: Dp = 15.dp): PrivacyEffect()
}