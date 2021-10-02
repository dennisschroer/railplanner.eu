package eu.railplanner.core.repository.timetable;

import eu.railplanner.core.model.timetable.Connection;
import eu.railplanner.core.model.timetable.Connection_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ConnectionRepositoryCustomImpl implements ConnectionRepositoryCustom {
    private final EntityManager entityManager;

    public ConnectionRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Connection> findAllValidConnections(Instant startTime, short window) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Connection> query = builder.createQuery(Connection.class);

        Root<Connection> connection = query.from(Connection.class);
        query.select(connection);
        query.where(builder.and(
                builder.greaterThanOrEqualTo(connection.get(Connection_.departure), startTime),
                builder.lessThan(connection.get(Connection_.departure), startTime.plus(window, ChronoUnit.MINUTES))
        ));

        return entityManager.createQuery(query).getResultList();
    }
}
