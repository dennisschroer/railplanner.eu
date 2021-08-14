package eu.railplanner.railplanner.repository;

import eu.railplanner.railplanner.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRespository extends JpaRepository<Station, Long> {
}
