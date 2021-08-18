package eu.railplanner.railplanner.service;

import eu.railplanner.railplanner.model.Country;
import eu.railplanner.railplanner.model.Station;
import eu.railplanner.railplanner.model.StationLocalCode;

import javax.annotation.Nullable;
import java.util.Optional;

public interface StationService {
    Optional<Station> findMatchingStation(@Nullable String uicCode, @Nullable String name, @Nullable String localCode, @Nullable Country localCodeCountry);

    Station save(Station station);

    StationLocalCode setLocalCode(Station station, Country country, String code);
}
