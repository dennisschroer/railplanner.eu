package eu.railplanner.runner.importer.iff.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@Data
@RequiredArgsConstructor
public class IFF {
    private final Path path;

    private Delivery delivery;

    private Timetable timetable;

    private Footnote footnote;

    private Stations stations;
}
