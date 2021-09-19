package eu.railplanner.runner.importer.iff.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Footnote {
    private IdentificationRecord identificationRecord;

    private List<Note> notes = new ArrayList<>();

    @Data
    public static class Note{
        private int number;
        private String vector;
    }
}
