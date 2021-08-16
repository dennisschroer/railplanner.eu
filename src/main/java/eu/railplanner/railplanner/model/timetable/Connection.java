package eu.railplanner.railplanner.model.timetable;

import eu.railplanner.railplanner.model.Station;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;

@Data
@Entity
public class Connection {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Station start;

    @ManyToOne(optional = false)
    private Station end;

    @Column(nullable = false)
    private OffsetDateTime departure;

    @Column
    private OffsetDateTime arrival;

    @ManyToOne(optional = false)
    private Trip trip;
}
