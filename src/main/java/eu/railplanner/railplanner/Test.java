package eu.railplanner.railplanner;

import eu.railplanner.railplanner.external.nl.ns.reisinformatie.Runner;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@CommonsLog
public class Test implements ApplicationRunner {

    @Autowired
    private Runner runner;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(String.format("Running %s", runner.getClass().getName()));
        runner.run();
    }
}
