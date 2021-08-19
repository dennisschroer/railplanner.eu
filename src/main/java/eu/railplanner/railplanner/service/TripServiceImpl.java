package eu.railplanner.railplanner.service;

import eu.railplanner.railplanner.model.timetable.Connection;
import eu.railplanner.railplanner.model.timetable.Trip;
import eu.railplanner.railplanner.repository.timetable.ConnectionRepository;
import eu.railplanner.railplanner.repository.timetable.TripRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;

    private final ConnectionRepository connectionRepository;

    public TripServiceImpl(TripRepository tripRepository, ConnectionRepository connectionRepository) {
        this.tripRepository = tripRepository;
        this.connectionRepository = connectionRepository;
    }

    @Override
    public Trip save(@Nonnull Trip trip) {
        return tripRepository.save(trip);
    }

    @Override
    public Connection save(@Nonnull Connection connection) {
        return connectionRepository.save(connection);
    }
}
