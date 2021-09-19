package eu.railplanner.runner.importer.iff.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Stations {
    private IdentificationRecord identificationRecord;

    private List<Station> stations = new ArrayList<>();

    @Data
    public static class Station{
        private byte changes;
        private String shortName;
        private byte changeTime;
        private String countryCode;
        private short timeZone;
        private String name;
    }
}
