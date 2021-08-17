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
        // Ignore for now
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

        Timetable.StartStop stop = new Timetable.StartStop();
        stop.setStationName(splittedLine[0].substring(1).trim());
        stop.setDepartureHour(Byte.parseByte(splittedLine[1].substring(0, 2)));
        stop.setDepartureMinute(Byte.parseByte(splittedLine[1].substring(2)));

        currentService.getStops().add(stop);
    }

    private void visitPlatformRecord(String line) {
        // Ignore for now
    }

    private void visitPassingStopRecord(String line) {
        Timetable.PassingStop stop = new Timetable.PassingStop();
        stop.setStationName(line.substring(1).trim());
        currentService.getStops().add(stop);
    }

    private void visitContinuationStopRecord(String line) {
        String[] splittedLine = line.split(",");
        Assert.isTrue(splittedLine.length == 2, "Line should have 2 parts: " + line);

        Timetable.ContinuationStop stop = new Timetable.ContinuationStop();
        stop.setStationName(splittedLine[0].substring(1).trim());
        stop.setHour(Byte.parseByte(splittedLine[1].substring(0, 2)));
        stop.setMinute(Byte.parseByte(splittedLine[1].substring(2)));

        currentService.getStops().add(stop);
    }

    private void visitIntervalStopRecord(String line) {
        String[] splittedLine = line.split(",");
        Assert.isTrue(splittedLine.length == 3, "Line should have 3 parts: " + line);

        Timetable.IntervalStop stop = new Timetable.IntervalStop();
        stop.setStationName(splittedLine[0].substring(1).trim());
        stop.setArrivalHour(Byte.parseByte(splittedLine[1].substring(0, 2)));
        stop.setArrivalMinute(Byte.parseByte(splittedLine[1].substring(2)));
        stop.setDepartureHour(Byte.parseByte(splittedLine[2].substring(0, 2)));
        stop.setDepartureMinute(Byte.parseByte(splittedLine[2].substring(2)));

        currentService.getStops().add(stop);
    }

    private void visitFinalStopRecord(String line) {
        String[] splittedLine = line.split(",");
        Assert.isTrue(splittedLine.length == 2, "Line should have 2 parts: " + line);

        Timetable.FinalStop stop = new Timetable.FinalStop();
        stop.setStationName(splittedLine[0].substring(1).trim());
        stop.setArrivalHour(Byte.parseByte(splittedLine[1].substring(0, 2)));
        stop.setArrivalMinute(Byte.parseByte(splittedLine[1].substring(2)));

        currentService.getStops().add(stop);
    }
}
