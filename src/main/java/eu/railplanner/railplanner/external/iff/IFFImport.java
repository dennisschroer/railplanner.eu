package eu.railplanner.railplanner.external.iff;

import eu.railplanner.railplanner.external.ImportRunnable;
import eu.railplanner.railplanner.external.iff.model.IFF;
import eu.railplanner.railplanner.external.iff.model.Stations;
import eu.railplanner.railplanner.external.iff.model.Timetable;
import eu.railplanner.railplanner.external.iff.parser.IFFParser;
import eu.railplanner.railplanner.model.Country;
import eu.railplanner.railplanner.model.Station;
import eu.railplanner.railplanner.model.timetable.Connection;
import eu.railplanner.railplanner.model.timetable.Trip;
import eu.railplanner.railplanner.model.timetable.TripValidity;
import eu.railplanner.railplanner.service.StationService;
import eu.railplanner.railplanner.service.TripService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CommonsLog
@Component
public class IFFImport implements ImportRunnable {

    private final IFFConfig config;

    private final TripService tripService;

    private final StationService stationService;

    private Map<String, Station> mappedStations;

    private Map<Integer, List<LocalDate>> mappedFootnotes;

    public IFFImport(IFFConfig config, TripService tripService, StationService stationService) {
        this.config = config;
        this.tripService = tripService;
        this.stationService = stationService;
    }

    @Override
    public boolean isEnabled() {
        return config.isEnabled();
    }

