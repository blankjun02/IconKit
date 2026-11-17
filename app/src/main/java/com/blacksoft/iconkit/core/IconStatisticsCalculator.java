package com.blacksoft.iconkit.core;

import java.util.List;

/** Pure calculation logic; switching the source does not rescan the device. */
public final class IconStatisticsCalculator {
    private IconStatisticsCalculator() {}

    public static IconStatistics calculate(List<InspectedApp> apps, IconSource source) {
        int adaptive = 0;
        int legacy = 0;
        int unavailable = 0;
        for (InspectedApp app : apps) {
            switch (app.getIcon(source).getAdaptation()) {
                case ADAPTIVE: adaptive++; break;
                case LEGACY: legacy++; break;
                case UNAVAILABLE: unavailable++; break;
            }
        }
        return new IconStatistics(source, apps.size(), adaptive + legacy,
                adaptive, legacy, unavailable);
    }
}
