package eu.railplanner.railplanner.repository.timetable;

import eu.railplanner.railplanner.model.timetable.Connection;

import java.time.LocalDate;
import java.util.List;

public interface ConnectionRepositoryCustom {
    /**
     * Find all valid connections on a certain date within a time window starting at a specific time.
     *
     * @param date      The date for which to find valid connecitons
     * @param startTime The start time (in minutes since start of the day) of the time window.
     * @param window    The size (in minutes) of the window.
     * @return A list of all valid connections departing in the time window.
     */
    List<Connection> findAllValidConnections(LocalDate date, short startTime, short window);
}
