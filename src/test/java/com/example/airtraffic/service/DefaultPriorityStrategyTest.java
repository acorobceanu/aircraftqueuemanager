package com.example.airtraffic.service;

import com.example.airtraffic.TestUtils;
import com.example.airtraffic.model.AircraftSize;
import com.example.airtraffic.model.AircraftType;
import com.example.airtraffic.model.jpa.AircraftState;
import com.example.airtraffic.service.impl.DefaultPriorityStrategy;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Random;

/**
 * Tests few scenarios for {@link DefaultPriorityStrategy} aircraft state comparator.
 * TODO: Add more code to cover all scenarios
 */
@RunWith(JUnit4.class)
public class DefaultPriorityStrategyTest {
    private DefaultPriorityStrategy sut;

    @Before
    public void setUp() {
        sut = new DefaultPriorityStrategy();
    }

    @Test
    public void testTwoLargeSameTypeEarlierPriority() {
        // Same size and type aircraft the one queued earlier has the priority
        assertEquals(1,
                sut.compare(
                        TestUtils.getAircraft(AircraftType.VIP, AircraftSize.SMALL, 101L),
                        TestUtils.getAircraft(AircraftType.VIP, AircraftSize.SMALL, 100L)));
    }

    @Test
    public void testTwoEmergencyLargePriority() {
        // Large aircraft of the same type has priority
        assertEquals(1,
                sut.compare(
                        TestUtils.getAircraft(AircraftType.VIP, AircraftSize.SMALL),
                        TestUtils.getAircraft(AircraftType.VIP, AircraftSize.LARGE)));
    }

    @Test
    public void testEmergencyHighestPriority() {
        // Emergency is top priority
        assertEquals(1,
                sut.compare(
                        TestUtils.getAircraft(AircraftType.VIP, AircraftSize.SMALL),
                        TestUtils.getAircraft(AircraftType.Emergency, AircraftSize.SMALL)));
        assertEquals(1,
                sut.compare(
                        TestUtils.getAircraft(AircraftType.Passenger, AircraftSize.SMALL),
                        TestUtils.getAircraft(AircraftType.Emergency, AircraftSize.SMALL)));
        assertEquals(1,
                sut.compare(
                        TestUtils.getAircraft(AircraftType.Cargo, AircraftSize.SMALL),
                        TestUtils.getAircraft(AircraftType.Emergency, AircraftSize.SMALL)));
    }
}
