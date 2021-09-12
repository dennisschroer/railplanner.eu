package eu.railplanner.railplanner.model.response;

import eu.railplanner.railplanner.model.Country;
import lombok.Data;

@Data
public class StationResponse {
    private Long id;
    private String name;
    private String uicCode;
    private Country country;
}
