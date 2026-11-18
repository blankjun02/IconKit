package com.blacksoft.iconkit.ui

import android.content.Intent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.blacksoft.iconkit.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.yukonga.miuix.kmp.basic.Text as MiuixText
import top.yukonga.miuix.kmp.basic.TextButton as MiuixTextButton
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet
import java.io.File
import java.util.UUID
import java.util.zip.ZipFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun IconPreviewActions(selection: IconPreviewSelection?, appName: String, style: UiStyle, onDismiss: () -> Unit) {
    val bitmap = selection?.bitmap
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var pendingSave by remember { mutableStateOf<Bitmap?>(null) }
    var pendingOriginal by remember { mutableStateOf<OriginalIconResource?>(null) }
    val originalLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("*/*")) { uri: Uri? ->
        val original = pendingOriginal
        pendingOriginal = null
        if (uri != null && original != null) {
            scope.launch {
                val ok = withContext(Dispatchers.IO) {
                    runCatching {
                        ZipFile(original.apkPath).use { zip ->
                            val entry = zip.getEntry(original.entryPath) ?: error("Resource not found")
                            val output = context.contentResolver.openOutputStream(uri) ?: error("Cannot open destination")
                            output.use { target -> zip.getInputStream(entry).use { it.copyTo(target) } }
                        }
                        true
                    }.getOrDefault(false)
                }
                Toast.makeText(context, if (ok) R.string.icon_saved else R.string.icon_action_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }
    val saveLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("image/png")) { uri: Uri? ->
        val image = pendingSave
        pendingSave = null
        if (uri != null && image != null) {
            scope.launch {
                val ok = withContext(Dispatchers.IO) {
                    runCatching {
                        context.contentResolver.openOutputStream(uri)?.use { image.compress(Bitmap.CompressFormat.PNG, 100, it) } == true
                    }.getOrDefault(false)
                }
                Toast.makeText(context, if (ok) R.string.icon_saved else R.string.icon_action_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }
    val filename = remember(appName) { appName.replace(Regex("[\\\\/:*?\"<>|\\p{Cntrl}]"), "_").take(80).ifBlank { "IconKit" } + ".png" }
    fun copyColor(value: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("Color", value))
        Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show()
    }
    fun openOrShare(image: Bitmap, share: Boolean) {
        onDismiss()
        scope.launch {
            val uri = withContext(Dispatchers.IO) {
                runCatching {
                    val directory = File(context.cacheDir, "icon_previews").apply { mkdirs() }
                    val file = File(directory, "${UUID.randomUUID()}_$filename")
                    file.outputStream().use { check(image.compress(Bitmap.CompressFormat.PNG, 100, it)) }
                    FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", file)
                }.getOrNull()
            }
            if (uri == null) {
                Toast.makeText(context, R.string.icon_action_failed, Toast.LENGTH_SHORT).show()
                return@launch
            }
            runCatching {
                val intent = if (share) Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    clipData = android.content.ClipData.newRawUri("image", uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "image/png")
                    clipData = android.content.ClipData.newRawUri("image", uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, context.getString(if (share) R.string.icon_share else R.string.icon_open)))
            }.onFailure { Toast.makeText(context, R.string.icon_action_failed, Toast.LENGTH_SHORT).show() }
        }
    }
    if (style == UiStyle.MIUIX) {
        var retainedSelection by remember { mutableStateOf<IconPreviewSelection?>(null) }
        SideEffect { if (selection != null) retainedSelection = selection }
        val sheetSelection = selection ?: retainedSelection
        val sheetBitmap = sheetSelection?.bitmap
        val navigationBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        OverlayBottomSheet(
            show = bitmap != null,
            title = stringResource(R.string.icon_actions),
            onDismissRequest = onDismiss,
            onDismissFinished = { retainedSelection = null },
            renderInRootScaffold = false,
        ) {
            if (sheetBitmap != null) {
                val save = { pendingSave = sheetBitmap; onDismiss(); saveLauncher.launch(filename) }
                Column(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp, bottom = 8.dp + navigationBottom),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(sheetBitmap.asImageBitmap(), null, Modifier.size(72.dp).padding(bottom = 8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        MiuixTextButton(text = stringResource(R.string.icon_open), onClick = { openOrShare(sheetBitmap, false) }, modifier = Modifier.weight(1f))
                        MiuixTextButton(text = stringResource(R.string.icon_share), onClick = { openOrShare(sheetBitmap, true) }, modifier = Modifier.weight(1f))
                        MiuixTextButton(text = stringResource(R.string.icon_save), onClick = save, modifier = Modifier.weight(1f))
                    }
                    sheetSelection.original?.let { original ->
                        MiuixText(
                            stringResource(R.string.icon_original_format, original.filename.substringAfterLast('.', "").uppercase(), original.entryPath),
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp), textAlign = TextAlign.Center,
                        )
                        MiuixTextButton(
                            text = stringResource(R.string.icon_save_original),
                            onClick = { pendingOriginal = original; onDismiss(); originalLauncher.launch(original.filename) },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        )
                    }
                    sheetSelection.backgroundColor?.let { color ->
                        val value = String.format(java.util.Locale.ROOT, "#%08X", color)
                        MiuixText(stringResource(R.string.icon_background_color, value), modifier = Modifier.fillMaxWidth().padding(top = 12.dp).clickable { copyColor(value) }, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    } else if (bitmap != null) {
        val save = { pendingSave = bitmap; onDismiss(); saveLauncher.launch(filename) }
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(Modifier.fillMaxWidth().padding(bottom = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(bitmap.asImageBitmap(), null, Modifier.size(64.dp))
                ListItem(headlineContent = { Text(stringResource(R.string.icon_open)) }, modifier = Modifier.clickableAction { openOrShare(bitmap, false) })
                ListItem(headlineContent = { Text(stringResource(R.string.icon_share)) }, modifier = Modifier.clickableAction { openOrShare(bitmap, true) })
                ListItem(headlineContent = { Text(stringResource(R.string.icon_save)) }, modifier = Modifier.clickableAction(save))
                selection.original?.let { original ->
                    Text(stringResource(R.string.icon_original_format, original.filename.substringAfterLast('.', "").uppercase(), original.entryPath), modifier = Modifier.padding(horizontal = 24.dp))
                    ListItem(headlineContent = { Text(stringResource(R.string.icon_save_original)) }, modifier = Modifier.clickableAction {
                        pendingOriginal = original; onDismiss(); originalLauncher.launch(original.filename)
                    })
                }
                selection.backgroundColor?.let { color ->
                    val value = String.format(java.util.Locale.ROOT, "#%08X", color)
                    Text(stringResource(R.string.icon_background_color, value), modifier = Modifier.clickable { copyColor(value) }.padding(horizontal = 24.dp, vertical = 8.dp))
                }
            }
        }
    }
}

private fun Modifier.clickableAction(onClick: () -> Unit): Modifier = this.then(Modifier.clickable(onClick = onClick))
