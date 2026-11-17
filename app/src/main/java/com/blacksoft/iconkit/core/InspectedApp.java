package com.blacksoft.iconkit.core;

/** Metadata and both icon representations for an installed package. */
public final class InspectedApp {
    private final String packageName;
    private final CharSequence label;
    private final long versionCode;
    private final boolean systemApp;
    private final int iconResourceId;
    private final LoadedIcon apkIcon;
    private final LoadedIcon runtimeIcon;

    public InspectedApp(String packageName, CharSequence label, long versionCode,
                        boolean systemApp, int iconResourceId,
                        LoadedIcon apkIcon, LoadedIcon runtimeIcon) {
        this.packageName = packageName;
        this.label = label;
        this.versionCode = versionCode;
        this.systemApp = systemApp;
        this.iconResourceId = iconResourceId;
        this.apkIcon = apkIcon;
        this.runtimeIcon = runtimeIcon;
    }

    public String getPackageName() { return packageName; }
    public CharSequence getLabel() { return label; }
    public long getVersionCode() { return versionCode; }
    public boolean isSystemApp() { return systemApp; }
    public int getIconResourceId() { return iconResourceId; }
    public LoadedIcon getApkIcon() { return apkIcon; }
    public LoadedIcon getRuntimeIcon() { return runtimeIcon; }

    public LoadedIcon getIcon(IconSource source) {
        return source == IconSource.APK_RESOURCE ? apkIcon : runtimeIcon;
    }
}
