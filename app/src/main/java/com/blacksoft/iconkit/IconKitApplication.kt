package com.blacksoft.iconkit

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.blacksoft.iconkit.ui.AppsViewModel

/** Keeps the app list alive for the lifetime of this process, like KernelSU manager. */
class IconKitApplication : Application(), ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()

    override fun onCreate() {
        super.onCreate()
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this))[AppsViewModel::class.java]
            .loadIfNeeded()
    }
}
