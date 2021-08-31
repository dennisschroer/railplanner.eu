package eu.railplanner.railplanner.service;

import eu.railplanner.railplanner.model.timetable.Connection;
import eu.railplanner.railplanner.model.timetable.Trip;
import eu.railplanner.railplanner.model.timetable.TripValidity;
import eu.railplanner.railplanner.repository.timetable.ConnectionRepository;
import eu.railplanner.railplanner.repository.timetable.TripRepository;
import eu.railplanner.railplanner.repository.timetable.TripValidityRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

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
}
