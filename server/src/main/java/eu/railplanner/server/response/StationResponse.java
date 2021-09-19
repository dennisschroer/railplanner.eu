package eu.railplanner.server.response;

import eu.railplanner.core.model.Country;
import lombok.Data;

@Data
public class StationResponse {
    private Long id;
    private String name;
    private String uicCode;
    private Country country;
}
