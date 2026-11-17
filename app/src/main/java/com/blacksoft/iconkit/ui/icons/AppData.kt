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


private var _appDataSurfaceLight: ImageVector? = null

private var _appDataSurfaceNormal: ImageVector? = null

val IconKitGlassIcons.AppData: ImageVector
    get() {
        if (_appDataSurfaceRegular != null) return _appDataSurfaceRegular!!
        _appDataSurfaceRegular = buildHyperOSIcon(
            name = "AppData.Glass.Regular",
            pathData = listOf(
                PathNode.MoveTo(1163f, 342f),
                PathNode.HorizontalTo(655f),
                PathNode.QuadTo(641f, 342f, 636.5f, 350f),
                PathNode.QuadTo(632f, 358f, 632f, 376f),
                PathNode.VerticalTo(874f),
                PathNode.QuadTo(503f, 866f, 396.5f, 797f),
                PathNode.QuadTo(290f, 728f, 228f, 617f),
                PathNode.QuadTo(166f, 506f, 166f, 376f),
                PathNode.QuadTo(166f, 240f, 233f, 125.5f),
                PathNode.QuadTo(300f, 11f, 414.5f, -56.5f),
                PathNode.QuadTo(529f, -124f, 665f, -124f),
                PathNode.QuadTo(795f, -124f, 906f, -62f),
                PathNode.QuadTo(1017f, 0f, 1085.5f, 107f),
                PathNode.QuadTo(1154f, 214f, 1163f, 342f),
                PathNode.Close,
                PathNode.MoveTo(698f, 874f),
                PathNode.VerticalTo(409f),
                PathNode.HorizontalTo(1163f),
                PathNode.QuadTo(1155f, 532f, 1091.5f, 635.5f),
                PathNode.QuadTo(1028f, 739f, 924.5f, 802.5f),
                PathNode.QuadTo(821f, 866f, 698f, 874f),
                PathNode.Close,
            ),
            scale = 1f,
            translationX = 2.1667f,
            translationY = 1041.6667f,
            autoMirror = false,
        )
        return _appDataSurfaceRegular!!
    }

private var _appDataSurfaceRegular: ImageVector? = null

private var _appDataSurfaceMedium: ImageVector? = null

private var _appDataSurfaceDemibold: ImageVector? = null


