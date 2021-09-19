package eu.railplanner.core.service;

import eu.railplanner.core.model.timetable.Connection;
import eu.railplanner.core.model.timetable.Trip;
import eu.railplanner.core.model.timetable.TripValidity;

import javax.annotation.Nonnull;
import java.util.List;

public interface TripService {
    Trip save(@Nonnull Trip trip);

    Connection save(@Nonnull Connection connection);

    TripValidity save(@Nonnull TripValidity tripValidity);

    void saveTrips(@Nonnull List<Trip> trips);

    void saveConnections(@Nonnull List<Connection> connections);

    void saveTripValidities(@Nonnull List<TripValidity> tripValidities);
}
