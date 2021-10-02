package eu.railplanner.core.repository.timetable;

import eu.railplanner.core.model.timetable.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {

    Optional<Trip> findByIdentifier(String identifier);
}