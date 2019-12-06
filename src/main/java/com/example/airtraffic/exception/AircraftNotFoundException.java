package com.example.airtraffic.exception;

public class AircraftNotFoundException extends Exception {
    public AircraftNotFoundException() {
        super("Aircraft not fount in the queue");
    }
}
