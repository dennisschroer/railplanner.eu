package eu.railplanner.core.repository.timetable;

import eu.railplanner.core.model.timetable.TripValidity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripValidityRepository extends JpaRepository<TripValidity, Long> {
}