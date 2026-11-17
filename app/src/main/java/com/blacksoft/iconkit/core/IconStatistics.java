package com.blacksoft.iconkit.core;

/** Summary for one icon source. Ratios use successfully inspected icons as denominator. */
public final class IconStatistics {
    private final IconSource source;
    private final int totalApps;
    private final int inspectedApps;
    private final int adaptiveApps;
    private final int legacyApps;
    private final int unavailableApps;

    IconStatistics(IconSource source, int totalApps, int inspectedApps,
                   int adaptiveApps, int legacyApps, int unavailableApps) {
        this.source = source;
        this.totalApps = totalApps;
        this.inspectedApps = inspectedApps;
        this.adaptiveApps = adaptiveApps;
        this.legacyApps = legacyApps;
        this.unavailableApps = unavailableApps;
    }

    public IconSource getSource() { return source; }
    public int getTotalApps() { return totalApps; }
    public int getInspectedApps() { return inspectedApps; }
    public int getAdaptiveApps() { return adaptiveApps; }
    public int getLegacyApps() { return legacyApps; }
    public int getUnavailableApps() { return unavailableApps; }
    public double getAdaptiveRatio() { return ratio(adaptiveApps, inspectedApps); }
    public double getLegacyRatio() { return ratio(legacyApps, inspectedApps); }

    private static double ratio(int value, int denominator) {
        return denominator == 0 ? 0.0 : (double) value / denominator;
    }
}
