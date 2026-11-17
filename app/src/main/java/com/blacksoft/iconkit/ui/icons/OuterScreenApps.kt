// Copyright 2026, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package com.blacksoft.iconkit.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.unit.dp


private var _outerScreenAppsSurfaceLight: ImageVector? = null

private var _outerScreenAppsSurfaceNormal: ImageVector? = null

val IconKitGlassIcons.OuterScreenApps: ImageVector
    get() {
        if (_outerScreenAppsSurfaceRegular != null) return _outerScreenAppsSurfaceRegular!!
        _outerScreenAppsSurfaceRegular = buildHyperOSIcon(
            name = "OuterScreenApps.Glass.Regular",
            pathData = listOf(
                PathNode.MoveTo(784f, 763f),
                PathNode.LineTo(772f, 751f),
                PathNode.LineTo(760f, 739f),
                PathNode.QuadTo(740f, 719f, 726f, 703f),
                PathNode.QuadTo(712f, 687f, 706f, 674f),
                PathNode.QuadTo(693f, 647f, 693f, 616.5f),
                PathNode.QuadTo(693f, 586f, 706f, 559f),
                PathNode.QuadTo(712f, 546f, 726f, 530f),
                PathNode.QuadTo(740f, 514f, 760f, 494f),
                PathNode.LineTo(772f, 482f),
                PathNode.LineTo(784f, 470f),
                PathNode.QuadTo(804f, 450f, 820f, 436f),
                PathNode.QuadTo(836f, 422f, 849f, 416f),
                PathNode.QuadTo(876f, 403f, 906.5f, 403f),
                PathNode.QuadTo(937f, 403f, 964f, 416f),
                PathNode.QuadTo(977f, 422f, 993f, 436f),
                PathNode.QuadTo(1009f, 450f, 1029f, 470f),
                PathNode.LineTo(1041f, 482f),
                PathNode.LineTo(1053f, 494f),
                PathNode.QuadTo(1073f, 514f, 1087f, 530f),
                PathNode.QuadTo(1101f, 546f, 1107f, 559f),
                PathNode.QuadTo(1120f, 586f, 1120f, 616.5f),
                PathNode.QuadTo(1120f, 647f, 1107f, 674f),
                PathNode.QuadTo(1101f, 687f, 1087f, 703f),
                PathNode.QuadTo(1073f, 719f, 1053f, 739f),
                PathNode.LineTo(1041f, 751f),
                PathNode.LineTo(1029f, 763f),
                PathNode.QuadTo(1009f, 783f, 993f, 797f),
                PathNode.QuadTo(977f, 811f, 964f, 817f),
                PathNode.QuadTo(937f, 830f, 906.5f, 830f),
                PathNode.QuadTo(876f, 830f, 849f, 817f),
                PathNode.QuadTo(836f, 811f, 820f, 797f),
                PathNode.QuadTo(804f, 783f, 784f, 763f),
                PathNode.Close,
                PathNode.MoveTo(795f, 303f),
                PathNode.QuadTo(769f, 292f, 749.5f, 272.5f),
                PathNode.QuadTo(730f, 253f, 720f, 227f),
                PathNode.QuadTo(714f, 211f, 712.5f, 189.5f),
                PathNode.QuadTo(711f, 168f, 711f, 116f),
                PathNode.QuadTo(711f, 65f, 712.5f, 43f),
                PathNode.QuadTo(714f, 21f, 720f, 5f),
                PathNode.QuadTo(730f, -21f, 749.5f, -40.5f),
                PathNode.QuadTo(769f, -60f, 795f, -71f),
                PathNode.QuadTo(812f, -77f, 834f, -78.5f),
                PathNode.QuadTo(856f, -80f, 907f, -80f),
                PathNode.QuadTo(958f, -80f, 980f, -78.5f),
                PathNode.QuadTo(1002f, -77f, 1018f, -71f),
                PathNode.QuadTo(1044f, -60f, 1063.5f, -40.5f),
                PathNode.QuadTo(1083f, -21f, 1093f, 5f),
                PathNode.QuadTo(1099f, 21f, 1100.5f, 43f),
                PathNode.QuadTo(1102f, 65f, 1102f, 116f),
                PathNode.QuadTo(1102f, 168f, 1100.5f, 189.5f),
                PathNode.QuadTo(1099f, 211f, 1093f, 227f),
                PathNode.QuadTo(1083f, 253f, 1063.5f, 272.5f),
                PathNode.QuadTo(1044f, 292f, 1018f, 303f),
                PathNode.QuadTo(1002f, 309f, 980f, 310.5f),
                PathNode.QuadTo(958f, 312f, 907f, 312f),
                PathNode.QuadTo(856f, 312f, 834f, 310.5f),
                PathNode.QuadTo(812f, 309f, 795f, 303f),
                PathNode.Close,
                PathNode.MoveTo(295f, 803f),
                PathNode.QuadTo(269f, 793f, 249.5f, 773.5f),
                PathNode.QuadTo(230f, 754f, 219f, 728f),
                PathNode.QuadTo(213f, 712f, 211.5f, 690f),
                PathNode.QuadTo(210f, 668f, 210f, 617f),
                PathNode.QuadTo(210f, 566f, 211.5f, 544f),
                PathNode.QuadTo(213f, 522f, 219f, 505f),
                PathNode.QuadTo(230f, 479f, 249.5f, 459.5f),
                PathNode.QuadTo(269f, 440f, 295f, 430f),
                PathNode.QuadTo(311f, 424f, 333f, 422.5f),
                PathNode.QuadTo(355f, 421f, 406f, 421f),
                PathNode.QuadTo(458f, 421f, 479.5f, 422.5f),
                PathNode.QuadTo(501f, 424f, 517f, 430f),
                PathNode.QuadTo(543f, 440f, 562.5f, 459.5f),
                PathNode.QuadTo(582f, 479f, 593f, 505f),
                PathNode.QuadTo(599f, 522f, 600.5f, 544f),
                PathNode.QuadTo(602f, 566f, 602f, 617f),
                PathNode.QuadTo(602f, 668f, 600.5f, 690f),
                PathNode.QuadTo(599f, 712f, 593f, 728f),
                PathNode.QuadTo(582f, 754f, 562.5f, 773.5f),
                PathNode.QuadTo(543f, 793f, 517f, 803f),
                PathNode.QuadTo(501f, 810f, 479.5f, 811.5f),
                PathNode.QuadTo(458f, 813f, 406f, 813f),
                PathNode.QuadTo(355f, 813f, 333f, 811.5f),
                PathNode.QuadTo(311f, 810f, 295f, 803f),
                PathNode.Close,
                PathNode.MoveTo(295f, 303f),
                PathNode.QuadTo(269f, 293f, 249.5f, 273f),
                PathNode.QuadTo(230f, 253f, 219f, 227f),
                PathNode.QuadTo(213f, 211f, 211.5f, 189.5f),
                PathNode.QuadTo(210f, 168f, 210f, 116f),
                PathNode.QuadTo(210f, 65f, 211.5f, 43f),
                PathNode.QuadTo(213f, 21f, 219f, 5f),
                PathNode.QuadTo(230f, -21f, 249.5f, -41f),
                PathNode.QuadTo(269f, -61f, 295f, -71f),
                PathNode.QuadTo(311f, -77f, 333f, -78.5f),
                PathNode.QuadTo(355f, -80f, 406f, -80f),
                PathNode.QuadTo(458f, -80f, 479.5f, -78.5f),
                PathNode.QuadTo(501f, -77f, 517f, -71f),
                PathNode.QuadTo(543f, -61f, 563f, -41f),
                PathNode.QuadTo(583f, -21f, 593f, 5f),
                PathNode.QuadTo(599f, 21f, 600.5f, 43f),
                PathNode.QuadTo(602f, 65f, 602f, 116f),
                PathNode.QuadTo(602f, 168f, 600.5f, 189.5f),
                PathNode.QuadTo(599f, 211f, 593f, 227f),
                PathNode.QuadTo(583f, 253f, 563f, 273f),
                PathNode.QuadTo(543f, 293f, 517f, 303f),
                PathNode.QuadTo(501f, 309f, 479.5f, 310.5f),
                PathNode.QuadTo(458f, 312f, 406f, 312f),
                PathNode.QuadTo(355f, 312f, 333f, 310.5f),
                PathNode.QuadTo(311f, 309f, 295f, 303f),
                PathNode.Close,
            ),
            scale = 1f,
            translationX = 1.6667f,
            translationY = 1041.6667f,
            autoMirror = false,
        )
        return _outerScreenAppsSurfaceRegular!!
    }

private var _outerScreenAppsSurfaceRegular: ImageVector? = null

private var _outerScreenAppsSurfaceMedium: ImageVector? = null

private var _outerScreenAppsSurfaceDemibold: ImageVector? = null


