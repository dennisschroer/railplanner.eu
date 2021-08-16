package eu.railplanner.railplanner.repository;

import eu.railplanner.railplanner.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StationRespository extends JpaRepository<Station, Long> {
    Optional<Station> findByUicCode(String uicCode);
}
