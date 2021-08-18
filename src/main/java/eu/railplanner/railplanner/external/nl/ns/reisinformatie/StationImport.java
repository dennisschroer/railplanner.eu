package eu.railplanner.railplanner.external.nl.ns.reisinformatie;

import eu.railplanner.external.nl.ns.reisinformatie.StationsApi;
import eu.railplanner.external.nl.ns.reisinformatie.model.StationResponse;
import eu.railplanner.railplanner.external.ImportRunnable;
import eu.railplanner.railplanner.model.Country;
import eu.railplanner.railplanner.model.Location;
import eu.railplanner.railplanner.model.Station;
import eu.railplanner.railplanner.service.StationService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@CommonsLog
public class StationImport implements ImportRunnable {
    private final ReisinformatieConfig config;

    private final StationService stationService;

    public StationImport(ReisinformatieConfig config, StationService stationService) {
        this.config = config;
        this.stationService = stationService;
    }

    @Override
    public void run() {
        StationsApi api = new StationsApi();
        api.getApiClient().setApiKey(config.getApiKey());

        StationResponse response = api.getStations().block(Duration.ofSeconds(5));

        response.getPayload().forEach(this::insertOrUpdateStation);
    }

    private void insertOrUpdateStation(eu.railplanner.external.nl.ns.reisinformatie.model.Station externalStation) {
        Station station = stationService.findMatchingStation(
                externalStation.getUiCCode(), externalStation.getNamen().getLang(),
                externalStation.getCode(), Country.NETHERLANDS).orElse(new Station());

        if (station.getId() != null) {
            log.info(String.format("Updating existing station for UIC code %s (%s)", externalStation.getUiCCode(), station));
        }

        station.setUicCode(externalStation.getUiCCode());
        station.setCountry(Country.byCode(convertCountryCodeToIsoCode(externalStation.getLand())));
        if (externalStation.getLat() != null && externalStation.getLng() != null) {
            station.setLocation(new Location(externalStation.getLat(), externalStation.getLng()));
        }
        if (externalStation.getNamen() != null) {
            station.setName(externalStation.getNamen().getLang());
        }

        station = stationService.save(station);
        stationService.setLocalCode(station, Country.NETHERLANDS, externalStation.getCode());
    }

    private String convertCountryCodeToIsoCode(String countryCode) {
        if (countryCode != null) {
            countryCode = countryCode.equals("A") ? "AT" : countryCode;
            countryCode = countryCode.equals("B") ? "BE" : countryCode;
            countryCode = countryCode.equals("D") ? "DE" : countryCode;
            countryCode = countryCode.equals("F") ? "FR" : countryCode;
        }

        return countryCode;
    }

    @Override
    public boolean isEnabled() {
        return config.isStationImportEnabled();
    }
}
