package eu.railplanner.railplanner.repository;

import eu.railplanner.railplanner.model.Country;
import eu.railplanner.railplanner.model.Station;
import eu.railplanner.railplanner.model.StationLocalCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationLocalCodeRespository extends JpaRepository<StationLocalCode, Long> {
    Optional<StationLocalCode> findByStationAndCountry(Station station, Country country);

    Optional<StationLocalCode> findByCountryAndLocalCode(Country country, String localCode);
}
