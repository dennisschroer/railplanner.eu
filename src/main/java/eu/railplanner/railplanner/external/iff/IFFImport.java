package eu.railplanner.railplanner.external.iff;

import eu.railplanner.railplanner.external.ImportRunnable;
import eu.railplanner.railplanner.external.iff.model.IFF;
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
        IFF iff = new IFF(Path.of("./iff/NDOV_32_33_34_35"));
        try {
            iff.load();

            log.info(String.format("Loaded %s", iff.getDelivery()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
