package eu.railplanner.runner.util;

import eu.railplanner.core.model.Country;

import java.time.ZoneId;

public class TimezoneUtils {
    public static ZoneId getTimezoneForCountry(Country country){
        return country.getTimezone();
    }
}
