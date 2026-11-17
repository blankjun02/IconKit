package com.blacksoft.iconkit.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.blacksoft.iconkit.core.InspectedApp
import com.blacksoft.iconkit.core.InstalledAppIconScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppsViewModel(application: Application) : AndroidViewModel(application) {
    var apps by mutableStateOf<List<InspectedApp>>(emptyList())
        private set
    var isRefreshing by mutableStateOf(false)
        private set
    var hasLoaded by mutableStateOf(false)
        private set
    var error by mutableStateOf<Throwable?>(null)
        private set
    var selectedApp by mutableStateOf<InspectedApp?>(null)
        private set
    var selectedPackages by mutableStateOf<Set<String>>(emptySet())
        private set

    fun loadIfNeeded() {
        loadAppList(force = false)
    }

    fun refresh() = loadAppList(force = true)

    private fun loadAppList(force: Boolean) {
        if (isRefreshing || (!force && hasLoaded)) return
        isRefreshing = true
        error = null
        viewModelScope.launch {
            try {
                val refreshedApps = withContext(Dispatchers.IO) {
                    InstalledAppIconScanner(getApplication()).scan()
                }
                apps = refreshedApps
                selectedPackages = selectedPackages.intersect(refreshedApps.map { it.packageName }.toSet())
                hasLoaded = true
            } catch (cancelled: kotlinx.coroutines.CancellationException) {
                throw cancelled
            } catch (failure: Exception) {
                error = failure
            } finally {
                isRefreshing = false
            }
        }
    }

    fun openDetails(app: InspectedApp) { selectedApp = app }
    fun closeDetails() { selectedApp = null }
    fun toggleSelection(app: InspectedApp) {
        selectedPackages = selectedPackages.toMutableSet().apply {
            if (!add(app.packageName)) remove(app.packageName)
        }
    }
    fun clearSelection() { selectedPackages = emptySet() }
}
