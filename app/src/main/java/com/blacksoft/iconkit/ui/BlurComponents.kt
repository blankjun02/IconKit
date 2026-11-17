package com.blacksoft.iconkit.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurDefaults
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.isRuntimeShaderSupported
import top.yukonga.miuix.kmp.blur.rememberLayerBackdrop
import top.yukonga.miuix.kmp.blur.textureBlur
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun rememberManagerBlurBackdrop(enabled: Boolean): LayerBackdrop? {
    if (!enabled || !isRuntimeShaderSupported()) return null
    val surface = MiuixTheme.colorScheme.surface
    return rememberLayerBackdrop { drawRect(surface); drawContent() }
}

@Composable
fun ManagerBlurredBar(backdrop: LayerBackdrop?, content: @Composable () -> Unit) {
    Box {
        if (backdrop != null) {
            Box(Modifier.matchParentSize().textureBlur(
                backdrop = backdrop,
                shape = RectangleShape,
                blurRadius = 25f,
                colors = BlurDefaults.blurColors(
                    blendColors = listOf(BlendColorEntry(MiuixTheme.colorScheme.surface.copy(alpha = .8f)))
                )
            ))
        }
        content()
    }
}
