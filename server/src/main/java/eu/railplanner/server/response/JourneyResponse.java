package eu.railplanner.server.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JourneyResponse {
    private List<ConnectionResponse> journey = new ArrayList<>();

    private List<StationResponse> stations = new ArrayList<>();
}
