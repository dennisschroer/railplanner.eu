package eu.railplanner.railplanner.model.timetable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    private String type;

    @Column(nullable = false)
    private String identifier;

    @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY)
    private Collection<TripValidity> validities;

    public Trip(String identifier) {
        this.identifier = identifier;
    }

    public Trip(String company, String identifier) {
        this.company = company;
        this.identifier = identifier;
    }
}
