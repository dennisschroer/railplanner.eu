package eu.railplanner.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableConfigurationProperties
@EntityScan({"eu.railplanner.core", "eu.railplanner.server"})
@EnableJpaRepositories({"eu.railplanner.core", "eu.railplanner.server"})
@SpringBootApplication(scanBasePackages = {"eu.railplanner.core", "eu.railplanner.server"})
public class RailplannerServer {

    public static void main(String[] args) {
        SpringApplication.run(RailplannerServer.class, args);
    }

}

