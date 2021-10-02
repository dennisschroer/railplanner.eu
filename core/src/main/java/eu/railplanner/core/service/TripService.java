package eu.railplanner.core.service;

import eu.railplanner.core.model.timetable.Connection;
import eu.railplanner.core.model.timetable.Trip;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface TripService {
    /**
     * Save the list of trips.
     *
     * @param trips The trips to save.
     */
    void saveTrips(@Nonnull List<Trip> trips);

    /**
     * Save the list of connections.
     *
     * @param connections The connections to save.
     */
    void saveConnections(@Nonnull List<Connection> connections);

    /**
     * Find an existing connection by the unique internal identifier.
     *
     * @param identifier The identifier to search for.
     * @return The connection if it exists.
     */
    Optional<Trip> findByIdentifier(String identifier);
}