    @Override
    public void run() {
        IFFParser parser = new IFFParser();
        Path path = Path.of("./import/iff/NDOV_32_33_34_35");

        try {
            // Parse and load all required data
            log.info("Parsing IFF data from " + path);
            IFF iff = parser.load(path);
            log.info(String.format("IFF loaded. Period: %s - %s. Stations: %d, Services: %d",
                    iff.getDelivery().getIdentificationRecord().getFirstDay(),
                    iff.getDelivery().getIdentificationRecord().getLastDay(),
                    iff.getStations().getStations().size(),
                    iff.getTimetable().getServices().size()));

            // Match stations with stations in database
            log.info("Matching IFF stations with stations in database");
            mapStations(iff);
            log.info("Mapping IFF footnotes to dates");
            mapFootnotes(iff);
            log.info("Importing train trips from IFF.");
            importTimetable(iff);

            log.info(String.format("Loaded %s", iff.getDelivery()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mapStations(IFF iff) {
        mappedStations = new HashMap<>();

        iff.getStations().getStations().forEach(iffStation -> {
            String stationCode = iffStation.getShortName();
            Optional<Station> station = stationService.findMatchingStation(
                    null, iffStation.getName(), stationCode.toUpperCase(), Country.NETHERLANDS);

            if (station.isEmpty()) {
                log.warn(String.format("Station does not exist in database, creating new station: %s", iffStation));
                mappedStations.put(stationCode, createStation(iffStation));
            } else {
                mappedStations.put(stationCode, station.get());
            }
        });
    }

    private Station createStation(Stations.Station iffStation) {
        Country country = Country.byCode(convertCountryCodeToIsoCode(iffStation.getCountryCode()));
        if (country == null) {
            throw new IllegalStateException(String.format("No country found for code %s", iffStation.getCountryCode()));
        }

        Station station = new Station();
        station.setName(iffStation.getName());
        station.setCountry(country);
        station = stationService.save(station);
        stationService.setLocalCode(station, Country.NETHERLANDS, iffStation.getShortName().toUpperCase());
        return station;
    }

    private String convertCountryCodeToIsoCode(String countryCode) {
        if (countryCode != null) {
            countryCode = countryCode.equals("A") ? "AT" : countryCode; // Oostenrijk
            countryCode = countryCode.equals("B") ? "BE" : countryCode; // België
            countryCode = countryCode.equals("CRO") ? "HR" : countryCode; // Croatië
            countryCode = countryCode.equals("CS") ? "CZ" : countryCode; // Tsjechië
            countryCode = countryCode.equals("D") ? "DE" : countryCode; // Duitsland
            countryCode = countryCode.equals("F") ? "FR" : countryCode; // Frankrijk
            countryCode = countryCode.equals("H") ? "HU" : countryCode; // Hongarije
            countryCode = countryCode.equals("I") ? "IT" : countryCode; // Italië
            countryCode = countryCode.equals("L") ? "LU" : countryCode; // Luxemburg
            countryCode = countryCode.equals("N") ? "NO" : countryCode; // Noorwegen
            countryCode = countryCode.equals("P") ? "PT" : countryCode; // Portugal
            countryCode = countryCode.equals("R") ? "RO" : countryCode; // Roemenië
            countryCode = countryCode.equals("S") ? "SE" : countryCode; // Zweden
            countryCode = countryCode.equals("SLO") ? "SI" : countryCode; // Slovenië
            countryCode = countryCode.equals("SWK") ? "SK" : countryCode; // Slowakije
        }

        return countryCode;
    }

    private void mapFootnotes(IFF iff) {
        mappedFootnotes = new HashMap<>();

        LocalDate firstDay = iff.getFootnote().getIdentificationRecord().getFirstDay();
        LocalDate lastDay = iff.getFootnote().getIdentificationRecord().getLastDay();
        List<LocalDate> validityDates = firstDay.datesUntil(lastDay.plusDays(1)).collect(Collectors.toList());

        iff.getFootnote().getNotes().forEach(footnote -> {
            int id = footnote.getNumber();
            String vector = footnote.getVector();

            mappedFootnotes.put(id, new ArrayList<>());
            for (int i = 0; i < vector.length(); i++) {
                if (vector.charAt(i) == '1') {
                    mappedFootnotes.get(id).add(validityDates.get(i));
                }
            }
        });
    }

    private void importTimetable(IFF iff) {
        int totalServices = iff.getTimetable().getServices().size();
        int currentServiceIndex = 1;

        for (Timetable.TransportService transportService : iff.getTimetable().getServices()) {
            // Create trip
            Trip trip = new Trip();
            trip.setCompany(determineCompany(transportService));
            trip.setIdentifier(determineTripIdentifier(transportService));
            trip = tripService.save(trip);

            // Add connections to trip
            Timetable.Stop previousStop = null;
            for (Timetable.Stop stop : transportService.getStops()) {
                if (previousStop != null) {
                    // TODO check timezone offset
                    Connection connection = new Connection();
                    connection.setStart(mappedStations.get(previousStop.getStationName()));
                    connection.setDeparture(previousStop.getDeparture());
                    connection.setEnd(mappedStations.get(stop.getStationName()));
                    connection.setArrival(stop.getArrival());
                    connection.setTrip(trip);

                    tripService.save(connection);
                }

                previousStop = stop;
            }

            // Create validities
            TripValidity tripValidity;
            for (Timetable.Validity validity : transportService.getValidities()) {
                for (LocalDate date : mappedFootnotes.get(validity.getFootnoteNumber())) {
                    tripValidity = new TripValidity();
                    tripValidity.setTrip(trip);
                    tripValidity.setDate(date);
                    tripService.save(tripValidity);
                }
            }

            if (currentServiceIndex % 100 == 0) {
                log.info(String.format("Imported trip %d/%d", currentServiceIndex, totalServices));
            }
            currentServiceIndex++;
        }
    }

    private String determineCompany(Timetable.TransportService transportService) {
        // TODO link with companies file
        List<String> companyNumbers = transportService.getServiceNumbers().stream()
                .map(Timetable.ServiceNumber::getCompanyNumber)
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.toList());

        return String.join(",", companyNumbers);
    }

    private String determineTripIdentifier(Timetable.TransportService transportService) {
        List<String> serviceNumbers = transportService.getServiceNumbers().stream()
                .map(Timetable.ServiceNumber::getServiceNumber)
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.toList());

        return String.join(",", serviceNumbers);
    }
}
