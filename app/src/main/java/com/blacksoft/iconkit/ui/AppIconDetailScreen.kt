/*
 * Adapted from Boundo AppIconPage.kt, Copyright 2022 Clifford Liu.
 * Licensed under the Apache License, Version 2.0. Modified for IconKit.
 */
package com.blacksoft.iconkit.ui

import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ColorDrawable
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.content.Intent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import android.content.pm.PackageManager
import android.os.Build
import android.util.TypedValue
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.BlurOff
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.Path
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
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
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.layerBackdrop

private val LocalInvertPreviewBackground = staticCompositionLocalOf { false }
private val LocalShowPreviewShadow = staticCompositionLocalOf { false }
private val LocalIconRenderSize = staticCompositionLocalOf { 512 }
internal data class OriginalIconResource(val apkPath: String, val entryPath: String) {
    val filename: String get() = entryPath.substringAfterLast('/')
}
internal data class IconPreviewSelection(val bitmap: Bitmap, val original: OriginalIconResource?, val backgroundColor: Int? = null)
private val LocalPreviewClick = staticCompositionLocalOf<(IconPreviewSelection) -> Unit> { {} }

@Composable
fun AppIconDetailScreen(app: InspectedApp, style: UiStyle, enableBlur: Boolean, renderSize: Int, onBack: () -> Unit) {
    var inverted by rememberSaveable(app.packageName) { mutableStateOf(false) }
    var showShadow by rememberSaveable(app.packageName) { mutableStateOf(false) }
    var selectedPreview by remember { mutableStateOf<IconPreviewSelection?>(null) }
    val previewClick: (IconPreviewSelection) -> Unit = { selectedPreview = it }
    if (style == UiStyle.MATERIAL3) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(app.label.toString()) },
                    navigationIcon = { IconButton(onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                    actions = {
                        IconButton(onClick = { showShadow = !showShadow }) { Icon(if (showShadow) Icons.Filled.BlurOn else Icons.Filled.BlurOff, stringResource(if (showShadow) R.string.hide_preview_shadow else R.string.show_preview_shadow)) }
                        IconButton(onClick = { inverted = !inverted }) { Icon(Icons.Filled.Contrast, stringResource(R.string.invert_preview_background)) }
                    },
                )
            }
        ) { padding ->
            CompositionLocalProvider(LocalInvertPreviewBackground provides inverted, LocalShowPreviewShadow provides showShadow, LocalIconRenderSize provides renderSize, LocalPreviewClick provides previewClick) { IconPageContent(app, padding, style) }
            IconPreviewActions(selectedPreview, app.label.toString(), style) { selectedPreview = null }
        }
    } else {
        val backdrop = rememberManagerBlurBackdrop(enableBlur)
        MiuixScaffold(
            topBar = {
                ManagerBlurredBar(backdrop) {
                    MiuixTopAppBar(
                        title = app.label.toString(),
                        color = if (backdrop != null) Color.Transparent else MiuixTheme.colorScheme.surface,
                        navigationIcon = { MiuixIconButton(onBack) { MiuixIcon(MiuixIcons.Back, null) } },
                        actions = {
                            MiuixIconButton(onClick = { showShadow = !showShadow }) { MiuixIcon(if (showShadow) Icons.Filled.BlurOn else Icons.Filled.BlurOff, stringResource(if (showShadow) R.string.hide_preview_shadow else R.string.show_preview_shadow)) }
                            MiuixIconButton(onClick = { inverted = !inverted }) { MiuixIcon(Icons.Filled.Contrast, stringResource(R.string.invert_preview_background)) }
                        },
                    )
                }
            }
        ) { padding ->
            CompositionLocalProvider(LocalInvertPreviewBackground provides inverted, LocalShowPreviewShadow provides showShadow, LocalIconRenderSize provides renderSize, LocalPreviewClick provides previewClick) { IconPageContent(app, padding, style, backdrop) }
            IconPreviewActions(selectedPreview, app.label.toString(), style) { selectedPreview = null }
        }
    }
}

