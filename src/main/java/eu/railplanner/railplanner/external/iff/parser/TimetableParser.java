package eu.railplanner.railplanner.external.iff.parser;

import eu.railplanner.railplanner.external.iff.model.Timetable;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.util.Assert;

import java.util.Queue;

@CommonsLog
public class TimetableParser {
    private Timetable timetable;

    private Timetable.TransportService currentService;

    public Timetable parse(Queue<String> lines) {
        this.timetable = new Timetable();

        String line;
        while (!lines.isEmpty()) {
            line = lines.remove();

            switch (line.charAt(0)) {
                case '@':
                    visitIdentificationRecord(line);
                    break;
                case '#':
                    visitServiceIdentificationRecord(line);
                    break;
                case '%':
                    visitServiceNumberRecord(line);
                    break;
                case '-':
                    visitValidityRecord(line);
                    break;
                case '&':
                    visitTransportModeRecord(line);
                    break;
                case '*':
                    visitAttributeRecord(line);
                    break;
                case '>':
                    vistStartStopRecord(line);
                    break;
                case '?':
                    visitPlatformRecord(line);
                    break;
                case ';':
                    visitPassingStopRecord(line);
                    break;
                case '.':
                    visitContinuationStopRecord(line);
                    break;
                case '+':
                    visitIntervalStopRecord(line);
                    break;
                case '<':
                    visitFinalStopRecord(line);
                    break;
                default:
                    log.error(String.format("Unknown line in IFF timetable: '%s'", line));
            }
        }

        if (currentService != null) {
            timetable.getServices().add(currentService);
        }

        return timetable;
    }

    private void visitIdentificationRecord(String line) {
        timetable.setIdentificationRecord(IFFParser.parseIdentificationRecord(line));
    }

    private void visitServiceIdentificationRecord(String line) {
        if (currentService != null) {
            timetable.getServices().add(currentService);
        }

        currentService = new Timetable.TransportService();
        currentService.setIdentification(Integer.parseInt(line.substring(1)));
    }

    private void visitServiceNumberRecord(String line) {
        String[] splittedLine = line.split(",");
        Assert.isTrue(splittedLine.length == 6, "Line should have 6 parts: " + line);

        Timetable.ServiceNumber serviceNumber = new Timetable.ServiceNumber();
        serviceNumber.setCompanyNumber(Short.parseShort(splittedLine[0].substring(1)));
        serviceNumber.setServiceNumber(Integer.parseInt(splittedLine[1]));
        serviceNumber.setVariant(splittedLine[2].trim());
        serviceNumber.setFirstStop(Short.parseShort(splittedLine[3]));
        serviceNumber.setLastStop(Short.parseShort(splittedLine[4]));
        serviceNumber.setName(splittedLine[5].trim());

        currentService.getServiceNumbers().add(serviceNumber);
    }

    private void visitValidityRecord(String line) {
        String[] splittedLine = line.split(",");
        Assert.isTrue(splittedLine.length == 3, "Line should have 3 parts: " + line);

        Timetable.Validity validity = new Timetable.Validity();
        validity.setFootnoteNumber(Integer.parseInt(splittedLine[0].substring(1)));
        validity.setFirstStop(Short.parseShort(splittedLine[1]));
        validity.setLastStop(Short.parseShort(splittedLine[2]));

        currentService.getValidities().add(validity);
    }

    private void visitTransportModeRecord(String line) {
        String[] splittedLine = line.split(",");
        Assert.isTrue(splittedLine.length == 3, "Line should have 3 parts: " + line);

        Timetable.TransportMode transportMode = new Timetable.TransportMode();
        transportMode.setTransportMode(splittedLine[0].substring(1).trim());
        transportMode.setFirstStop(Short.parseShort(splittedLine[1]));
        transportMode.setLastStop(Short.parseShort(splittedLine[2]));

        currentService.getTransportModes().add(transportMode);
    }

    private void visitAttributeRecord(String line) {
        String[] splittedLine = line.split(",");
        Assert.isTrue(splittedLine.length >= 3, "Line should have >=3 parts: " + line);

        Timetable.Attribute attribute = new Timetable.Attribute();
        attribute.setAttributeCode(splittedLine[0].substring(1).trim());
        attribute.setFirstStop(Short.parseShort(splittedLine[1]));
        attribute.setLastStop(Short.parseShort(splittedLine[2]));

        currentService.getAttributes().add(attribute);
    }

    private void vistStartStopRecord(String line) {
        String[] splittedLine = line.split(",");
        Assert.isTrue(splittedLine.length == 2, "Line should have 2 parts: " + line);

        Timetable.Stop stop = new Timetable.Stop();
        stop.setStationName(splittedLine[0].substring(1).trim());
        stop.setDeparture(timeToMinutes(splittedLine[1]));

        currentService.getStops().add(stop);
    }

    private Short timeToMinutes(String time) {
        short hours = Short.parseShort(time.substring(0, 2));
        short minutes = Short.parseShort(time.substring(2));

        return (short) (hours * 60 + minutes);
    }

    private void visitPlatformRecord(String line) {
        // Ignore for now
    }

    private void visitPassingStopRecord(String line) {
        // Ignore for now
    }

    private void visitContinuationStopRecord(String line) {
        String[] splittedLine = line.split(",");
        Assert.isTrue(splittedLine.length == 2, "Line should have 2 parts: " + line);

        Timetable.Stop stop = new Timetable.Stop();
        stop.setStationName(splittedLine[0].substring(1).trim());
        stop.setArrival(timeToMinutes(splittedLine[1]));
        stop.setDeparture(stop.getArrival());

        currentService.getStops().add(stop);
    }

    private void visitIntervalStopRecord(String line) {
        String[] splittedLine = line.split(",");
        Assert.isTrue(splittedLine.length == 3, "Line should have 3 parts: " + line);

        Timetable.Stop stop = new Timetable.Stop();
        stop.setStationName(splittedLine[0].substring(1).trim());
        stop.setArrival(timeToMinutes(splittedLine[1]));
        stop.setDeparture(timeToMinutes(splittedLine[2]));

        currentService.getStops().add(stop);
    }

    private void visitFinalStopRecord(String line) {
        String[] splittedLine = line.split(",");
        Assert.isTrue(splittedLine.length == 2, "Line should have 2 parts: " + line);

        Timetable.Stop stop = new Timetable.Stop();
        stop.setStationName(splittedLine[0].substring(1).trim());
        stop.setArrival(timeToMinutes(splittedLine[1]));

        currentService.getStops().add(stop);
    }
}
