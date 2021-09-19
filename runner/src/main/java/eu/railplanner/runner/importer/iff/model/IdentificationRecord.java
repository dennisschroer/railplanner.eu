package eu.railplanner.runner.importer.iff.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IdentificationRecord {
    private short companyNumber;

    private LocalDate firstDay;

    private LocalDate lastDay;

    private short version;

    private String description;
}
