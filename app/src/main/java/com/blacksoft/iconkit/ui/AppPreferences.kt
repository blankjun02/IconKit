package com.blacksoft.iconkit.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class UiStyle { MIUIX, MATERIAL3 }
enum class ThemeMode { SYSTEM, LIGHT, DARK }
enum class IconDetectionSource { APK, SYSTEM_API }

class AppPreferences(context: Context) {
    private val preferences = context.getSharedPreferences("appearance", Context.MODE_PRIVATE)

    var uiStyle by mutableStateOf(UiStyle.valueOf(preferences.getString("ui_style", UiStyle.MIUIX.name)!!))
        private set
    var themeMode by mutableStateOf(ThemeMode.valueOf(preferences.getString("theme_mode", ThemeMode.SYSTEM.name)!!))
        private set
    var dynamicColor by mutableStateOf(preferences.getBoolean("dynamic_color", true))
        private set
    var miuixMonet by mutableStateOf(preferences.getBoolean("miuix_monet", false))
        private set
    var enableBlur by mutableStateOf(preferences.getBoolean("enable_blur", true))
        private set
    var floatingNavigationBar by mutableStateOf(preferences.getBoolean("floating_navigation_bar", false))
        private set
    var pageScale by mutableFloatStateOf(preferences.getFloat("page_scale", 1f))
        private set
    var selectedTab by mutableIntStateOf(0)
        private set
    var showThemePage by mutableStateOf(false)
        private set
    var showAboutPage by mutableStateOf(false)
        private set
    var showSystemApps by mutableStateOf(preferences.getBoolean("show_system_apps", false))
        private set
    var sortByPackage by mutableStateOf(false)
        private set
    var reverseAppSort by mutableStateOf(false)
        private set
    var showSortMenu by mutableStateOf(false)
        private set
    var showAppFilterMenu by mutableStateOf(false)
        private set
    var iconDetectionSource by mutableStateOf(
        runCatching {
            IconDetectionSource.valueOf(preferences.getString("icon_detection_source", IconDetectionSource.APK.name)!!)
        }.getOrDefault(IconDetectionSource.APK)
    )
        private set

    fun updateUiStyle(value: UiStyle) = update("ui_style", value.name) { uiStyle = value }
    fun updateThemeMode(value: ThemeMode) = update("theme_mode", value.name) { themeMode = value }
    fun updateDynamicColor(value: Boolean) = update("dynamic_color", value) { dynamicColor = value }
    fun updateMiuixMonet(value: Boolean) = update("miuix_monet", value) { miuixMonet = value }
    fun updateEnableBlur(value: Boolean) = update("enable_blur", value) { enableBlur = value }
    fun updateFloatingNavigationBar(value: Boolean) = update("floating_navigation_bar", value) { floatingNavigationBar = value }
    fun updatePageScale(value: Float) = update("page_scale", value.coerceIn(.85f, 1.20f)) { pageScale = value.coerceIn(.85f, 1.20f) }
    fun updateSelectedTab(value: Int) { selectedTab = value.coerceIn(0, 2) }
    fun openThemePage() { showThemePage = true }
    fun closeThemePage() { showThemePage = false }
    fun openAboutPage() { showAboutPage = true }
    fun closeAboutPage() { showAboutPage = false }
    fun updateShowSystemApps(value: Boolean) = update("show_system_apps", value) { showSystemApps = value }
    fun updateSortByPackage(value: Boolean) { sortByPackage = value }
    fun updateReverseAppSort(value: Boolean) { reverseAppSort = value }
    fun updateShowSortMenu(value: Boolean) { showSortMenu = value }
    fun updateShowAppFilterMenu(value: Boolean) { showAppFilterMenu = value }
    fun updateIconDetectionSource(value: IconDetectionSource) =
        update("icon_detection_source", value.name) { iconDetectionSource = value }

    private inline fun update(key: String, value: Any, apply: () -> Unit) {
        apply()
        preferences.edit().also { editor ->
            when (value) {
                is String -> editor.putString(key, value)
                is Boolean -> editor.putBoolean(key, value)
                is Float -> editor.putFloat(key, value)
                is Int -> editor.putInt(key, value)
            }
        }.apply()
    }
}