@Composable
private fun IconPageContent(app: InspectedApp, padding: PaddingValues, style: UiStyle, backdrop: LayerBackdrop? = null) {
    val context = LocalContext.current
    val entries by produceState<List<BoundoIconEntry>>(emptyList(), app.packageName) {
        value = withContext(Dispatchers.IO) { loadBoundoIconEntries(context.packageManager, app) }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().then(if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier),
        contentPadding = PaddingValues(top = padding.calculateTopPadding() + 20.dp, bottom = padding.calculateBottomPadding() + 40.dp)
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
    Column(Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
            entry.labels.forEachIndexed { index, label ->
                if (index > 0) Spacer(Modifier.width(8.dp))
                ActivityIconTitle(label, style)
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
        entry.resourceName?.let {
            if (style == UiStyle.MIUIX) {
                MiuixText(it, modifier = Modifier.padding(horizontal = 20.dp), color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
            } else {
                Text(it, modifier = Modifier.padding(horizontal = 20.dp), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        entry.original?.takeUnless { entry.resourceName?.contains(it.entryPath) == true }?.let { original ->
            val format = original.filename.substringAfterLast('.', "").uppercase()
            val text = "$format · ${original.entryPath}"
            if (style == UiStyle.MIUIX) {
                MiuixText(text, modifier = Modifier.padding(horizontal = 20.dp), color = MiuixTheme.colorScheme.onSurfaceVariantSummary, fontSize = 12.sp)
            } else {
                Text(text, modifier = Modifier.padding(horizontal = 20.dp), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(10.dp))
        val drawable = entry.drawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && drawable is AdaptiveIconDrawable) {
            AdaptiveContent(drawable, style, showRuntime = entry.key == "runtime", originalLayers = entry.originalLayers)
        } else {
            Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
                if (entry.key == "runtime") {
                    LabeledIcon(drawable, stringResource(R.string.preview_theme), RoundedCornerShape(0.dp), style)
                } else {
                    BoundoIconImage(drawable, style = style, original = entry.original)
                }
            }
        }
    }
}

@Composable
private fun ActivityIconTitle(label: BoundoIconLabel, style: UiStyle) {
    val context = LocalContext.current
    var showClassName by remember(label.fullName) { mutableStateOf(false) }
    Box {
        val titleModifier = if (label.fullName != null) Modifier.clickable { showClassName = true } else Modifier
        if (style == UiStyle.MIUIX) {
            MiuixText(label.name, modifier = titleModifier, color = MiuixTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
        } else {
            Text(label.name, modifier = titleModifier, fontWeight = FontWeight.Medium)
        }
        val fullName = label.fullName
        if (showClassName && fullName != null) {
            val offset = with(LocalDensity.current) { 20.dp.roundToPx() }
            Popup(offset = IntOffset(0, offset), onDismissRequest = { showClassName = false }, properties = PopupProperties(focusable = true)) {
                val background = if (style == UiStyle.MIUIX) MiuixTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surfaceContainerHigh
                val foreground = if (style == UiStyle.MIUIX) MiuixTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface
                Text(
                    text = fullName,
                    color = foreground,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(background)
                        .clickable {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Activity", fullName))
                            showClassName = false
                            Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show()
                        }
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                )
            }
        }
    }
}

private data class BoundoIconLabel(val name: String, val source: String, val fullName: String? = null)
private data class BoundoIconEntry(
    val key: String,
    val resourceId: Int?,
    val drawable: Drawable,
    val resourceName: String?,
    val labels: List<BoundoIconLabel>,
    val original: OriginalIconResource? = null,
    val originalLayers: Map<String, OriginalIconResource> = emptyMap(),
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
    val apkPaths = listOfNotNull(info.publicSourceDir) + (info.splitPublicSourceDirs?.toList() ?: emptyList())
    fun original(id: Int): OriginalIconResource? = runCatching {
        val value = TypedValue()
        resources.getValue(id, value, true)
        val path = value.string?.toString()?.takeIf { it.startsWith("res/") } ?: return@runCatching null
        apkPaths.firstNotNullOfOrNull { apkPath ->
            ZipFile(apkPath).use { zip -> if (zip.getEntry(path) != null) OriginalIconResource(apkPath, path) else null }
        }
    }.getOrNull()
    fun layers(id: Int): Map<String, OriginalIconResource> = runCatching {
        val found = mutableMapOf<String, OriginalIconResource>()
        resources.getXml(id).use { parser ->
            while (parser.next() != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
                if (parser.eventType != org.xmlpull.v1.XmlPullParser.START_TAG || parser.name !in setOf("background", "foreground", "monochrome")) continue
                val layerId = parser.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawable", 0)
                if (layerId != 0) original(layerId)?.let { found[parser.name] = it }
            }
        }
        found.toMap()
    }.getOrDefault(emptyMap())

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
            result += BoundoIconEntry("apk:$id", id, it, resourceName(id), labels, original(id), layers(id))
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
            BoundoIconLabel(qualified.substringAfterLast('.').ifEmpty { "Launcher icon" }, "API", qualified.takeIf { name -> name.isNotBlank() })
        }
        packageManager.getDrawable(app.packageName, id, info)?.let {
            result += BoundoIconEntry("launcher:$id", id, it, resourceName(id), labels, original(id), layers(id))
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
                    original = OriginalIconResource(apkPath, zipEntry.name),
                )
            }.toList()
        }
    }.getOrDefault(emptyList())
}

@Composable
private fun AdaptiveContent(icon: AdaptiveIconDrawable, style: UiStyle, showRuntime: Boolean, originalLayers: Map<String, OriginalIconResource>) {
    val unmasked = remember(icon) { renderAdaptiveLayers(icon, cropped = false) }
    val viewport = remember(icon) { renderAdaptiveLayers(icon, cropped = true) }
    val layers = buildList {
        add(icon.background to R.string.background_layer)
        add(icon.foreground to R.string.foreground_layer)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) icon.monochrome?.let { add(it to R.string.monochrome_layer) }
    }
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        layers.forEach { (drawable, label) ->
            val shape = if (label == R.string.background_layer || label == R.string.foreground_layer) RoundedCornerShape(0.dp) else RoundedCornerShape(4.dp)
            val layerName = when (label) { R.string.background_layer -> "background"; R.string.foreground_layer -> "foreground"; else -> "monochrome" }
            LabeledIcon(drawable, stringResource(label), shape, style, originalLayers[layerName], label == R.string.background_layer)
        }
    }
    Spacer(Modifier.height(10.dp))
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        if (showRuntime) {
            LabeledIcon(icon, stringResource(R.string.preview_theme), RoundedCornerShape(0.dp), style)
        }
        val previews = listOf(
            "完整" to RoundedCornerShape(0.dp),
            "正方形" to IconMaskShape("M50,0L100,0 100,100 0,100 0,0z"),
            "酷派" to IconMaskShape("M0 46.5409C0 24.6013 0 13.6315 6.81575 6.81575C13.6315 0 24.6013 0 46.5409 0H53.4591C75.3987 0 86.3685 0 93.1842 6.81575C100 13.6315 100 24.6013 100 46.5409V53.4591C100 75.3987 100 86.3685 93.1842 93.1842C86.3685 100 75.3987 100 53.4591 100H46.5409C24.6013 100 13.6315 100 6.81575 93.1842C0 86.3685 0 75.3987 0 53.4591V46.5409Z"),
            "圆形" to CircleShape,
            "三星" to IconMaskShape("M50,0 C10,0 0,10 0,50 0,90 10,100 50,100 90,100 100,90 100,50 100,10 90,0 50,0 Z"),
            "水滴" to IconMaskShape("M50,0A50,50,0,0 1 100,50 L100,85 A15,15,0,0 1 85,100 L50,100 A50,50,0,0 1 50,0z"),
            "圆筒" to IconMaskShape("M50,0A50,30 0,0,1 100,30V70A50,30 0,0,1 0,70V30A50,30 0,0,1 50,0z"),
            "小米" to IconMaskShape("M723.79,84.42C647.55,8.48,537.94,0,404,0,269.89,0,160.12,8.58,83.92,84.72S0,270.43,0,404.39,7.74,648,84,724.14,269.9,808,404,808s243.85-7.71,320-83.86,84-185.78,84-319.75C808,270.25,800.16,160.54,723.79,84.42Z", viewportSize = 808f),
            "苹果" to IconMaskShape("M100 68.717C100 69.913 100 71.107 99.991 72.3C99.985 73.307 99.974 74.313 99.948 75.317C99.92 77.518 99.727 79.713 99.37 81.885C98.998 84.059 98.304 86.163 97.311 88.13C95.297 92.083 92.083 95.297 88.13 97.311C86.164 98.303 84.06 98.996 81.889 99.367C79.715 99.726 77.52 99.92 75.32 99.946C74.314 99.972 73.308 99.987 72.302 99.991C71.107 99.998 69.913 99.998 68.72 99.998H31.28C30.087 99.998 28.893 99.998 27.698 99.991C26.692 99.988 25.686 99.973 24.68 99.948C22.479 99.92 20.283 99.726 18.111 99.367C15.939 98.998 13.835 98.302 11.87 97.311C7.917 95.298 4.703 92.085 2.689 88.133C1.696 86.164 1.002 84.058 0.63 81.885C0.273 79.714 0.08 77.519 0.052 75.32C0.026 74.313 0.013 73.307 0.009 72.3C0 71.104 0 69.913 0 68.717V31.283C0 30.087 0 28.891 0.009 27.696C0.013 26.691 0.026 25.685 0.052 24.68C0.08 22.481 0.273 20.286 0.63 18.115C1.002 15.941 1.696 13.837 2.689 11.867C4.703 7.914 7.917 4.701 11.87 2.687C13.836 1.695 15.938 1.002 18.109 0.63C20.283 0.274 22.478 0.08 24.678 0.052C25.685 0.026 26.691 0.013 27.696 0.009C28.891 0.002 30.087 0.002 31.278 0.002H68.717C69.913 0.002 71.109 0.002 72.302 0.009C73.308 0.012 74.314 0.027 75.32 0.052C77.52 0.08 79.715 0.274 81.887 0.63C84.061 1.002 86.163 1.696 88.13 2.687C92.084 4.7 95.299 7.914 97.313 11.867C98.305 13.835 98.999 15.94 99.37 18.113C99.727 20.285 99.92 22.48 99.948 24.68C99.974 25.687 99.987 26.693 99.991 27.698C100 28.893 100 30.087 100 31.28V68.717Z"),
        )
        val order = listOf("完整", "正方形", "苹果", "酷派", "小米", "三星", "水滴", "圆形", "圆筒")
        previews.sortedBy { order.indexOf(it.first) }.forEach { (label, shape) ->
            val labelRes = when (label) {
                "完整" -> R.string.preview_full
                "正方形" -> R.string.preview_square
                "酷派" -> R.string.preview_coolpad
                "苹果" -> R.string.preview_apple
                "三星" -> R.string.preview_samsung
                "小米" -> R.string.preview_xiaomi
                "水滴" -> R.string.preview_teardrop
                "圆形" -> R.string.preview_round
                else -> R.string.preview_cylinder
            }
            LabeledIcon(if (label == "完整") unmasked else viewport, stringResource(labelRes), shape, style, isShapePreview = label != "完整")
        }
    }
}

