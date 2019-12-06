package com.example.airtraffic.exception;

public class QueueIsNotStarted extends Exception {
    public QueueIsNotStarted() {
        super("Aircraft queue manager is not started");
    }
}
