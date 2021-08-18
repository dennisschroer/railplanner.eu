package eu.railplanner.railplanner.external.iff;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "railplanner.external.iff")
public class IFFConfig {
    private boolean enabled;
}
