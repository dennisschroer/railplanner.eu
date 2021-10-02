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
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@CommonsLog
public abstract class AbstractTimetableImporter implements RailplannerJob {

    public static ZoneOffset CENTRAL_EUROPEAN = ZoneOffset.ofHours(1);

    @Autowired
    private StationService stationService;

    @Autowired
    private TripService tripService;

    private final Map<String, Station> stations = new HashMap<>();

    public abstract String getImportName();

    public abstract Country getCountry();

    /**
     * Base offset of the arrival times and departure times in this import.
     */
    public abstract ZoneOffset getBaseOffset();

    public abstract TimeZoneMode getTimeZoneMode();

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
        short baseOffsetMinutes = (short) (getBaseOffset().getTotalSeconds() / 60);

        List<Trip> trips = new ArrayList<>();
        //List<TripValidity> tripValidities = new ArrayList<>();
        List<Connection> connections = new ArrayList<>();

        importTrips.forEach(importTrip -> {
            // Create trip
            Trip trip = createTrip(importTrip);

            // Create trips, but only for dates after today
//            List<Trip> expandedTrips = importTrip.getDates().stream()
//                    .filter(date -> !date.isBefore(LocalDate.now()))
//                    .map(date -> {
//
//
//
////                        connections.add
////
////
////
////                        ZonedDateTime
////
////                        List<Connection> tripConnections = importTrip.getConnections().stream().map(importConnection -> {
////                            Connection connection = new Connection();
////                            connection.setStart(getStationForLocalCode(importConnection.getStartLocalCode()));
////                            connection.setDeparture((short) (importConnection.getDeparture() - baseOffsetMinutes));
////                            connection.setEnd(getStationForLocalCode(importConnection.getEndLocalCode()));
////                            connection.setArrival((short) (importConnection.getArrival() - baseOffsetMinutes));
////                            connection.setTrip(trip);
////                            return connection;
////                        }).collect(Collectors.toList());
////                        connections.addAll(tripConnections);
//
//
//
//                        return trip;
//                    })
//                    .collect(Collectors.toList());

            if(!tripService.existsByIdentifier(trip.getIdentifier())){
                trips.add(trip);
            }


            if (trips.size() > getBatchSize()) {
                tripService.saveTrips(trips);
                tripService.saveConnections(connections);

                trips.clear();
                connections.clear();
            }
        });
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
