package eu.railplanner.core.model.timetable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Trip {
    @Id
    @GeneratedValue
    private Long id;

    private String company;

    @Enumerated(EnumType.STRING)
    private TripType type;

    @Column(nullable = false, unique = true)
    private String identifier;

    private String serviceNumber;

    public Trip(String identifier) {
        this.identifier = identifier;
    }

    public Trip(String company, String identifier, String serviceNumber) {
        this.company = company;
        this.identifier = identifier;
        this.serviceNumber = serviceNumber;
    }
}
