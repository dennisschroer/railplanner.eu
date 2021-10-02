package eu.railplanner.core.service;

import eu.railplanner.core.model.timetable.Connection;
import eu.railplanner.core.model.timetable.Trip;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface TripService {
    Trip save(@Nonnull Trip trip);

    Connection save(@Nonnull Connection connection);

    void saveTrips(@Nonnull List<Trip> trips);

    void saveConnections(@Nonnull List<Connection> connections);

    boolean existsByIdentifier(String identifier);

    Optional<Trip> findByIdentifier(String identifier);
}
