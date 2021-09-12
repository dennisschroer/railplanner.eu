package eu.railplanner.railplanner.model.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ConnectionResponse {
    private Long startId;
    private Long endId;

    private OffsetDateTime departure;
    private OffsetDateTime arrival;
}
