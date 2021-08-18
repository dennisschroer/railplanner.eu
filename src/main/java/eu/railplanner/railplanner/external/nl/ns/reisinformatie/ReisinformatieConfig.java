package eu.railplanner.railplanner.external.nl.ns.reisinformatie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "railplanner.external.nl.ns.reisinformatie")
public class ReisinformatieConfig {
    private boolean stationImportEnabled;

    private boolean tripImportEnabled;

    private String apiKey;
}
