package eu.railplanner.core.service;

import eu.railplanner.core.model.timetable.Connection;
import eu.railplanner.core.model.timetable.Trip;
import eu.railplanner.core.model.timetable.TripValidity;
import eu.railplanner.core.repository.timetable.ConnectionRepository;
import eu.railplanner.core.repository.timetable.TripRepository;
import eu.railplanner.core.repository.timetable.TripValidityRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;

@Service
public class TripServiceImpl implements TripService {
    private final ConnectionRepository connectionRepository;

    private final TripRepository tripRepository;

    private final TripValidityRepository tripValidityRepository;

    public TripServiceImpl(ConnectionRepository connectionRepository, TripRepository tripRepository, TripValidityRepository tripValidityRepository) {
        this.connectionRepository = connectionRepository;
        this.tripRepository = tripRepository;
        this.tripValidityRepository = tripValidityRepository;
    }

    @Override
    public Trip save(@Nonnull Trip trip) {
        return tripRepository.save(trip);
    }

    @Override
    public Connection save(@Nonnull Connection connection) {
        return connectionRepository.save(connection);
    }

    @Override
    public TripValidity save(@Nonnull TripValidity tripValidity) {
        return tripValidityRepository.save(tripValidity);
    }

    @Override
    public void saveTrips(@Nonnull List<Trip> trips) {
        tripRepository.saveAllAndFlush(trips);
    }

    @Override
    public void saveConnections(@Nonnull List<Connection> connections) {
        connectionRepository.saveAllAndFlush(connections);
    }

    @Override
    public void saveTripValidities(@Nonnull List<TripValidity> tripValidities) {
        tripValidityRepository.saveAllAndFlush(tripValidities);
    }
}
