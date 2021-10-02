package eu.railplanner.core.service;


import eu.railplanner.core.model.Country;
import eu.railplanner.core.model.Station;
import eu.railplanner.core.model.StationLocalCode;

import javax.annotation.Nullable;
import java.util.Optional;

public interface StationService {
    /**
     * Find a station by matching the supplied attributes by existing stations.
     * <p>
     * Stations are matched in this order:
     * <ul>
     *     <li>UIC-code</li>
     *     <li>If UIC-code is absent, match on the local code from the country.</li>
     * </ul>
     * A warning is logged when the given name does not match the name of the existing station.
     */
    Optional<Station> findMatchingStation(@Nullable String uicCode, @Nullable String name, @Nullable String localCode, @Nullable Country localCodeCountry);

    /**
     * Save the station.
     *
     * @param station The station to save.
     * @return The saved station.
     */
    Station save(Station station);

    /**
     * Attach a local code from a countries train system to a station.
     *
     * @param station The station to attach the local code to
     * @param country The country in which the local code is used.
     * @param code    The local code to attach.
     * @return The local code attached to the station.
     */
    StationLocalCode setLocalCode(Station station, Country country, String code);
}
