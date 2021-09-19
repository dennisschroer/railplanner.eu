package eu.railplanner.core.repository;

import eu.railplanner.core.model.Country;
import eu.railplanner.core.model.Station;
import eu.railplanner.core.model.StationLocalCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationLocalCodeRespository extends JpaRepository<StationLocalCode, Long> {
    Optional<StationLocalCode> findByStationAndCountry(Station station, Country country);

    Optional<StationLocalCode> findByCountryAndLocalCode(Country country, String localCode);
}
