package eu.railplanner.core.model.timetable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Trip {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Name of the company providing this trip.
     */
    private String company;

    /**
     * Type of this trip.
     */
    @Enumerated(EnumType.STRING)
    private TripType type;

    /**
     * Unique internal identifier of this trip. Used in imports for matching on already existing trips.
     */
    @Column(nullable = false, unique = true)
    private String identifier;

    /**
     * The service number or line number of this trip.
     */
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
