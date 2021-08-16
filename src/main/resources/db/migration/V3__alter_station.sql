ALTER TABLE station
    ADD country VARCHAR(255);

ALTER TABLE station
    ADD latitude DECIMAL;

ALTER TABLE station
    ADD longitude DECIMAL;

CREATE INDEX IDX_STATION_UICCODE ON station (uic_code);