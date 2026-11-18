package com.blacksoft.iconkit.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurDefaults
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.material.isRuntimeShaderSupported
import top.yukonga.miuix.kmp.blur.rememberLayerBackdrop
import top.yukonga.miuix.kmp.blur.textureBlur
import top.yukonga.miuix.kmp.blur.progressiveTextureBlur
import top.yukonga.miuix.kmp.blur.ProgressiveBlur
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun rememberManagerBlurBackdrop(enabled: Boolean): LayerBackdrop? {
    if (!enabled || !isRuntimeShaderSupported()) return null
    val surface = MiuixTheme.colorScheme.surface
    return rememberLayerBackdrop { drawRect(surface); drawContent() }
}

@Composable
fun ManagerBlurredBar(backdrop: LayerBackdrop?, progressive: Boolean = false, content: @Composable () -> Unit) {
    Box {
        if (backdrop != null) {
            val colors = BlurDefaults.blurColors(
                blendColors = listOf(BlendColorEntry(MiuixTheme.colorScheme.surface.copy(alpha = .8f)))
            )
            Box(if (progressive) Modifier.matchParentSize().progressiveTextureBlur(
                backdrop = backdrop,
                shape = RectangleShape,
                blurRadius = 25f,
                gradient = ProgressiveBlur.Top,
                colors = colors,
            ) else Modifier.matchParentSize().textureBlur(
                backdrop = backdrop,
                shape = RectangleShape,
                blurRadius = 25f,
                colors = colors,
            ))
        }
        content()
    }
}
