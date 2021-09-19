package eu.railplanner.runner;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "railplanner.runner")
public class RailplannerRunnerConfig {
    /**
     * The identifier of the current job to run.
     */
    private RailplannerJobs job;
}
