CREATE DATABASE airtraffic;

CREATE TABLE aircraft_state (
   id integer PRIMARY KEY,
   type text NOT NULL,
   size text NOT NULL,
   queued_timestamp bigint NOT NULL,
   dequeued_timestamp bigint
);

CREATE TABLE aircraft_queue (
   id integer PRIMARY KEY,
   started boolean NOT NULL,
   queue_size integer NOT NULL
);

