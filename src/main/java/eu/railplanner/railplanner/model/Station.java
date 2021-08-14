package eu.railplanner.railplanner.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Station {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * UIC-code van het station, identificerende code volgens de standaard van de Internationale Spoorwegunie,
     * de UIC: Union Internationale des Chemins de fer.
     */
    @Column(nullable = false, unique = true)
    private String uicCode;

    @Column(nullable = false)
    private String name;
}
