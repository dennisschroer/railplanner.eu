package eu.railplanner.railplanner.external.nl.ns.reisinformatie;

import eu.railplanner.external.nl.ns.reisinformatie.StationsApi;
import eu.railplanner.external.nl.ns.reisinformatie.model.StationResponse;
import eu.railplanner.railplanner.external.ImportRunnable;
import eu.railplanner.railplanner.model.Country;
import eu.railplanner.railplanner.model.Location;
import eu.railplanner.railplanner.model.Station;
import eu.railplanner.railplanner.repository.StationRespository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@CommonsLog
public class StationImport implements ImportRunnable {
    private final Config config;

    private final StationRespository stationRespository;

    public StationImport(Config config, StationRespository stationRespository) {
        this.config = config;
        this.stationRespository = stationRespository;
    }

    @Override
    public void run() {
        StationsApi api = new StationsApi();
        api.getApiClient().setApiKey(config.getApiKey());

        StationResponse response = api.getStations().block(Duration.ofSeconds(5));

        response.getPayload().forEach(this::insertOrUpdateStation);
    }

    private void insertOrUpdateStation(eu.railplanner.external.nl.ns.reisinformatie.model.Station externalStation) {
        Station station = stationRespository.findByUicCode(externalStation.getUiCCode()).orElse(new Station());

        if (station.getId() != null) {
            log.info(String.format("Updating existing station for UIC code %s (%s)", externalStation.getUiCCode(), station));
        }

        station.setUicCode(externalStation.getUiCCode());
        station.setCountry(findCountry(externalStation));
        if (externalStation.getLat() != null && externalStation.getLng() != null) {
            station.setLocation(new Location(externalStation.getLat(), externalStation.getLng()));
        }
        if (externalStation.getNamen() != null) {
            station.setName(externalStation.getNamen().getLang());
        }

        stationRespository.save(station);
    }

    private Country findCountry(eu.railplanner.external.nl.ns.reisinformatie.model.Station externalStation) {
        Country country = null;
        String externalCode = externalStation.getLand();

        if (externalCode != null) {
            externalCode = externalCode.equals("A") ? "AT" : externalCode;
            externalCode = externalCode.equals("B") ? "BE" : externalCode;
            externalCode = externalCode.equals("D") ? "DE" : externalCode;
            externalCode = externalCode.equals("F") ? "FR" : externalCode;
            country = Country.byCode(externalCode);
        }

        if (country == null) {
            log.warn(String.format("No country found for code '%s'", externalCode));
        }

        return country;
    }

    @Override
    public boolean isEnabled() {
        return config.isStationImportEnabled();
    }
}
