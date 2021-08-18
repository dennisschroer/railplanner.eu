package eu.railplanner.railplanner.external.iff.parser;

import eu.railplanner.railplanner.external.iff.model.Delivery;
import eu.railplanner.railplanner.external.iff.model.IFF;
import eu.railplanner.railplanner.external.iff.model.IdentificationRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

@RequiredArgsConstructor
public class IFFParser {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy");

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

    public static final String DELIVERY_FILE = "delivery.dat";
    public static final String FOOTNOTE_FILE = "footnote.dat";
    public static final String TIMETABLE_FILE = "timetbls.dat";

    public IFF load(Path path) throws IOException {
        IFF iff = new IFF(path);
        loadDelivery(iff);
        loadTimetable(iff);
        loadFootnote(iff);
        return iff;
    }

    private void loadDelivery(IFF iff) throws IOException {
        List<String> lines = Files.readAllLines(iff.getPath().resolve(DELIVERY_FILE));
        Assert.isTrue(lines.size() == 1);

        iff.setDelivery(new Delivery());
        iff.getDelivery().setIdentificationRecord(IFFParser.parseIdentificationRecord(lines.get(0)));
    }

    private void loadTimetable(IFF iff) throws IOException {
        Queue<String> lines = new ArrayDeque<>(Files.readAllLines(iff.getPath().resolve(TIMETABLE_FILE)));
        Assert.isTrue(lines.size() >= 1);

        TimetableParser parser = new TimetableParser();
        iff.setTimetable(parser.parse(lines));
    }

    private void loadFootnote(IFF iff) throws IOException {
        Queue<String> lines = new ArrayDeque<>(Files.readAllLines(iff.getPath().resolve(FOOTNOTE_FILE)));
        Assert.isTrue(lines.size() >= 1);

        FootnoteParser parser = new FootnoteParser();
        iff.setFootnote(parser.parse(lines));
    }

    public static IdentificationRecord parseIdentificationRecord(String line) {
        String[] splittedLine = line.split(",");
        Assert.isTrue(splittedLine.length == 5);

        IdentificationRecord identificationRecord = new IdentificationRecord();
        identificationRecord.setCompanyNumber(Short.parseShort(splittedLine[0].substring(1)));
        identificationRecord.setFirstDay(LocalDate.parse(splittedLine[1], DATE_FORMATTER));
        identificationRecord.setLastDay(LocalDate.parse(splittedLine[2], DATE_FORMATTER));
        identificationRecord.setVersion(Short.parseShort(splittedLine[3]));
        identificationRecord.setDescription(splittedLine[4].trim());

        return identificationRecord;
    }
}
