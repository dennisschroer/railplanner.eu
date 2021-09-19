package eu.railplanner.runner.importer.model;

public enum TimeZoneMode {
    /**
     * The time in the import is the local time, including daylight saving.
     *
     * This means that when the time states 6:00 and the base time offset is +1:00,
     * in the winter this means 5:00 UTC and in summer 4:00 UTC.
     */
    LOCAL_TIME_WITH_DAYLIGHT_SAVING;
}
