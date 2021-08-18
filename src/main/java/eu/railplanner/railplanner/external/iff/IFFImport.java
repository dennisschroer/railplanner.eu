package eu.railplanner.railplanner.external.iff;

import eu.railplanner.railplanner.external.ImportRunnable;
import eu.railplanner.railplanner.external.iff.model.IFF;
import eu.railplanner.railplanner.external.iff.model.Stations;
import eu.railplanner.railplanner.external.iff.parser.IFFParser;
import eu.railplanner.railplanner.model.Country;
import eu.railplanner.railplanner.model.Station;
import eu.railplanner.railplanner.service.StationService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CommonsLog
@Component
public class IFFImport implements ImportRunnable {

    private final IFFConfig config;

    private final StationService stationService;

    private Map<Stations.Station, Station> mappedStations;

    public IFFImport(IFFConfig config, StationService stationService) {
        this.config = config;
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

            // Match stations with stations in database
            log.info("Matching IFF stations with stations in database");
            mapStations(iff);

            log.info(String.format("Loaded %s", iff.getDelivery()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mapStations(IFF iff) {
        mappedStations = new HashMap<>();

        iff.getStations().getStations().forEach(iffStation -> {
            Optional<Station> station = stationService.findMatchingStation(
                    null, iffStation.getName(), iffStation.getShortName().toUpperCase(), Country.NETHERLANDS);

            if (station.isEmpty()) {
                log.warn(String.format("Station does not exist in database, creating new station: %s", iffStation));

                mappedStations.put(iffStation, createStation(iffStation));
            } else {
                mappedStations.put(iffStation, station.get());
            }
        });
    }

    private Station createStation(Stations.Station iffStation) {
        Station station = new Station();

        station.setName(iffStation.getName());
        station.setCountry(Country.byCode(convertCountryCodeToIsoCode(iffStation.getCountryCode())));

        if (station.getCountry() == null) {
            throw new IllegalStateException(String.format("No country found for code %s", iffStation.getCountryCode()));
        }

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
}
