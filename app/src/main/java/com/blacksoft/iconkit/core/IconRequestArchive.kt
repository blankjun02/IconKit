package com.blacksoft.iconkit.core

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.FileProvider
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

object IconRequestArchive {
    @Suppress("DEPRECATION")
    fun create(context: Context, apps: List<InspectedApp>): File {
        val outputDir = File(context.cacheDir, "icon_requests").apply { mkdirs() }
        val output = File(outputDir, "IconKit_${System.currentTimeMillis()}.zip")
        val packageManager = context.packageManager
        ZipOutputStream(output.outputStream().buffered()).use { zip ->
            writeText(zip, "appfilter.xml", buildAppFilter(packageManager, apps))
            apps.forEach { app -> addApkIconResources(zip, packageManager, app) }
        }
        return output
    }

    fun share(context: Context, archive: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", archive)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/zip"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "分享图标资源"))
    }

    private fun buildAppFilter(packageManager: PackageManager, apps: List<InspectedApp>): String = buildString {
        append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n")
        apps.forEach { app ->
            val component = packageManager.getLaunchIntentForPackage(app.packageName)?.component
            val componentName = component?.flattenToString() ?: "${app.packageName}/${app.packageName}"
            append("    <item component=\"ComponentInfo{")
            append(xmlEscape(componentName))
            append("}\" drawable=\"")
            append(iconEntryName(packageManager, app) ?: safeName(app.label.toString(), app.packageName))
            append("\" />\n")
        }
        append("</resources>\n")
    }

    @Suppress("DEPRECATION")
    private fun iconEntryName(packageManager: PackageManager, app: InspectedApp): String? = runCatching {
        val info = packageManager.getApplicationInfo(app.packageName, PackageManager.MATCH_DISABLED_COMPONENTS)
        packageManager.getResourcesForApplication(info).getResourceEntryName(info.icon)
    }.getOrNull()

    @Suppress("DEPRECATION")
    private fun addApkIconResources(zip: ZipOutputStream, packageManager: PackageManager, app: InspectedApp) {
        val info = runCatching {
            packageManager.getApplicationInfo(app.packageName, PackageManager.MATCH_DISABLED_COMPONENTS)
        }.getOrNull() ?: return
        val resources = runCatching { packageManager.getResourcesForApplication(info) }.getOrNull() ?: return
        val ids = linkedSetOf<Int>()
        if (info.icon != 0) ids += info.icon
        readRoundIconId(info).takeIf { it != 0 }?.let(ids::add)
        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        packageManager.queryIntentActivities(launcherIntent, PackageManager.MATCH_DISABLED_COMPONENTS)
            .asSequence().filter { it.activityInfo.packageName == app.packageName }
            .map { it.iconResource }.filter { it != 0 }.forEach(ids::add)

        val targets = ids.mapNotNull { id ->
            runCatching { resources.getResourceTypeName(id) to resources.getResourceEntryName(id) }.getOrNull()
        }.toSet()
        if (targets.isEmpty()) return

        val source = info.publicSourceDir ?: return
        runCatching {
            ZipFile(source).use { apk ->
                apk.entries().asSequence().forEach { entry ->
                    if (entry.isDirectory || !entry.name.startsWith("res/")) return@forEach
                    val directory = entry.name.substringAfter("res/").substringBefore('/')
                    val type = directory.substringBefore('-')
                    val fileName = entry.name.substringAfterLast('/').substringBeforeLast('.')
                    if ((type to fileName) !in targets) return@forEach
                    val targetName = "apps/${app.packageName}/${entry.name}"
                    zip.putNextEntry(ZipEntry(targetName))
                    apk.getInputStream(entry).use { it.copyTo(zip) }
                    zip.closeEntry()
                }
            }
        }
    }

    private fun readRoundIconId(info: android.content.pm.ApplicationInfo): Int = runCatching {
        info.javaClass.getDeclaredMethod("getRoundIconRes").apply { isAccessible = true }.invoke(info) as Int
    }.recoverCatching {
        info.javaClass.getDeclaredField("roundIconRes").apply { isAccessible = true }.getInt(info)
    }.getOrDefault(0)

    private fun safeName(label: String, fallback: String): String = label.lowercase()
        .replace(Regex("[^a-z0-9_]+"), "_").trim('_').ifBlank { fallback.replace('.', '_') }

    private fun xmlEscape(value: String) = value.replace("&", "&amp;").replace("\"", "&quot;")

    private fun writeText(zip: ZipOutputStream, name: String, text: String) {
        zip.putNextEntry(ZipEntry(name))
        zip.write(text.toByteArray())
        zip.closeEntry()
    }
}
