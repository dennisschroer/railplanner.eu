package eu.railplanner.core.repository.timetable;

import eu.railplanner.core.model.timetable.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, Long>, ConnectionRepositoryCustom {
}