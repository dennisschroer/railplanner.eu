package eu.railplanner.railplanner.external.iff;

import eu.railplanner.railplanner.external.ImportRunnable;
import eu.railplanner.railplanner.external.iff.model.IFF;
import eu.railplanner.railplanner.external.iff.parser.IFFParser;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@CommonsLog
@Component
public class IFFImport implements ImportRunnable {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void run() {
        IFFParser parser = new IFFParser();
        Path path = Path.of("./import/iff/NDOV_32_33_34_35");

        try {
            IFF iff = parser.load(path);

            log.info(String.format("Loaded %s", iff.getDelivery()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
