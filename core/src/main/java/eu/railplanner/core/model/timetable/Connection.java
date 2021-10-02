package eu.railplanner.core.model.timetable;

import eu.railplanner.core.model.Station;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * An elementary connection between two stops without stops in between, as part of a trip.
 */
@Getter
@Setter
@Entity
public class Connection {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Station start;

    @ManyToOne(optional = false)
    private Station end;

    /**
     * Departure time in UTC.
     */
    @Column(nullable = false)
    private Instant departure;

    /**
     * Arrival time in UTC.
     */
    @Column(nullable = false)
    private Instant arrival;

    @ManyToOne(optional = false)
    private Trip trip;
}
