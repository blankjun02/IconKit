/*
 * Adapted from Boundo AppIconPage.kt, Copyright 2022 Clifford Liu.
 * Licensed under the Apache License, Version 2.0. Modified for IconKit.
 */
package com.blacksoft.iconkit.ui

import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.Bitmap
import android.graphics.Canvas
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.produceState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Density
import androidx.core.graphics.PathParser
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.blacksoft.iconkit.R
import com.blacksoft.iconkit.core.InspectedApp
import com.blacksoft.iconkit.core.LoadedIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.zip.ZipFile
import top.yukonga.miuix.kmp.basic.Icon as MiuixIcon
import top.yukonga.miuix.kmp.basic.IconButton as MiuixIconButton
import top.yukonga.miuix.kmp.basic.Scaffold as MiuixScaffold
import top.yukonga.miuix.kmp.basic.Text as MiuixText
import top.yukonga.miuix.kmp.basic.TopAppBar as MiuixTopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun AppIconDetailScreen(app: InspectedApp, style: UiStyle, onBack: () -> Unit) {
    if (style == UiStyle.MATERIAL3) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(app.label.toString()) },
                    navigationIcon = { IconButton(onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } }
                )
            }
        ) { padding -> IconPageContent(app, padding, style) }
    } else {
        MiuixScaffold(
            topBar = {
                MiuixTopAppBar(
                    title = app.label.toString(),
                    navigationIcon = { MiuixIconButton(onBack) { MiuixIcon(MiuixIcons.Back, null) } }
                )
            }
        ) { padding -> IconPageContent(app, padding, style) }
    }
}

@Composable
private fun IconPageContent(app: InspectedApp, padding: PaddingValues, style: UiStyle) {
    val context = LocalContext.current
    val entries by produceState<List<BoundoIconEntry>>(emptyList(), app.packageName) {
        value = withContext(Dispatchers.IO) { loadBoundoIconEntries(context.packageManager, app) }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = padding.calculateTopPadding() + 20.dp, bottom = 40.dp)
    ) {
        items(entries, key = { it.key }) { entry ->
            val index = entries.indexOf(entry)
            if (index > 0) Spacer(Modifier.height(24.dp))
            IconSection(entry, style)
        }
    }
}

