package eu.railplanner.core.model;

import eu.railplanner.core.converter.ZoneIdConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.ZoneId;
import java.util.List;

@Table(name = "station", indexes = {
        @Index(name = "idx_station_uiccode", columnList = "uicCode")
})
@Setter
@Getter
@Entity
@NoArgsConstructor
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
    @Column(unique = true)
    private String uicCode;

    /**
     * Code of the station used by the local country.
     * Can be used to match timetable imports.
     */
    @OneToMany(mappedBy = "station", fetch = FetchType.EAGER)
    private List<StationLocalCode> localCodes;

    /**
     * The country this station is located in.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Country country;

    /**
     * Optional the location of this station on the world.
     */
    @Nullable
    @Embedded
    private Location location;

    /**
     * The local timezone of this station.
     */
    @Nonnull
    @Column(nullable = false)
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId timezone;
}
