# Railplanner

Plan your train trip in Europe, as easy as pie.

[![Build and run tests](https://github.com/dennisschroer/railplanner.eu/actions/workflows/test.yaml/badge.svg?branch=main)](https://github.com/dennisschroer/railplanner.eu/actions/workflows/test.yaml)

## Motivation

Traveling by train should be easy and fun. Take traveling by plane. It is very easy to find the best and cheapest routes
to your destination, and book tickets without ever entering the website of the airplane company. Why not extend this to
trains?

I think that a lot more people would consider traveling by train, if it were as simple as that.
Unfortunately, the current state requires you to buy train tickets from individual train companies, or buy something like
an EUrail/InterRail ticket. Although some countries offer international train tickets, these only include tickets for
routes (partly) serviced by these companies.

### Goal

This project aims to explore the feasibility of a rail planner and booking application for the whole of Europe.

### Open Source

Part of making train travel easy and fun is because I believe it is better for nature if we leave the plane or car and
use public transport more often. This is something we can only do together. Open Source enables this.

Check the [license](LICENSE.txt)

## Challenges

There are a couple of challenges:

1. Import the timetable of all trains in Europe converged to a single data format.
2. Implement a journey planning algorithm
3. Incorporate live disruptions and delays
4. Find prices and/or tickets for the journeys
5. If possible: book tickets through the site

### Algorithms

- Dijkstra
- CSA
- RAPTOR

## Terminology

| Term          | Explanation   |
| ------------- | ------------- |
| Stop          | A location where a train stops, a station. |
| Connection    | An elementary part of a trip, connecting two stops with a departure and arrival time.   |
| Trip          | A set of connections serviced by a single train on a specific time and day.  |
| Route         | A set of trips sharing the same stops/connections. |
| Journey       | A set of trips connecting a starting stop to a destination stop. |

