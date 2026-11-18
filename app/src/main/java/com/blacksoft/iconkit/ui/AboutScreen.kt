package com.blacksoft.iconkit.ui

import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blacksoft.iconkit.R
import androidx.core.graphics.drawable.toBitmap
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon as MiuixIcon
import top.yukonga.miuix.kmp.basic.IconButton as MiuixIconButton
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold as MiuixScaffold
import top.yukonga.miuix.kmp.basic.Text as MiuixText
import top.yukonga.miuix.kmp.basic.TopAppBar as MiuixTopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.utils.overScrollVertical

private data class AboutLink(val title: Int, val url: String)

private val aboutLinks = listOf(
    AboutLink(R.string.about_manager, "https://github.com/tiann/KernelSU/"),
    AboutLink(R.string.about_miuix, "https://github.com/compose-miuix-ui/miuix"),
    AboutLink(R.string.about_boundo, "https://github.com/LIU64N/Boundo"),
    AboutLink(R.string.about_donut, "https://github.com/futuredapp/donut"),
)
private const val TELEGRAM_CHANNEL_URL = "https://t.me/blacksoft_update"
private const val PROJECT_URL = "https://github.com/blankjun02/IconKit"

@Composable
fun AboutScreen(style: UiStyle, enableBlur: Boolean, onBack: () -> Unit) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val version = remember(context) {
        runCatching {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_META_DATA).versionName
        }.getOrNull() ?: "1.0"
    }
    if (style == UiStyle.MIUIX) {
        AboutMiuix(version, enableBlur, onBack, uriHandler::openUri)
    } else {
        AboutMaterial(version, onBack, uriHandler::openUri)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutMaterial(version: String, onBack: () -> Unit, openLink: (String) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = { Text(stringResource(R.string.about)) },
                navigationIcon = { IconButton(onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize(), contentPadding = padding) {
            item { AboutHeader(version, miuix = false) }
            item {
                SegmentedListItem(
                    onClick = { openLink(PROJECT_URL) },
                    modifier = Modifier.padding(horizontal = 16.dp),
                    shapes = ListItemDefaults.segmentedShapes(0, 1),
                    content = { Text(stringResource(R.string.about_github)) },
                    supportingContent = { Text("blankjun02/IconKit") },
                )
            }
            item {
                SegmentedListItem(
                    onClick = { openLink(TELEGRAM_CHANNEL_URL) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    shapes = ListItemDefaults.segmentedShapes(0, 1),
                    content = { Text(stringResource(R.string.about_telegram)) },
                    supportingContent = { Text("@blacksoft_update") },
                )
            }
            item {
                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    aboutLinks.forEachIndexed { index, link ->
                        SegmentedListItem(
                            onClick = { openLink(link.url) },
                            shapes = ListItemDefaults.segmentedShapes(index, aboutLinks.size),
                            content = { Text(stringResource(link.title)) },
                        )
                    }
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun AboutMiuix(version: String, enableBlur: Boolean, onBack: () -> Unit, openLink: (String) -> Unit) {
    val scrollBehavior = MiuixScrollBehavior()
    val backdrop = rememberManagerBlurBackdrop(enableBlur)
    MiuixScaffold(
        topBar = {
            ManagerBlurredBar(backdrop) {
                MiuixTopAppBar(
                    title = stringResource(R.string.about),
                    color = if (backdrop != null) Color.Transparent else MiuixTheme.colorScheme.surface,
                    navigationIcon = { MiuixIconButton(onBack) { MiuixIcon(MiuixIcons.Back, null) } },
                    scrollBehavior = scrollBehavior,
                )
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .then(if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .overScrollVertical(),
            contentPadding = padding,
            overscrollEffect = null,
        ) {
            item { AboutHeader(version, miuix = true) }
            item {
                Card(Modifier.fillMaxWidth().padding(horizontal = 12.dp).padding(bottom = 12.dp), insideMargin = PaddingValues(vertical = 4.dp)) {
                    ArrowPreference(
                        title = stringResource(R.string.about_github),
                        summary = "blankjun02/IconKit",
                        onClick = { openLink(PROJECT_URL) },
                    )
                    ArrowPreference(
                        title = stringResource(R.string.about_telegram),
                        summary = "@blacksoft_update",
                        onClick = { openLink(TELEGRAM_CHANNEL_URL) },
                    )
                }
            }
            item {
                Card(Modifier.fillMaxWidth().padding(horizontal = 12.dp), insideMargin = PaddingValues(vertical = 4.dp)) {
                    aboutLinks.forEach { link ->
                        ArrowPreference(title = stringResource(link.title), onClick = { openLink(link.url) })
                    }
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun AboutHeader(version: String, miuix: Boolean) {
    val context = LocalContext.current
    val appIcon = remember(context) {
        context.packageManager.getApplicationIcon(context.applicationInfo).toBitmap(192, 192).asImageBitmap()
    }
    val textColor = if (miuix) MiuixTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface
    val secondary = if (miuix) MiuixTheme.colorScheme.onSurfaceVariantSummary else MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        Modifier.fillMaxWidth().padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.size(88.dp), contentAlignment = Alignment.Center) {
            Image(appIcon, null, Modifier.size(80.dp))
        }
        if (miuix) {
            MiuixText("IconKit", modifier = Modifier.padding(top = 12.dp), color = textColor, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            MiuixText(stringResource(R.string.about_version, version), color = secondary, fontSize = 14.sp)
        } else {
            Text("IconKit", modifier = Modifier.padding(top = 12.dp), color = textColor, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Medium)
            Text(stringResource(R.string.about_version, version), color = secondary)
        }
    }
}
