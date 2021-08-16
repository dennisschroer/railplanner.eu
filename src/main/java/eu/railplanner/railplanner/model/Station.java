package eu.railplanner.railplanner.model;

import lombok.Data;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Table(name = "station", indexes = {
        @Index(name = "idx_station_uiccode", columnList = "uicCode")
})
@Data
@Entity
public class Station {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    /**
     * UIC-code of the station. This code identifies the station following to the standards from the UIC,
     * Union Internationale des Chemins de fer.
     */
    @Column(nullable = false, unique = true)
    private String uicCode;

    @Column()
    @Enumerated(EnumType.STRING)
    private Country country;

    @Nullable
    @Embedded
    private Location location;
}
