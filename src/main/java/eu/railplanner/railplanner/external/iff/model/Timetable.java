package eu.railplanner.railplanner.external.iff.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;
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
    public static abstract class Stop {
        private String stationName;
    }

    /**
     * This stop is the start of a service
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class StartStop extends Stop {
        private byte departureHour;
        private byte departureMinute;
    }

    /**
     * Stop where arrival time is same as departure time.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class ContinuationStop extends Stop {
        private byte hour;
        private byte minute;
    }

    /**
     * Stop where the service just passes.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class PassingStop extends Stop {
    }

    /**
     * Stop where arrival time differs from departure time.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class IntervalStop extends Stop {
        private byte arrivalHour;
        private byte arrivalMinute;
        private byte departureHour;
        private byte departureMinute;
    }

    /**
     * This stop is the end of a service.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class FinalStop extends Stop {
        private byte arrivalHour;
        private byte arrivalMinute;
    }
}
