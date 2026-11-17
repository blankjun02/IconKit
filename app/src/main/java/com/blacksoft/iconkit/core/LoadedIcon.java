package com.blacksoft.iconkit.core;

import android.graphics.drawable.Drawable;

/** Result of loading one icon. A failure does not abort the device scan. */
public final class LoadedIcon {
    private final Drawable drawable;
    private final IconAdaptation adaptation;
    private final String error;

    private LoadedIcon(Drawable drawable, IconAdaptation adaptation, String error) {
        this.drawable = drawable;
        this.adaptation = adaptation;
        this.error = error;
    }

    public static LoadedIcon success(Drawable drawable, IconAdaptation adaptation) {
        return new LoadedIcon(drawable, adaptation, null);
    }

    public static LoadedIcon unavailable(Throwable throwable) {
        String message = throwable.getMessage();
        return new LoadedIcon(null, IconAdaptation.UNAVAILABLE,
                throwable.getClass().getSimpleName() + (message == null ? "" : ": " + message));
    }

    public Drawable getDrawable() { return drawable; }
    public IconAdaptation getAdaptation() { return adaptation; }
    public String getError() { return error; }
    public boolean isAvailable() { return drawable != null; }
}
