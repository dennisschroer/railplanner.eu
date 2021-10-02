package eu.railplanner.core.service;

import eu.railplanner.core.model.timetable.Connection;
import eu.railplanner.core.model.timetable.Trip;
import eu.railplanner.core.repository.timetable.ConnectionRepository;
import eu.railplanner.core.repository.timetable.TripRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;

@Service
public class TripServiceImpl implements TripService {
    private final ConnectionRepository connectionRepository;

    private final TripRepository tripRepository;


    public TripServiceImpl(ConnectionRepository connectionRepository, TripRepository tripRepository) {
        this.connectionRepository = connectionRepository;
        this.tripRepository = tripRepository;
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
    public void saveTrips(@Nonnull List<Trip> trips) {
        tripRepository.saveAllAndFlush(trips);
    }

    @Override
    public void saveConnections(@Nonnull List<Connection> connections) {
        connectionRepository.saveAllAndFlush(connections);
    }

    @Override
    public boolean existsByIdentifier(String identifier) {
        return tripRepository.existsByIdentifier(identifier);
    }
}
