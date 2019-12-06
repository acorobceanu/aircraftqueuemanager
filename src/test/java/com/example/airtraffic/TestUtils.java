package com.example.airtraffic;

import com.example.airtraffic.model.AircraftSize;
import com.example.airtraffic.model.AircraftType;
import com.example.airtraffic.model.jpa.AircraftState;

import java.util.Random;

/**
 * Common test utilities class
 */
public class TestUtils {
    public static AircraftState getAircraft(
            AircraftType type,
            AircraftSize size) {
        return AircraftState.builder()
                .id((new Random()).nextInt())
                .type(type.name())
                .size(size.name())
                .queuedTimestamp((new Random()).nextLong())
                .build();
    }

    public static AircraftState getAircraft(
            AircraftType type,
            AircraftSize size,
            Long timestamp) {
        return AircraftState.builder()
                .id((new Random()).nextInt())
                .type(type.name())
                .size(size.name())
                .queuedTimestamp(timestamp)
                .build();
    }
}
