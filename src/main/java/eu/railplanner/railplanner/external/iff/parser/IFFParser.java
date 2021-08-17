package eu.railplanner.railplanner.external.iff.parser;

import eu.railplanner.railplanner.external.iff.model.IdentificationRecord;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class IFFParser {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy");

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

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
