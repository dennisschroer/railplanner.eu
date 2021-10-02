package eu.railplanner.core.repository.timetable;

import eu.railplanner.core.model.timetable.Connection;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public interface ConnectionRepositoryCustom {
    /**
     * Find all valid connections on a certain date within a time window starting at a specific time.
     *
     * @param startTime The start time (in UTC) of the time window.
     * @param window    The size (in minutes) of the window.
     * @return A list of all valid connections departing in the time window.
     */
    List<Connection> findAllValidConnections(Instant startTime, short window);
}
