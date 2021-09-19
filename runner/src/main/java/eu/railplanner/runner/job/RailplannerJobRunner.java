package eu.railplanner.runner.job;

import eu.railplanner.runner.RailplannerJobs;
import eu.railplanner.runner.RailplannerRunnerConfig;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@CommonsLog
public class RailplannerJobRunner implements ApplicationRunner {

    @Autowired
    private RailplannerRunnerConfig railplannerRunnerConfig;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) {
        RailplannerJobs jobIdentifier = railplannerRunnerConfig.getJob();
        RailplannerJob job = applicationContext.getBean(jobIdentifier.getBeanClass());
        log.info(String.format("Starting job '%s' for identifier %s", jobIdentifier.getBeanClass().getSimpleName(), jobIdentifier.name()));
        job.run();
    }
}
