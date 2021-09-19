package eu.railplanner.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"eu.railplanner.core", "eu.railplanner.runner"})
@EnableConfigurationProperties
public class RailplannerRunner {

    public static void main(String[] args) {
        SpringApplication.run(RailplannerRunner.class, args);
    }

}
