package com.blacksoft.iconkit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.futured.donut.compose.DonutProgress
import app.futured.donut.compose.data.DonutModel
import app.futured.donut.compose.data.DonutSection
import com.blacksoft.iconkit.R
import com.blacksoft.iconkit.core.IconSource
import com.blacksoft.iconkit.core.IconStatisticsCalculator
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun StatisticsPage(preferences: AppPreferences, modifier: Modifier = Modifier, contentPadding: PaddingValues = PaddingValues(0.dp)) {
    val viewModel = viewModel<AppsViewModel>()
    LaunchedEffect(Unit) { viewModel.loadIfNeeded() }

    if (!viewModel.hasLoaded && viewModel.isRefreshing) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    val source = if (preferences.iconDetectionSource == IconDetectionSource.APK) {
        IconSource.APK_RESOURCE
    } else {
        IconSource.RUNTIME
    }
    val statistics = remember(viewModel.apps, source) {
        IconStatisticsCalculator.calculate(viewModel.apps, source)
    }
    val isMiuix = preferences.uiStyle == UiStyle.MIUIX
    val adaptiveColor = if (isMiuix) MiuixTheme.colorScheme.primary else MaterialTheme.colorScheme.primary
    val legacyColor = if (isMiuix) MiuixTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.tertiary
    val surfaceColor = if (isMiuix) MiuixTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surfaceContainer
    val textColor = if (isMiuix) MiuixTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface
    val secondaryTextColor = if (isMiuix) MiuixTheme.colorScheme.onSurfaceVariantSummary else MaterialTheme.colorScheme.onSurfaceVariant
    val ratio = statistics.adaptiveRatio
    val density = LocalDensity.current
    val strokeWidthPx = with(density) { 28.dp.toPx() }
    val chartModel = remember(statistics.adaptiveApps, statistics.legacyApps, adaptiveColor, legacyColor, strokeWidthPx) {
        DonutModel(
            cap = statistics.inspectedApps.coerceAtLeast(1).toFloat(),
            gapWidthDegrees = 2f,
            gapAngleDegrees = 270f,
            strokeWidth = strokeWidthPx,
            backgroundLineColor = secondaryTextColor.copy(alpha = .12f),
            sections = listOf(
                DonutSection(statistics.adaptiveApps.toFloat(), adaptiveColor),
                DonutSection(statistics.legacyApps.toFloat(), legacyColor),
            ),
        )
    }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(contentPadding).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(surfaceColor, RoundedCornerShape(24.dp)).padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            DonutProgress(model = chartModel, modifier = Modifier.size(220.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${"%.1f".format(ratio * 100)}%", color = textColor, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(stringResource(R.string.adaptive_icon), color = secondaryTextColor)
            }
        }
        Spacer(Modifier.height(18.dp))
        StatisticLegendRow(stringResource(R.string.adaptive_icon), statistics.adaptiveApps, ratio, adaptiveColor, surfaceColor, textColor, secondaryTextColor)
        Spacer(Modifier.height(10.dp))
        StatisticLegendRow(stringResource(R.string.legacy_icon), statistics.legacyApps, statistics.legacyRatio, legacyColor, surfaceColor, textColor, secondaryTextColor)
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(R.string.statistics_checked, statistics.inspectedApps, statistics.totalApps) +
                if (statistics.unavailableApps > 0) stringResource(R.string.statistics_unavailable, statistics.unavailableApps) else "",
            color = secondaryTextColor,
        )
    }
}

@Composable
private fun StatisticLegendRow(
    label: String,
    count: Int,
    ratio: Double,
    color: Color,
    surfaceColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
) {
    Row(
        Modifier.fillMaxWidth().background(surfaceColor, RoundedCornerShape(18.dp)).padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(Modifier.size(12.dp).background(color, CircleShape))
        Column(Modifier.weight(1f)) {
            Text(label, color = textColor, fontWeight = FontWeight.Medium)
            Text("${"%.1f".format(ratio * 100)}%", color = secondaryTextColor)
        }
        Text(count.toString(), color = textColor, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
    }
}
