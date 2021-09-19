package eu.railplanner.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableConfigurationProperties
@EntityScan({"eu.railplanner.core", "eu.railplanner.runner"})
@EnableJpaRepositories({"eu.railplanner.core", "eu.railplanner.runner"})
@SpringBootApplication(scanBasePackages = {"eu.railplanner.core", "eu.railplanner.runner"})
public class RailplannerRunner {

    public static void main(String[] args) {
        SpringApplication.run(RailplannerRunner.class, args);
    }

}
