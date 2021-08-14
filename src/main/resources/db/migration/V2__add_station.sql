CREATE TABLE station
(
    id       BIGINT       NOT NULL,
    uic_code VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_station PRIMARY KEY (id)
);

ALTER TABLE station
    ADD CONSTRAINT uc_station_uiccode UNIQUE (uic_code);