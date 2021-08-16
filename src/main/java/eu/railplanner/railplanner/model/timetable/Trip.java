package eu.railplanner.railplanner.model.timetable;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Trip {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String identifier;

    public Trip(String identifier) {
        this.identifier = identifier;
    }
}
