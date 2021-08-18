package eu.railplanner.railplanner;

import eu.railplanner.railplanner.external.ImportRunnable;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@CommonsLog
public class ImportsRunner implements ApplicationRunner {

    private final List<ImportRunnable> importRunnables;

    public ImportsRunner(List<ImportRunnable> importRunnables) {
        this.importRunnables = importRunnables;
    }

    @Override
    public void run(ApplicationArguments args) {
        importRunnables.forEach(this::runImportRunnable);
    }

    private void runImportRunnable(ImportRunnable importRunnable) {
        if (importRunnable.isEnabled()) {
            log.info(String.format("Running %s", importRunnable.getClass().getName()));
            importRunnable.run();
        } else {
            log.info(String.format("Skipping %s", importRunnable.getClass().getName()));
        }
    }
}