private class IconMaskShape(private val pathData: String, private val viewportSize: Float = 100f) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = PathParser.createPathFromPathData(pathData)
        path.transform(android.graphics.Matrix().apply { setScale(size.width / viewportSize, size.height / viewportSize) })
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
private fun LabeledIcon(drawable: Drawable, label: String, shape: Shape = RoundedCornerShape(4.dp), style: UiStyle, original: OriginalIconResource? = null, isBackground: Boolean = false, isShapePreview: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BoundoIconImage(drawable, shape, style, original, isBackground, isShapePreview)
        Spacer(Modifier.height(4.dp))
        if (style == UiStyle.MIUIX) {
            MiuixText(label, color = MiuixTheme.colorScheme.onSurface, fontSize = 11.sp)
        } else {
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun BoundoIconImage(drawable: Drawable, shape: Shape = RoundedCornerShape(4.dp), style: UiStyle, original: OriginalIconResource? = null, isBackground: Boolean = false, isShapePreview: Boolean = false) {
    val bitmap = remember(drawable) { drawable.toBitmap(192, 192).asImageBitmap() }
    val onPreviewClick = LocalPreviewClick.current
    val density = LocalDensity.current
    val direction = LocalLayoutDirection.current
    val showShadow = LocalShowPreviewShadow.current && isShapePreview
    val renderSize = LocalIconRenderSize.current
    val shadow = remember(shape, density, direction, showShadow) {
        if (showShadow) createPreviewShadow(shape, density, direction).asImageBitmap() else null
    }
    val inverted = LocalInvertPreviewBackground.current
    fun Color.invertIfNeeded(): Color = if (inverted) Color(1f - red, 1f - green, 1f - blue, alpha) else this
    val miuixBackground by animateColorAsState(
        if (style == UiStyle.MIUIX) MiuixTheme.colorScheme.surfaceContainer.invertIfNeeded() else Color.Transparent,
        animationSpec = tween(300), label = "previewBackground",
    )
    val miuixLight = style == UiStyle.MIUIX && MiuixTheme.colorScheme.surfaceContainer.luminance() > .5f
    val miuixGradientStart by animateColorAsState(
        (if (miuixLight) Color(0xFFE8E8E8) else Color.Black).invertIfNeeded(),
        tween(300), label = "miuixPreviewGradientStart",
    )
    val miuixGradientEnd by animateColorAsState(
        (if (miuixLight) Color.White else Color(0xFF303030)).invertIfNeeded(),
        tween(300), label = "miuixPreviewGradientEnd",
    )
    val gradientStart by animateColorAsState(Color(0xFFC3EED6).invertIfNeeded(), tween(300), label = "previewGradientStart")
    val gradientMiddle by animateColorAsState(Color(0xFFDDF3E7).invertIfNeeded(), tween(300), label = "previewGradientMiddle")
    val gradientEnd by animateColorAsState(Color.White.invertIfNeeded(), tween(300), label = "previewGradientEnd")
    val back = if (style == UiStyle.MIUIX) {
        Brush.verticalGradient(listOf(miuixGradientStart, miuixGradientEnd))
    } else {
        Brush.verticalGradient(listOf(gradientStart, gradientMiddle, gradientEnd))
    }
    Card(
        shape = RoundedCornerShape(4.dp),
        border = if (style == UiStyle.MIUIX) BorderStroke(.5.dp, MiuixTheme.colorScheme.outline.copy(alpha = .25f)) else BorderStroke(.5.dp, Color(0xFF3DDC84)),
        colors = if (style == UiStyle.MIUIX) CardDefaults.cardColors(containerColor = miuixBackground) else CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Box(
            Modifier.size(72.dp).background(back).clickable {
                val output = if (showShadow) createPreviewShadow(shape, density, direction, size = renderSize)
                    else Bitmap.createBitmap(renderSize, renderSize, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(output)
                val iconSize = if (showShadow) renderSize * 5f / 6f else renderSize.toFloat()
                val inset = (renderSize - iconSize) / 2f
                val outline = shape.createOutline(Size(iconSize, iconSize), direction, density)
                val path = when (outline) {
                    is Outline.Rectangle -> Path().apply { addRect(outline.rect) }
                    is Outline.Rounded -> Path().apply { addRoundRect(outline.roundRect) }
                    is Outline.Generic -> outline.path
                }
                canvas.translate(inset, inset)
                canvas.clipPath(path.asAndroidPath())
                val source = drawable.toBitmap(renderSize, renderSize)
                canvas.drawBitmap(source, null, android.graphics.RectF(0f, 0f, iconSize, iconSize), android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG or android.graphics.Paint.FILTER_BITMAP_FLAG))
                onPreviewClick(IconPreviewSelection(output, original, (drawable as? ColorDrawable)?.color?.takeIf { isBackground }))
            },
            contentAlignment = Alignment.Center,
        ) {
            if (shadow != null) Image(shadow, null, Modifier.size(72.dp))
            Image(bitmap, null, Modifier.size(60.dp).clip(shape))
        }
    }
}

// Launcher3 Oreo: blurred alpha, an ambient shadow, then a downward key shadow.
private fun createPreviewShadow(shape: Shape, density: Density, direction: LayoutDirection, size: Int = 216): Bitmap {
    val iconSize = size * 5f / 6f
    val inset = (size - iconSize) / 2f
    val mask = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(mask)
    val outline = shape.createOutline(Size(iconSize, iconSize), direction, density)
    val path = when (outline) {
        is Outline.Rectangle -> Path().apply { addRect(outline.rect) }
        is Outline.Rounded -> Path().apply { addRoundRect(outline.roundRect) }
        is Outline.Generic -> outline.path
    }
    canvas.translate(inset, inset)
    canvas.drawPath(path.asAndroidPath(), Paint(Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.BLACK })
    val offset = IntArray(2)
    val alpha = mask.extractAlpha(Paint(Paint.ANTI_ALIAS_FLAG).apply {
        maskFilter = BlurMaskFilter(iconSize * (0.5f / 48f), BlurMaskFilter.Blur.NORMAL)
    }, offset)
    val result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val output = Canvas(result)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply { color = android.graphics.Color.BLACK; this.alpha = 30 }
    output.drawBitmap(alpha, offset[0].toFloat(), offset[1].toFloat(), paint)
    paint.alpha = 61
    output.drawBitmap(alpha, offset[0].toFloat(), offset[1] + iconSize / 48f, paint)
    mask.recycle()
    alpha.recycle()
    return result
}
