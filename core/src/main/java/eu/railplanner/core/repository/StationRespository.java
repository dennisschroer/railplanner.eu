package eu.railplanner.core.repository;

import eu.railplanner.core.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationRespository extends JpaRepository<Station, Long> {
    Optional<Station> findByUicCode(String uicCode);
}
