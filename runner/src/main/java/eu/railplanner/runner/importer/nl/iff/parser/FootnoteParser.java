package eu.railplanner.runner.importer.nl.iff.parser;

import eu.railplanner.runner.importer.nl.iff.model.Footnote;
import lombok.extern.apachecommons.CommonsLog;

import java.util.Queue;

@CommonsLog
public class FootnoteParser {
    private Footnote footnote;

    public Footnote parse(Queue<String> lines) {
        this.footnote = new Footnote();

        String line;
        while (!lines.isEmpty()) {
            line = lines.remove();

            switch (line.charAt(0)) {
                case '@':
                    visitIdentificationRecord(line);
                    break;
                case '#':
                    visitFootnoteNumberRecord(line, lines.remove());
                    break;
                default:
                    log.error(String.format("Unknown line in IFF timetable: '%s'", line));
            }
        }

        return footnote;
    }

    private void visitIdentificationRecord(String line) {
        footnote.setIdentificationRecord(IFFParser.parseIdentificationRecord(line));
    }

    private void visitFootnoteNumberRecord(String line, String vector) {
        Footnote.Note note = new Footnote.Note();

        note.setNumber(Integer.parseInt(line.substring(1)));
        note.setVector(vector);

        footnote.getNotes().add(note);
    }
}
