package eu.railplanner.core.service;

import eu.railplanner.core.model.Country;
import eu.railplanner.core.model.Station;
import eu.railplanner.core.model.StationLocalCode;
import eu.railplanner.core.repository.StationLocalCodeRespository;
import eu.railplanner.core.repository.StationRespository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Optional;

@Service
@CommonsLog
public class StationServiceImpl implements StationService {

    private final StationRespository stationRespository;

    private final StationLocalCodeRespository stationLocalCodeRespository;

    public StationServiceImpl(StationRespository stationRespository, StationLocalCodeRespository stationLocalCodeRespository) {
        this.stationRespository = stationRespository;
        this.stationLocalCodeRespository = stationLocalCodeRespository;
    }

    @Override
    public Optional<Station> findMatchingStation(@Nullable String uicCode, @Nullable String name, @Nullable String localCode, @Nullable Country localCodeCountry) {
        Optional<Station> station = Optional.empty();

        // Find by UIC code
        if (uicCode != null) {
            station = stationRespository.findByUicCode(uicCode);
        }

        // Find by local code
        if (localCode != null && localCodeCountry != null) {
            Optional<Station> stationByLocalCode = stationLocalCodeRespository
                    .findByCountryAndLocalCode(localCodeCountry, localCode)
                    .map(StationLocalCode::getStation);

            if (stationByLocalCode.isPresent()) {
                if (station.isPresent() && !station.get().getId().equals(stationByLocalCode.get().getId())) {
                    throw new IllegalStateException(String.format(
                            "Station referenced by local code %s from %s is not the same as station " +
                                    "referenced by UIC code %s. station=%s, stationByLocalCode=%s",
                            localCode, localCodeCountry, uicCode, station.get(), stationByLocalCode.get()
                    ));
                }

                station = stationByLocalCode;
            }
        }

        // Validate name
        if (name != null) {
            if (station.isPresent() && !station.get().getName().equals(name)) {
                log.warn(String.format("Name of station (%s) does not match (%s) for station %s",
                        station.get().getName(), name, station.get()));
            }
        }

        return station;
    }

    @Override
    public Station save(Station station) {
        return stationRespository.save(station);
    }

    @Override
    public StationLocalCode setLocalCode(Station station, Country country, String code) {
        StationLocalCode stationLocalCode = stationLocalCodeRespository
                .findByStationAndCountry(station, country)
                .orElse(new StationLocalCode());

        stationLocalCode.setStation(station);
        stationLocalCode.setCountry(country);
        stationLocalCode.setLocalCode(code);

        return stationLocalCodeRespository.save(stationLocalCode);
    }
}
