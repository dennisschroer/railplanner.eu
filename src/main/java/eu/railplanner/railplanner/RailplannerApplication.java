package eu.railplanner.railplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableConfigurationProperties
public class RailplannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RailplannerApplication.class, args);
	}

}
