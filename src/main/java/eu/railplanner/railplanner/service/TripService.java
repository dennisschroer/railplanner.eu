package eu.railplanner.railplanner.service;

import eu.railplanner.railplanner.model.timetable.Connection;
import eu.railplanner.railplanner.model.timetable.Trip;
import eu.railplanner.railplanner.model.timetable.TripValidity;

import javax.annotation.Nonnull;

public interface TripService {
    Trip save(@Nonnull Trip trip);

    Connection save(@Nonnull Connection connection);

    TripValidity save(@Nonnull TripValidity tripValidity);
}
