package eu.railplanner.runner.importer;

import eu.railplanner.core.model.Country;
import eu.railplanner.core.model.Station;
import eu.railplanner.core.model.timetable.Connection;
import eu.railplanner.core.model.timetable.Trip;
import eu.railplanner.core.model.timetable.TripValidity;
import eu.railplanner.core.service.StationService;
import eu.railplanner.core.service.TripService;
import eu.railplanner.runner.importer.model.ImportStation;
import eu.railplanner.runner.importer.model.ImportTrip;
import eu.railplanner.runner.job.RailplannerJob;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CommonsLog
public abstract class AbstractTimetableImporter implements RailplannerJob {

    @Autowired
    private StationService stationService;
    
    @Autowired
    private TripService tripService;

    private final Map<String, Station> stations = new HashMap<>();

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
        return stationService.findMatchingStation(
                importStation.getUicCode(),
                importStation.getName(),
                importStation.getLocalCode(),
                getCountry()
        ).orElse(createStation(importStation));
    }

    protected Station createStation(ImportStation importStation) {
        log.warn(String.format("Station does not exist in database, creating new station: %s", importStation));

        Station station = new Station();
        station.setName(importStation.getName());
        station.setCountry(importStation.getCountry());
        station = stationService.save(station);
        stationService.setLocalCode(station, getCountry(), importStation.getLocalCode());
        return station;
    }

    protected void importTrips(Stream<ImportTrip> importTrips) {
        List<Trip> trips = new ArrayList<>();
        List<TripValidity> tripValidities = new ArrayList<>();
        List<Connection> connections = new ArrayList<>();

        importTrips.forEach(importTrip -> {
            Trip trip = new Trip();
            trip.setCompany(importTrip.getCompany());
            trip.setIdentifier(importTrip.getIdentifier());

            List<TripValidity> relevantValidities = importTrip.getDates().stream()
                    .filter(date -> !date.isBefore(LocalDate.now()))
                    .map(date -> {
                        TripValidity tripValidity = new TripValidity();
                        tripValidity.setTrip(trip);
                        tripValidity.setDate(date);
                        return tripValidity;
                    })
                    .collect(Collectors.toList());

            if (!relevantValidities.isEmpty()) {
                trips.add(trip);
                tripValidities.addAll(relevantValidities);
                connections.addAll(importTrip.getConnections().stream().map(importConnection -> {
                    Connection connection = new Connection();
                    connection.setStart(getStationForLocalCode(importConnection.getStartLocalCode()));
                    connection.setDeparture(importConnection.getDeparture());
                    connection.setEnd(getStationForLocalCode(importConnection.getEndLocalCode()));
                    connection.setArrival(importConnection.getArrival());
                    connection.setTrip(trip);
                    return connection;
                }).collect(Collectors.toList()));
            }

            if (trips.size() % getBatchSize() == 0) {
                tripService.saveTrips(trips);
                tripService.saveConnections(connections);
                tripService.saveTripValidities(tripValidities);
            }
        });
    }

    private int getBatchSize() {
        return 100;
    }

    private Station getStationForLocalCode(String startLocalCode) {
        return stations.get(startLocalCode);
    }
}
