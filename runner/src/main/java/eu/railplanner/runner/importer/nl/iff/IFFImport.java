package eu.railplanner.runner.importer.nl.iff;

import eu.railplanner.core.model.Country;
import eu.railplanner.runner.importer.AbstractTimetableImporter;
import eu.railplanner.runner.importer.model.ImportConnection;
import eu.railplanner.runner.importer.model.ImportStation;
import eu.railplanner.runner.importer.model.ImportTrip;
import eu.railplanner.runner.importer.nl.iff.model.IFF;
import eu.railplanner.runner.importer.nl.iff.model.Timetable;
import eu.railplanner.runner.importer.nl.iff.parser.IFFParser;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CommonsLog
@Component
public class IFFImport extends AbstractTimetableImporter {

    private Map<Integer, List<LocalDate>> mappedFootnotes;

    private IFF iff;

    @Override
    public Country getCountry() {
        return Country.NETHERLANDS;
    }

    @Override
    public void loadData() throws IOException {
        IFFParser parser = new IFFParser();
        Path path = Path.of("./import/iff/NDOV_32_33_34_35");

        log.info("Parsing IFF data from " + path);
        iff = parser.load(path);
        log.info(String.format("IFF loaded. Period: %s - %s. Stations: %d, Services: %d",
                iff.getDelivery().getIdentificationRecord().getFirstDay(),
                iff.getDelivery().getIdentificationRecord().getLastDay(),
                iff.getStations().getStations().size(),
                iff.getTimetable().getServices().size()));

        mapFootnotes(iff);
    }

    @Override
    public Stream<ImportStation> getImportStations() {
        return iff.getStations().getStations().stream().map(
                iffStation -> ImportStation.builder()
                        .name(iffStation.getName())
                        .country(mapCountryCode(iffStation.getCountryCode()))
                        .localCode(iffStation.getShortName().toUpperCase())
                        .build());
    }

    @Override
    public Stream<ImportTrip> getImportTrips() {
        return iff.getTimetable().getServices().stream().map(transportService -> {
            // Determine dates
            List<LocalDate> dates = transportService.getValidities().stream()
                    .map(validity -> mappedFootnotes.get(validity.getFootnoteNumber()))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            List<ImportConnection> connections = new ArrayList<>();

            // Determine connections
            Timetable.Stop previousStop = null;
            for (Timetable.Stop stop : transportService.getStops()) {
                if (previousStop != null) {
                    connections.add(ImportConnection.builder()
                            .startLocalCode(previousStop.getStationName().toUpperCase())
                            .departure(previousStop.getDeparture())
                            .endLocalCode(stop.getStationName().toUpperCase())
                            .arrival(stop.getArrival())
                            .build()
                    );
                }
                previousStop = stop;
            }

            // Create trip
            return ImportTrip.builder()
                    .identifier(determineTripIdentifier(transportService))
                    .company(determineCompany(transportService))
                    .dates(dates)
                    .connections(connections)
                    .build();
        });
    }

    private Country mapCountryCode(String countryCode) {
        return Country.byCode(convertCountryCodeToIsoCode(countryCode));
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
