package eu.railplanner.core.model.timetable;

import eu.railplanner.core.model.Station;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;

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

    /**
     * The start station of this connection.
     */
    @ManyToOne(optional = false)
    private Station start;

    /**
     * The end station of this connection.
     */
    @ManyToOne(optional = false)
    private Station end;

    /**
     * Departure time in UTC from the start station.
     */
    @Column(nullable = false)
    private Instant departure;

    /**
     * Arrival time in UTC at the end station.
     */
    @Column(nullable = false)
    private Instant arrival;

    /**
     * Trip this connection is part of.
     */
    @ManyToOne(optional = false)
    private Trip trip;
}
