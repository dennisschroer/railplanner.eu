package eu.railplanner.railplanner.external.nl.ns.reisinformatie;

import eu.railplanner.external.nl.ns.reisinformatie.StationsApi;
import eu.railplanner.external.nl.ns.reisinformatie.model.StationResponse;
import eu.railplanner.railplanner.model.Station;
import eu.railplanner.railplanner.repository.StationRespository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@CommonsLog
public class Runner implements Runnable{
    private final Config config;

    private final StationRespository stationRespository;

    public Runner(Config config, StationRespository stationRespository) {
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
        Station station = new Station();
        station.setUicCode(externalStation.getUiCCode());
        station.setName(externalStation.getNamen().getLang());
        stationRespository.save(station);
    }
}
