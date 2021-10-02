package eu.railplanner.server.response;

import lombok.Data;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Data
public class ConnectionResponse {
    private Long startId;
    private Long endId;

    private ZonedDateTime departure;
    private ZonedDateTime arrival;
}
