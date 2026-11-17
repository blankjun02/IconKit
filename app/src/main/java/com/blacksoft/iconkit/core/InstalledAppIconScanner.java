package com.blacksoft.iconkit.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.WorkerThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Scans all installed packages and loads both raw APK and runtime icons. */
public final class InstalledAppIconScanner {
    private final PackageManager packageManager;

    public InstalledAppIconScanner(Context context) {
        packageManager = context.getApplicationContext().getPackageManager();
    }

    /** Performs disk I/O and must be called from a background thread. */
    @WorkerThread
    public List<InspectedApp> scan() {
        List<PackageInfo> packages = getInstalledPackages();
        List<InspectedApp> result = new ArrayList<>(packages.size());
        for (PackageInfo packageInfo : packages) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            if (appInfo == null) continue;

            CharSequence label;
            try {
                label = packageManager.getApplicationLabel(appInfo);
            } catch (RuntimeException ignored) {
                label = packageInfo.packageName;
            }
            result.add(new InspectedApp(
                    packageInfo.packageName,
                    label,
                    getLongVersionCode(packageInfo),
                    (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0,
                    appInfo.icon,
                    loadApkResourceIcon(appInfo),
                    loadRuntimeIcon(appInfo)));
        }
        result.sort(Comparator.comparing(
                app -> app.getLabel().toString(), String.CASE_INSENSITIVE_ORDER));
        return Collections.unmodifiableList(result);
    }

    @SuppressWarnings("deprecation")
    private List<PackageInfo> getInstalledPackages() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(
                    PackageManager.MATCH_DISABLED_COMPONENTS));
        }
        return packageManager.getInstalledPackages(PackageManager.MATCH_DISABLED_COMPONENTS);
    }

    /** Bypasses ApplicationInfo.loadIcon() and resolves android:icon from APK Resources. */
    @SuppressWarnings("deprecation")
    private LoadedIcon loadApkResourceIcon(ApplicationInfo appInfo) {
        try {
            if (appInfo.icon == 0) {
                throw new Resources.NotFoundException("android:icon is not declared");
            }
            Resources resources = packageManager.getResourcesForApplication(appInfo);
            Drawable drawable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    ? resources.getDrawable(appInfo.icon, null)
                    : resources.getDrawable(appInfo.icon);
            return LoadedIcon.success(drawable, classify(drawable));
        } catch (Exception error) {
            return LoadedIcon.unavailable(error);
        }
    }

    /** Loads the final icon exposed by PackageManager, including device/OEM processing. */
    private LoadedIcon loadRuntimeIcon(ApplicationInfo appInfo) {
        try {
            Drawable drawable = packageManager.getApplicationIcon(appInfo);
            return LoadedIcon.success(drawable, classify(drawable));
        } catch (Exception error) {
            return LoadedIcon.unavailable(error);
        }
    }

    private IconAdaptation classify(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && drawable instanceof AdaptiveIconDrawable) {
            return IconAdaptation.ADAPTIVE;
        }
        return IconAdaptation.LEGACY;
    }

    @SuppressWarnings("deprecation")
    private static long getLongVersionCode(PackageInfo info) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
                ? info.getLongVersionCode() : info.versionCode;
    }
}
