package eu.railplanner.runner.importer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImportConnection {
    /**
     * Local code of the importer of the start station of this connection.
     */
    private String startLocalCode;

    /**
     * Local code of the importer of the end station of this connection.
     */
    private String endLocalCode;

    /**
     * Departure in minutes since the start of the day, in the time zone of the import.
     */
    private Short departure;
    /**
     * Arrival in minutes since the start of the day, in the time zone of the import.
     */
    private Short arrival;
}
