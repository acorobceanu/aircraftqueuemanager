package com.example.airtraffic.service.impl;

import com.example.airtraffic.model.AircraftSize;
import com.example.airtraffic.model.AircraftType;
import com.example.airtraffic.model.jpa.AircraftState;
import com.example.airtraffic.service.AircraftInQueueComparator;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Default priority implementation of {@link AircraftInQueueComparator} interface.
 * Priority is determined according following rules:
 * <ul>
 * <li>a. VIP aircraft has precedence over all other ACs except Emergency. Emergency aircraft has highest priority.
 * <li>b. Passenger AC’s have removal precedence over Cargo AC’s.
 * <li>c. Large AC’s of a given type have removal precedence over Small AC’s of the same type.
 * <li>d. Earlier enqueued AC’s of a given type and size have precedence over later enqueued AC’s of the same type and size.
 * </ul>
 */
@Slf4j
@Component
public class DefaultPriorityStrategy implements AircraftInQueueComparator {
    private final Map<AircraftType, Integer> typePriority = ImmutableMap.of(
            AircraftType.Emergency, 1,
            AircraftType.VIP, 2,
            AircraftType.Passenger, 3,
            AircraftType.Cargo, 4
    );

    @Override
    public int compare(AircraftState aircraftState1, AircraftState aircraftState2) {
        log.info("Comparing {} with {}", aircraftState1, aircraftState2);

        AircraftType type1 = AircraftType.valueOf(aircraftState1.getType());
        AircraftType type2 = AircraftType.valueOf(aircraftState2.getType());
        if (type1 != type2) {
            log.debug("Different aircraft types, comparing by priority");
            return Integer.compare(
                    getPriority(aircraftState1),
                    getPriority(aircraftState2));
        }

        AircraftSize size1 = AircraftSize.valueOf(aircraftState1.getSize());
        AircraftSize size2 = AircraftSize.valueOf(aircraftState2.getSize());
        if (size1 != size2) {
            log.debug("Different aircraft size, comparing by size");
            return size1 == AircraftSize.LARGE ? -1 : 1;
        }

        log.debug("Same aircraft type and size, comparing by queued timestamp");
        return Long.compare(
                // inverse compare as earliest (smaller) timestamp has priority over later (bigger) timestamp
                getTimestamp(aircraftState1),
                getTimestamp(aircraftState2));
    }

    private int getPriority(AircraftState state) {
        AircraftType type = AircraftType.valueOf(state.getType());
        if (!typePriority.containsKey(type)) {
            throw new RuntimeException(
                    String.format("Invalid aircraft type %s.", type));
        }
        Integer priority = typePriority.get(type);
        log.debug("Aircraft type {} has priority {}", type, priority);
        return priority;
    }

    private long getTimestamp(AircraftState state) {
        if (state.getQueuedTimestamp() == null) {
            throw new RuntimeException(
                    "Invalid aircraft state. Aircraft in the queue without queued timestamp");
        }
        return state.getQueuedTimestamp().longValue();
    }
}
