package eu.railplanner.railplanner.repository.timetable;

import eu.railplanner.railplanner.model.timetable.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
}