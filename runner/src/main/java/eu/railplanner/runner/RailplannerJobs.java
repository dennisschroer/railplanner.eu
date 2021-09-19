package eu.railplanner.runner;

import eu.railplanner.runner.importer.nl.iff.IFFImport;
import eu.railplanner.runner.importer.nl.ns.reisinformatie.StationImport;
import eu.railplanner.runner.job.RailplannerJob;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RailplannerJobs {
    IMPORTER_NL_NS_REISPLANNER_STATIONS(StationImport.class),
    IMPORTER_NL_IFF(IFFImport.class);

    private Class<? extends RailplannerJob> beanClass;
}
