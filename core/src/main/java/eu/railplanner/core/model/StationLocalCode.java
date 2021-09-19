package eu.railplanner.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
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

@Data
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

    @ManyToOne(optional = false)
    private Station station;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Country country;

    @Column(nullable = false)
    private String localCode;
}
