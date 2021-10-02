package eu.railplanner.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Companies tend to use short codes for stations instead of the name or UIC-code. Using the {@link StationLocalCode} such
 * a codes can be linked to stations.
 */
@Setter
@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "station_local_code_unique_country_local_code", columnNames = {"country", "localCode"})
})
@ToString(exclude = "station")
@NoArgsConstructor
public class StationLocalCode {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The station this local code is linked to.
     */
    @ManyToOne(optional = false)
    private Station station;

    /**
     * The country in which the local code is used.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Country country;

    /**
     * The code used by the company to identify the station.
     */
    @Column(nullable = false)
    private String localCode;
}
