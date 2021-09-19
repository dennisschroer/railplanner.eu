package eu.railplanner.core.service;


import eu.railplanner.core.model.Country;
import eu.railplanner.core.model.Station;
import eu.railplanner.core.model.StationLocalCode;

import javax.annotation.Nullable;
import java.util.Optional;

public interface StationService {
    Optional<Station> findMatchingStation(@Nullable String uicCode, @Nullable String name, @Nullable String localCode, @Nullable Country localCodeCountry);

    Station save(Station station);

    StationLocalCode setLocalCode(Station station, Country country, String code);
}
