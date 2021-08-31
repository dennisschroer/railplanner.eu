package eu.railplanner.railplanner.repository.timetable;

import eu.railplanner.railplanner.model.timetable.TripValidity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripValidityRepository extends JpaRepository<TripValidity, Long> {
}