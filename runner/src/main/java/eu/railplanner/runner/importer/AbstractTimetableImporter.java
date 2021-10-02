package eu.railplanner.runner.importer;

import eu.railplanner.core.model.Country;
import eu.railplanner.core.model.Station;
import eu.railplanner.core.model.timetable.Connection;
import eu.railplanner.core.model.timetable.Trip;
import eu.railplanner.core.service.StationService;
import eu.railplanner.core.service.TripService;
import eu.railplanner.runner.importer.model.ImportStation;
import eu.railplanner.runner.importer.model.ImportTrip;
import eu.railplanner.runner.importer.model.TimeZoneMode;
import eu.railplanner.runner.job.RailplannerJob;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CommonsLog
public abstract class AbstractTimetableImporter implements RailplannerJob {

    @Autowired
    private StationService stationService;

    @Autowired
    private TripService tripService;

    private final Map<String, Station> stations = new HashMap<>();

    public abstract String getImportName();

    public abstract Country getCountry();

    public abstract void loadData() throws IOException;

    public abstract Stream<ImportStation> getImportStations();

    public abstract Stream<ImportTrip> getImportTrips();

    @Override
    public void run() {
        try {
            log.info("Loading data");
            loadData();

            log.info("Importing stations");
            importStations(getImportStations());

            log.info("Importing trips");
            importTrips(getImportTrips());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    protected void importStations(Stream<ImportStation> importStations) {
        importStations.forEach(importStation -> stations.put(importStation.getLocalCode(), matchStation(importStation)));
    }

    protected Station matchStation(ImportStation importStation) {
        Optional<Station> matchingStation = stationService.findMatchingStation(
                importStation.getUicCode(),
                importStation.getName(),
                importStation.getLocalCode(),
                getCountry()
        );

        return matchingStation.orElseGet(() -> createStation(importStation));
    }

    protected Station createStation(ImportStation importStation) {
        log.warn(String.format("Station does not exist in database, creating new station: %s", importStation));

        Station station = new Station();
        station.setName(importStation.getName());
        station.setCountry(importStation.getCountry());
        station.setTimezone(importStation.getTimezone());
        station = stationService.save(station);
        stationService.setLocalCode(station, getCountry(), importStation.getLocalCode());
        return station;
    }

    protected void importTrips(Stream<ImportTrip> importTrips) {
        List<Trip> trips = new ArrayList<>();
        List<Connection> connections = new ArrayList<>();

        importTrips.forEach(importTrip -> {
            Trip trip = reuseOrImportTrip(importTrip);
            trips.add(trip);

            // Create trips, but only for dates after today
            List<Connection> expandedConnections = importTrip.getDates().stream()
                    .filter(date -> !date.isBefore(LocalDate.now()))
                    .map(date -> importConnectionsForLocalDate(importTrip, trip, date))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            connections.addAll(expandedConnections);

            if (trips.size() >= getBatchSize()) {
                log.info(String.format("Flushing %d trips and %s connections", trips.size(), connections.size()));

                tripService.saveTrips(trips);
                tripService.saveConnections(connections);

                trips.clear();
                connections.clear();
            }
        });
    }

    private List<Connection> importConnectionsForLocalDate(ImportTrip importTrip, Trip trip, LocalDate date) {
        return importTrip.getConnections().stream()
                .map(importConnection -> {
                    Station start = getStationForLocalCode(importConnection.getStartLocalCode());
                    Station end = getStationForLocalCode(importConnection.getEndLocalCode());

                    // Hours can be larger than 24
                    int localDepartureDateDiff = importConnection.getDeparture() / 60 / 24;
                    int localArrivalDateDiff = importConnection.getArrival() / 60 / 24;
                    LocalTime localDepartureTime = LocalTime.of(importConnection.getDeparture() / 60 % 24, importConnection.getDeparture() % 60);
                    LocalTime localArrivalTime = LocalTime.of(importConnection.getArrival() / 60 % 24, importConnection.getArrival() % 60);

                    Connection connection = new Connection();
                    connection.setStart(start);
                    connection.setDeparture(ZonedDateTime.of(date.plusDays(localDepartureDateDiff), localDepartureTime, start.getTimezone()).toInstant());
                    connection.setEnd(end);
                    connection.setArrival(ZonedDateTime.of(date.plusDays(localArrivalDateDiff), localArrivalTime, end.getTimezone()).toInstant());
                    connection.setTrip(trip);
                    return connection;
                }).collect(Collectors.toList());
    }

    private Trip reuseOrImportTrip(ImportTrip importTrip) {
        Trip trip = createTrip(importTrip);
        return tripService.findByIdentifier(trip.getIdentifier()).orElse(trip);
    }

    private Trip createTrip(ImportTrip importTrip) {
        Trip trip = new Trip();
        trip.setCompany(importTrip.getCompany());
        trip.setServiceNumber(importTrip.getServiceNumber());
        trip.setIdentifier(calculateTripIdentifier(importTrip));
        return trip;
    }

    private String calculateTripIdentifier(ImportTrip trip) {
        return String.format("%s.%s.%s.%s", getImportName(), trip.getCompany(), trip.getServiceNumber(), trip.getDates().get(0).format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    private int getBatchSize() {
        return 100;
    }

    private Station getStationForLocalCode(String startLocalCode) {
        return stations.get(startLocalCode);
    }
}
