package eu.railplanner.railplanner.external.iff.model;

import eu.railplanner.railplanner.external.iff.parser.FootnoteParser;
import eu.railplanner.railplanner.external.iff.parser.IFFParser;
import eu.railplanner.railplanner.external.iff.parser.TimetableParser;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

@Data
@RequiredArgsConstructor
public class IFF {
    public static final Charset IFF_CHARSET = StandardCharsets.ISO_8859_1;
    public static final String LINE_TERMINATION = "\n\r";


    public static final String DELIVERY_FILE = "delivery.dat";
    public static final String FOOTNOTE_FILE = "footnote.dat";
    public static final String TIMETABLE_FILE = "timetbls.dat";

    private final Path path;

    private Delivery delivery;

    private Timetable timetable;

    private Footnote footnote;

    public void load() throws IOException {
        loadDelivery();
        loadTimetable();
        loadFootnote();
    }

    private void loadDelivery() throws IOException {
        List<String> lines = Files.readAllLines(path.resolve(DELIVERY_FILE));
        Assert.isTrue(lines.size() == 1);

        delivery = new Delivery();
        delivery.setIdentificationRecord(IFFParser.parseIdentificationRecord(lines.get(0)));
    }

    private void loadTimetable() throws IOException {
        Queue<String> lines = new ArrayDeque<>(Files.readAllLines(path.resolve(TIMETABLE_FILE)));
        Assert.isTrue(lines.size() >= 1);

        TimetableParser parser = new TimetableParser();
        timetable = parser.parse(lines);
    }

    private void loadFootnote() throws IOException {
        Queue<String> lines = new ArrayDeque<>(Files.readAllLines(path.resolve(FOOTNOTE_FILE)));
        Assert.isTrue(lines.size() >= 1);

        FootnoteParser parser = new FootnoteParser();
        footnote = parser.parse(lines);
    }
}
