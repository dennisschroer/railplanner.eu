package eu.railplanner.railplanner.external.nl.ns.reisinformatie;

import eu.railplanner.external.nl.ns.reisinformatie.DeparturesApi;
import eu.railplanner.external.nl.ns.reisinformatie.model.Departure;
import eu.railplanner.railplanner.external.ImportRunnable;
import eu.railplanner.railplanner.model.Station;
import eu.railplanner.railplanner.model.timetable.Connection;
import eu.railplanner.railplanner.model.timetable.Trip;
import eu.railplanner.railplanner.repository.StationRespository;
import eu.railplanner.railplanner.repository.timetable.ConnectionRepository;
import eu.railplanner.railplanner.repository.timetable.TripRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.ZoneOffset;
import java.util.List;

@Component
@CommonsLog
public class TripImport implements ImportRunnable {
    private final ReisinformatieConfig config;

    private final ConnectionRepository connectionRepository;

    private final StationRespository stationRespository;

    private final TripRepository tripRepository;

    public TripImport(ReisinformatieConfig config, ConnectionRepository connectionRepository, StationRespository stationRespository, TripRepository tripRepository) {
        this.config = config;
        this.connectionRepository = connectionRepository;
        this.stationRespository = stationRespository;
        this.tripRepository = tripRepository;
    }

    @Override
    public void run() {
        DeparturesApi api = new DeparturesApi();
        api.getApiClient().setApiKey(config.getApiKey());

        Station station = stationRespository.findByUicCode("8400212").orElseThrow();

        api.getDepartures(null, null, station.getUicCode(), null, null)
                .doOnSuccess(response -> this.handleDepartures(station, response.getPayload().getDepartures()))
                .block(Duration.ofSeconds(10));
    }

    private void handleDepartures(Station station, List<Departure> departures) {
        departures.forEach(departure -> {
            String tripIdentifier = departure.getName();
            Station nextStation = stationRespository.findByUicCode(departure.getRouteStations().get(0).getUicCode()).orElseThrow();

            Trip trip = tripRepository.findByIdentifier(tripIdentifier).orElseGet(() -> tripRepository.save(new Trip(tripIdentifier)));

            Connection connection = new Connection();
            connection.setTrip(trip);
            connection.setStart(station);
            connection.setDeparture(departure.getPlannedDateTime().withOffsetSameInstant(ZoneOffset.ofTotalSeconds(60 * departure.getPlannedTimeZoneOffset())));
            connection.setEnd(nextStation);
            connection.setArrival(null);

            connectionRepository.save(connection);
        });

    }

    @Override
    public boolean isEnabled() {
        return config.isTripImportEnabled();
    }
}