@Composable
private fun IconSection(entry: BoundoIconEntry, style: UiStyle) {
    Column(Modifier.padding(horizontal = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.horizontalScroll(rememberScrollState())) {
            entry.labels.forEachIndexed { index, label ->
                if (index > 0) Spacer(Modifier.width(8.dp))
                Text(label.name, fontWeight = FontWeight.Medium)
                Spacer(Modifier.width(4.dp))
                if (style == UiStyle.MIUIX) {
                    MiuixText(
                        label.source,
                        modifier = Modifier.background(MiuixTheme.colorScheme.tertiaryContainer.copy(alpha = .6f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        color = MiuixTheme.colorScheme.onTertiaryContainer.copy(alpha = .8f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight(750),
                    )
                } else {
                    Text(
                        label.source,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest, RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
        entry.resourceName?.let { Text(it, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Spacer(Modifier.height(10.dp))
        val drawable = entry.drawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && drawable is AdaptiveIconDrawable) {
            AdaptiveContent(drawable, style, showRuntime = entry.key == "runtime")
        } else {
            Row(Modifier.horizontalScroll(rememberScrollState())) {
                if (entry.key == "runtime") {
                    LabeledIcon(drawable, stringResource(R.string.preview_theme), RoundedCornerShape(0.dp), style)
                } else {
                    BoundoIconImage(drawable, style = style)
                }
            }
        }
    }
}

private data class BoundoIconLabel(val name: String, val source: String)
private data class BoundoIconEntry(
    val key: String,
    val resourceId: Int?,
    val drawable: Drawable,
    val resourceName: String?,
    val labels: List<BoundoIconLabel>,
)

@Suppress("DEPRECATION")
private fun loadBoundoIconEntries(packageManager: PackageManager, app: InspectedApp): List<BoundoIconEntry> {
    val info = packageManager.getApplicationInfo(app.packageName, PackageManager.MATCH_DISABLED_COMPONENTS)
    val resources = packageManager.getResourcesForApplication(info)
    fun resourceName(id: Int?): String? = id?.takeIf { it != 0 }?.let {
        runCatching {
            "R.${resources.getResourceTypeName(it)}.${resources.getResourceEntryName(it)}"
        }.getOrNull()
    }
    fun drawable(id: Int): Drawable? = runCatching { resources.getDrawable(id, null) }.getOrNull()

    val result = mutableListOf<BoundoIconEntry>()
    app.runtimeIcon.drawable?.let {
        result += BoundoIconEntry(
            key = "runtime",
            resourceId = info.icon.takeIf { id -> id != 0 },
            drawable = it,
            resourceName = resourceName(info.icon),
            labels = listOf(BoundoIconLabel("Runtime icon", "API")),
        )
    }

    val apkLabels = linkedMapOf<Int, MutableList<BoundoIconLabel>>()
    if (info.icon != 0) apkLabels.getOrPut(info.icon) { mutableListOf() } += BoundoIconLabel("Normal icon", "APK")
    val roundIconId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        runCatching {
            info.javaClass.getDeclaredMethod("getRoundIconRes").apply { isAccessible = true }.invoke(info) as Int
        }.recoverCatching {
            info.javaClass.getDeclaredField("roundIconRes").apply { isAccessible = true }.getInt(info)
        }.getOrDefault(0)
    } else 0
    if (roundIconId != 0) {
        apkLabels.getOrPut(roundIconId) { mutableListOf() } += BoundoIconLabel("Round icon", "APK")
    }
    apkLabels.forEach { (id, labels) ->
        drawable(id)?.let {
            result += BoundoIconEntry("apk:$id", id, it, resourceName(id), labels)
        }
        result += loadLegacyApkVariants(
            apkPath = info.publicSourceDir,
            resources = resources,
            resourceId = id,
            labels = labels,
        )
    }

    val definedApkIds = apkLabels.keys
    val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
    val resolves = packageManager.queryIntentActivities(launcherIntent, PackageManager.MATCH_DISABLED_COMPONENTS)
        .filter { it.activityInfo.packageName == app.packageName }
        .groupBy { it.iconResource }
    resolves.forEach { (id, matches) ->
        if (id == 0 || id in definedApkIds) return@forEach
        val labels = matches.map {
            val qualified = it.activityInfo.name.orEmpty()
            BoundoIconLabel(qualified.substringAfterLast('.').ifEmpty { "Launcher icon" }, "API")
        }
        packageManager.getDrawable(app.packageName, id, info)?.let {
            result += BoundoIconEntry("launcher:$id", id, it, resourceName(id), labels)
        }
    }
    return result
}

/**
 * Android's Resources API resolves only the best configuration for the current device. On Android
 * 8+ that usually hides the bitmap fallback behind a drawable-v26 adaptive icon with the same ID.
 * Enumerate the APK itself so those legacy bitmap variants remain visible as separate entries.
 */
private fun loadLegacyApkVariants(
    apkPath: String?,
    resources: android.content.res.Resources,
    resourceId: Int,
    labels: List<BoundoIconLabel>,
): List<BoundoIconEntry> {
    if (apkPath.isNullOrBlank()) return emptyList()
    val type = runCatching { resources.getResourceTypeName(resourceId) }.getOrNull() ?: return emptyList()
    val entry = runCatching { resources.getResourceEntryName(resourceId) }.getOrNull() ?: return emptyList()
    val pathPattern = Regex("^res/$type(?:-[^/]+)?/${Regex.escape(entry)}\\.(png|webp|jpg|jpeg)$", RegexOption.IGNORE_CASE)

    return runCatching {
        ZipFile(apkPath).use { apk ->
            apk.entries().asSequence().mapNotNull { zipEntry ->
                if (!pathPattern.matches(zipEntry.name)) return@mapNotNull null
                val bitmap = apk.getInputStream(zipEntry).use(android.graphics.BitmapFactory::decodeStream)
                    ?: return@mapNotNull null
                val qualifier = zipEntry.name.substringAfter("res/$type").substringBefore('/')
                    .removePrefix("-").ifBlank { "default" }
                BoundoIconEntry(
                    key = "legacy:$resourceId:${zipEntry.name}",
                    resourceId = resourceId,
                    drawable = BitmapDrawable(resources, bitmap),
                    resourceName = "R.$type.$entry · ${zipEntry.name}",
                    labels = labels.map { it.copy(name = "${it.name} · Legacy ($qualifier)") },
                )
            }.toList()
        }
    }.getOrDefault(emptyList())
}

@Composable
private fun AdaptiveContent(icon: AdaptiveIconDrawable, style: UiStyle, showRuntime: Boolean) {
    val unmasked = remember(icon) { renderAdaptiveLayers(icon, cropped = false) }
    val viewport = remember(icon) { renderAdaptiveLayers(icon, cropped = true) }
    val layers = buildList {
        add(icon.background to R.string.background_layer)
        add(icon.foreground to R.string.foreground_layer)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) icon.monochrome?.let { add(it to R.string.monochrome_layer) }
    }
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        layers.forEach { (drawable, label) -> LabeledIcon(drawable, stringResource(label), style = style) }
    }
    Spacer(Modifier.height(10.dp))
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        if (showRuntime) {
            LabeledIcon(icon, stringResource(R.string.preview_theme), RoundedCornerShape(0.dp), style)
        }
        val previews = listOf(
            "完整" to RoundedCornerShape(0.dp),
            "圆形" to CircleShape,
            "三星" to IconMaskShape("M50,0 C10,0 0,10 0,50 0,90 10,100 50,100 90,100 100,90 100,50 100,10 90,0 50,0 Z"),
            "水滴" to IconMaskShape("M50,0A50,50,0,0 1 100,50 L100,85 A15,15,0,0 1 85,100 L50,100 A50,50,0,0 1 50,0z"),
            "正方形" to IconMaskShape("M50,0L100,0 100,100 0,100 0,0z"),
            "小米" to IconMaskShape("M98.1,26l-.6-2.3-.2-.6-.4-1.3-.7-1.6-.9-2-.7-1.3-1.3-2.2-.5-.8-.5-.6-1.7-2.1-2.1-2-.4-.4-.5-.4L85.4,6.8,83.7,5.7,82.3,5,79.2,3.5h-.5L75.3,2.2l-1.2-.3L72,1.4h-.6L69.3,1l-2-.3h-.9l-2-.2H35.6L33.7.6h-.9l-2,.3-2.2.3H28l-2,.5-1.3.3L21.3,3.4h-.4L17.7,4.9l-1.4.8-1.6,1L12.4,8.4l-.5.4-.4.4-2.1,2L7.7,13.3l-.4.6-.6.8L5.4,16.9l-.7,1.3-.9,2-.6,1.6-.5,1.3-.2.6L1.9,26l-.4,1.6v1L.8,31.8v1.1L.5,34.3.3,36.2V63.9l.2,1.8.2,1.4v1.2l.5,3.1v1l.4,1.7.6,2.2.2.6.5,1.3.6,1.7.9,1.9.7,1.4,1.3,2.1.6.8.4.6,1.7,2.1,2.1,2.1.4.3.5.4,2.3,1.7,1.6,1,1.4.8,3.2,1.4h.4l3.4,1.1,1.2.3,2.1.5h.6l2.2.4,2,.2h.9l1.9.2H64.4l1.9-.2h.9l2-.2,2.2-.4H72l2.1-.5,1.2-.3,3.4-1.1h.4l3.2-1.4,1.4-.8,1.6-1,2.3-1.7.5-.4.4-.3,2.1-2.1,1.7-2.1.4-.6.6-.8,1.3-2.1.7-1.4.9-1.9.6-1.7.5-1.3.2-.6.6-2.2.4-1.7v-1l.5-3.1V67.1l.2-1.4.2-1.8V36.2l-.2-1.9-.2-1.4V31.8l-.5-3.2v-1Z"),
            "苹果" to IconMaskShape("M100 68.717C100 69.913 100 71.107 99.991 72.3C99.985 73.307 99.974 74.313 99.948 75.317C99.92 77.518 99.727 79.713 99.37 81.885C98.998 84.059 98.304 86.163 97.311 88.13C95.297 92.083 92.083 95.297 88.13 97.311C86.164 98.303 84.06 98.996 81.889 99.367C79.715 99.726 77.52 99.92 75.32 99.946C74.314 99.972 73.308 99.987 72.302 99.991C71.107 99.998 69.913 99.998 68.72 99.998H31.28C30.087 99.998 28.893 99.998 27.698 99.991C26.692 99.988 25.686 99.973 24.68 99.948C22.479 99.92 20.283 99.726 18.111 99.367C15.939 98.998 13.835 98.302 11.87 97.311C7.917 95.298 4.703 92.085 2.689 88.133C1.696 86.164 1.002 84.058 0.63 81.885C0.273 79.714 0.08 77.519 0.052 75.32C0.026 74.313 0.013 73.307 0.009 72.3C0 71.104 0 69.913 0 68.717V31.283C0 30.087 0 28.891 0.009 27.696C0.013 26.691 0.026 25.685 0.052 24.68C0.08 22.481 0.273 20.286 0.63 18.115C1.002 15.941 1.696 13.837 2.689 11.867C4.703 7.914 7.917 4.701 11.87 2.687C13.836 1.695 15.938 1.002 18.109 0.63C20.283 0.274 22.478 0.08 24.678 0.052C25.685 0.026 26.691 0.013 27.696 0.009C28.891 0.002 30.087 0.002 31.278 0.002H68.717C69.913 0.002 71.109 0.002 72.302 0.009C73.308 0.012 74.314 0.027 75.32 0.052C77.52 0.08 79.715 0.274 81.887 0.63C84.061 1.002 86.163 1.696 88.13 2.687C92.084 4.7 95.299 7.914 97.313 11.867C98.305 13.835 98.999 15.94 99.37 18.113C99.727 20.285 99.92 22.48 99.948 24.68C99.974 25.687 99.987 26.693 99.991 27.698C100 28.893 100 30.087 100 31.28V68.717Z"),
        )
        previews.forEachIndexed { index, (label, shape) ->
            LabeledIcon(if (index == 0) unmasked else viewport, label, shape, style)
        }
    }
}

private class IconMaskShape(private val pathData: String) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = PathParser.createPathFromPathData(pathData)
        path.transform(android.graphics.Matrix().apply { setScale(size.width / 100f, size.height / 100f) })
        return Outline.Generic(path.asComposePath())
    }
}

private fun renderAdaptiveLayers(icon: AdaptiveIconDrawable, cropped: Boolean): Drawable {
    val bitmap = Bitmap.createBitmap(192, 192, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    listOfNotNull(icon.background, icon.foreground).forEach { layer ->
        val copy = layer.constantState?.newDrawable()?.mutate() ?: layer
        val oldBounds = android.graphics.Rect(copy.bounds)
        // A 108 dp adaptive layer is viewed through the centered 72 dp viewport.
        if (cropped) copy.setBounds(-48, -48, 240, 240) else copy.setBounds(0, 0, 192, 192)
        copy.draw(canvas)
        copy.bounds = oldBounds
    }
    return BitmapDrawable(null, bitmap)
}

@Composable
private fun LabeledIcon(drawable: Drawable, label: String, shape: Shape = RoundedCornerShape(4.dp), style: UiStyle) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BoundoIconImage(drawable, shape, style)
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun BoundoIconImage(drawable: Drawable, shape: Shape = RoundedCornerShape(4.dp), style: UiStyle) {
    val bitmap = remember(drawable) { drawable.toBitmap(192, 192).asImageBitmap() }
    val back = if (style == UiStyle.MIUIX) {
        Brush.verticalGradient(listOf(MiuixTheme.colorScheme.surfaceContainer, MiuixTheme.colorScheme.surfaceContainer))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFC3EED6), Color(0xFFDDF3E7), Color.White))
    }
    Card(
        shape = RoundedCornerShape(4.dp),
        border = if (style == UiStyle.MIUIX) null else BorderStroke(.5.dp, Color(0xFF3DDC84)),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Box(
            Modifier.size(72.dp).background(back),
            contentAlignment = Alignment.Center,
        ) {
            Image(bitmap, null, Modifier.size(60.dp).clip(shape))
        }
    }
}
