#!/bin/bash

docker-compose exec database pg_dump -O -U railplanner -d railplanner > backup.sql