CREATE TABLE connection
(
    id        BIGINT                      NOT NULL,
    start_id  BIGINT                      NOT NULL,
    end_id    BIGINT                      NOT NULL,
    departure TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    arrival   TIMESTAMP WITHOUT TIME ZONE,
    trip_id   BIGINT                      NOT NULL,
    CONSTRAINT pk_connection PRIMARY KEY (id)
);

CREATE TABLE station
(
    id        BIGINT       NOT NULL,
    name      VARCHAR(255) NOT NULL,
    uic_code  VARCHAR(255),
    country   VARCHAR(255) NOT NULL,
    timezone  VARCHAR(255) NOT NULL,
    latitude  DECIMAL,
    longitude DECIMAL,
    CONSTRAINT pk_station PRIMARY KEY (id)
);

CREATE TABLE station_local_code
(
    id         BIGINT       NOT NULL,
    station_id BIGINT       NOT NULL,
    country    VARCHAR(255) NOT NULL,
    local_code VARCHAR(255) NOT NULL,
    CONSTRAINT pk_stationlocalcode PRIMARY KEY (id)
);

CREATE TABLE trip
(
    id             BIGINT       NOT NULL,
    company        VARCHAR(255),
    type           VARCHAR(255),
    identifier     VARCHAR(255) NOT NULL,
    service_number VARCHAR(255),
    CONSTRAINT pk_trip PRIMARY KEY (id)
);

ALTER TABLE station_local_code
    ADD CONSTRAINT station_local_code_unique_country_local_code UNIQUE (country, local_code);

ALTER TABLE station
    ADD CONSTRAINT uc_station_uiccode UNIQUE (uic_code);

ALTER TABLE trip
    ADD CONSTRAINT uc_trip_identifier UNIQUE (identifier);

CREATE INDEX idx_station_uiccode ON station (uic_code);

ALTER TABLE connection
    ADD CONSTRAINT FK_CONNECTION_ON_END FOREIGN KEY (end_id) REFERENCES station (id);

ALTER TABLE connection
    ADD CONSTRAINT FK_CONNECTION_ON_START FOREIGN KEY (start_id) REFERENCES station (id);

ALTER TABLE connection
    ADD CONSTRAINT FK_CONNECTION_ON_TRIP FOREIGN KEY (trip_id) REFERENCES trip (id);

ALTER TABLE station_local_code
    ADD CONSTRAINT FK_STATIONLOCALCODE_ON_STATION FOREIGN KEY (station_id) REFERENCES station (id);