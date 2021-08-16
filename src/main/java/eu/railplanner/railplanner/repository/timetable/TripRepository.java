package eu.railplanner.railplanner.repository.timetable;

import eu.railplanner.railplanner.model.timetable.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {

    Optional<Trip> findByIdentifier(String identifier);
}