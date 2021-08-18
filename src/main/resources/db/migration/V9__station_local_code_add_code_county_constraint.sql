ALTER TABLE station_local_code
    ADD CONSTRAINT station_local_code_unique_country_local_code UNIQUE (country, local_code);