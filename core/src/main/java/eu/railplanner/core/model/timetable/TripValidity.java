package eu.railplanner.core.model.timetable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class TripValidity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Trip trip;

    private LocalDate date;
}
