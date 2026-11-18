package com.blacksoft.iconkit.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/** A pixel grid within crop marks, representing the rendered image size. */
val RenderResolutionIcon: ImageVector by lazy {
    ImageVector.Builder("RenderResolution", 24.dp, 24.dp, 24f, 24f).apply {
        path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(4f, 9f); verticalLineTo(6f); curveTo(4f, 4.9f, 4.9f, 4f, 6f, 4f); horizontalLineTo(9f)
            moveTo(15f, 4f); horizontalLineTo(18f); curveTo(19.1f, 4f, 20f, 4.9f, 20f, 6f); verticalLineTo(9f)
            moveTo(20f, 15f); verticalLineTo(18f); curveTo(20f, 19.1f, 19.1f, 20f, 18f, 20f); horizontalLineTo(15f)
            moveTo(9f, 20f); horizontalLineTo(6f); curveTo(4.9f, 20f, 4f, 19.1f, 4f, 18f); verticalLineTo(15f)
        }
        path(fill = SolidColor(Color.Black)) {
            moveTo(7f, 7f); horizontalLineTo(10f); verticalLineTo(10f); horizontalLineTo(7f); close()
            moveTo(10.5f, 7f); horizontalLineTo(13.5f); verticalLineTo(10f); horizontalLineTo(10.5f); close()
            moveTo(14f, 7f); horizontalLineTo(17f); verticalLineTo(10f); horizontalLineTo(14f); close()
            moveTo(7f, 10.5f); horizontalLineTo(10f); verticalLineTo(13.5f); horizontalLineTo(7f); close()
            moveTo(10.5f, 10.5f); horizontalLineTo(13.5f); verticalLineTo(13.5f); horizontalLineTo(10.5f); close()
            moveTo(14f, 10.5f); horizontalLineTo(17f); verticalLineTo(13.5f); horizontalLineTo(14f); close()
            moveTo(7f, 14f); horizontalLineTo(10f); verticalLineTo(17f); horizontalLineTo(7f); close()
            moveTo(10.5f, 14f); horizontalLineTo(13.5f); verticalLineTo(17f); horizontalLineTo(10.5f); close()
            moveTo(14f, 14f); horizontalLineTo(17f); verticalLineTo(17f); horizontalLineTo(14f); close()
        }
    }.build()
}
