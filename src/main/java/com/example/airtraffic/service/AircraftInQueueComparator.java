package com.example.airtraffic.service;

import com.example.airtraffic.model.jpa.AircraftState;

import java.util.Comparator;

/**
 * Extends {@link Comparator} interface for comparing aircrafts in the queue
 */
public interface AircraftInQueueComparator extends Comparator<AircraftState> {
}
