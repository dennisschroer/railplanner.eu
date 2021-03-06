package eu.railplanner.runner.importer.nl.iff.model;

import lombok.Data;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Timetable {
    private IdentificationRecord identificationRecord;

    private List<TransportService> services = new ArrayList<>();

    @Data
    public static class TransportService {
        private int identification;

        private List<ServiceNumber> serviceNumbers = new ArrayList<>();

        private List<Validity> validities = new ArrayList<>();

        private List<TransportMode> transportModes = new ArrayList<>();

        private List<Attribute> attributes = new ArrayList<>();

        private List<Stop> stops = new ArrayList<>();
    }

    @Data
    public static class ServiceNumber {
        private short companyNumber;

        private int serviceNumber;

        private String variant;

        private short firstStop;

        private short lastStop;

        private String name;
    }

    @Data
    public static class Validity {
        private int footnoteNumber;

        private short firstStop;

        private short lastStop;
    }

    @Data
    public static class TransportMode {
        private String transportMode;

        private short firstStop;

        private short lastStop;
    }

    @Data
    public static class Attribute {
        private String attributeCode;

        private short firstStop;

        private short lastStop;
    }

    @Data
    public static class Stop {
        private String stationName;

        /**
         * Amount of time in minutes since the start of the day the train will arrive on this stop.
         *
         * Is null if this is the first stop.
         */
        @Nullable
        private Short arrival;

        /**
         * Amount of time in minutes ince the start of the day the train will depart from on this stop.
         *
         * Is null if this is the final stop.
         */
        @Nullable
        private Short departure;
    }
}
