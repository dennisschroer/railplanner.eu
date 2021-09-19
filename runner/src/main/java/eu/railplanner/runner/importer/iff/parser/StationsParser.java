package eu.railplanner.runner.importer.iff.parser;

import eu.railplanner.runner.importer.iff.model.Stations;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.util.Assert;

import java.util.Queue;

@CommonsLog
public class StationsParser {

    public Stations parse(Queue<String> lines) {
        Stations stations = new Stations();

        // First line is identification
        stations.setIdentificationRecord(IFFParser.parseIdentificationRecord(lines.remove()));

        // All other lines describe stations
        while (!lines.isEmpty()) {
            stations.getStations().add(visitStationRecord(lines.remove()));
        }

        return stations;
    }

    private Stations.Station visitStationRecord(String line) {
        String[] splittedLine = line.split(",");
        Assert.isTrue(splittedLine.length == 10, "Line should have 10 parts: " + line);

        Stations.Station station = new Stations.Station();
        station.setChanges(Byte.parseByte(splittedLine[0]));
        station.setShortName(splittedLine[1].trim());
        station.setChangeTime(Byte.parseByte(splittedLine[2]));
        station.setCountryCode(splittedLine[4].trim());
        station.setTimeZone(Short.parseShort(splittedLine[5]));
        station.setName(splittedLine[9].trim());

        return station;
    }
}
