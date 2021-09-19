package eu.railplanner.core.repository.timetable;

import eu.railplanner.core.model.timetable.Connection;
import eu.railplanner.core.model.timetable.Connection_;
import eu.railplanner.core.model.timetable.Trip;
import eu.railplanner.core.model.timetable.TripValidity;
import eu.railplanner.core.model.timetable.TripValidity_;
import eu.railplanner.core.model.timetable.Trip_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CollectionJoin;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

public class ConnectionRepositoryCustomImpl implements ConnectionRepositoryCustom {
    private final EntityManager entityManager;

    public ConnectionRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Connection> findAllValidConnections(LocalDate date, short startTime, short window) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Connection> query = builder.createQuery(Connection.class);

        Root<Connection> connection = query.from(Connection.class);
        Join<Connection, Trip> trip = connection.join(Connection_.trip);
        CollectionJoin<Trip, TripValidity> tripValidity = trip.join(Trip_.validities);

        query.select(connection);
        query.where(builder.and(
                builder.equal(tripValidity.get(TripValidity_.date), date)),
                builder.greaterThanOrEqualTo(connection.get(Connection_.departure), startTime),
                builder.lessThan(connection.get(Connection_.departure), (short) (startTime + window))
        );

        return entityManager.createQuery(query).getResultList();
    }
}
