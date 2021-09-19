package eu.railplanner.runner.importer.model;

import eu.railplanner.core.model.Country;
import lombok.Builder;
import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Data
@Builder
public class ImportStation {
    /**
     * The identifier for this station used in the data format imported by this importer
     */
    @Nonnull
    private String localCode;
    /**
     * The name of this station
     */
    @Nonnull
    private String name;
    /**
     * The country this station is in
     */
    @Nonnull
    private Country country;
    /**
     * The UIC-code of this station. Not all data sets provide this
     */
    @Nullable
    private String uicCode;
}
