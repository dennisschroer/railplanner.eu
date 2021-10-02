package eu.railplanner.runner.importer.model;

import eu.railplanner.core.model.timetable.TripType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ImportTrip {
    private String serviceNumber;

    private String company;

    private TripType type;

    private List<LocalDate> dates;

    private List<ImportConnection> connections;
}
