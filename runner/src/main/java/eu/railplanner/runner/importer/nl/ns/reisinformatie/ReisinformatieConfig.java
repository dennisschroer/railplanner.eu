package eu.railplanner.runner.importer.nl.ns.reisinformatie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "railplanner.importer.nl.ns.reisinformatie")
public class ReisinformatieConfig {
    private String apiKey;
}
