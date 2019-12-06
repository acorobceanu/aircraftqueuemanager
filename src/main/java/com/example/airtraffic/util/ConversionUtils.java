package com.example.airtraffic.util;

import com.example.airtraffic.model.Aircraft;
import com.example.airtraffic.model.AircraftSize;
import com.example.airtraffic.model.AircraftType;
import com.example.airtraffic.model.jpa.AircraftState;

/**
 * Helper class for conversion operations
 */
public class ConversionUtils {
    public static Aircraft fromEntity(AircraftState aircraftState) {
        if (aircraftState == null) {
            return null;
        }
        return Aircraft.builder()
                .id(aircraftState.getId())
                .size(AircraftSize.valueOf(aircraftState.getSize()))
                .type(AircraftType.valueOf(aircraftState.getType()))
                .queueTimestamp(aircraftState.getQueuedTimestamp())
                .build();
    }

    public static AircraftState toEntity(Aircraft aircraft) {
        if (aircraft == null) {
            return null;
        }
        return AircraftState.builder()
                .id(aircraft.getId())
                .size(aircraft.getSize().name())
                .type(aircraft.getType().name())
                .build();
    }
}
