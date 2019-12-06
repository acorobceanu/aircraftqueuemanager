package com.example.airtraffic.service;

import com.example.airtraffic.TestUtils;
import com.example.airtraffic.model.Aircraft;
import com.example.airtraffic.model.AircraftSize;
import com.example.airtraffic.model.AircraftType;
import com.example.airtraffic.model.jpa.AircraftQueue;
import com.example.airtraffic.repository.AircraftQueueRepository;
import com.example.airtraffic.repository.AircraftStateRepository;
import com.example.airtraffic.service.impl.DefaultPriorityStrategy;
import com.example.airtraffic.service.impl.PriorityQueueManager;
import com.example.airtraffic.util.ConversionUtils;
import lombok.SneakyThrows;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;

import java.util.Optional;

@RunWith(JUnit4.class)
public class PriorityQueueManagerTest {
    private PriorityQueueManager sut;

    @Mock
    private AircraftQueueRepository aircraftQueueRepository;

    @Mock
    private AircraftStateRepository aircraftStateRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        sut = new PriorityQueueManager(
                new DefaultPriorityStrategy(),
                aircraftQueueRepository,
                aircraftStateRepository);

        when(aircraftQueueRepository.findById(PriorityQueueManager.QUEUE_ID))
                .thenReturn(
                        Optional.of(
                                AircraftQueue.builder()
                                        .id(PriorityQueueManager.QUEUE_ID)
                                        .started(true)
                                        .queueSize(0)
                                        .build()));
        sut.start();
    }

    @Test
    @SneakyThrows
    public void testValidatePriorityQueue() {
        // setup
        Aircraft vipLargeAircraft = ConversionUtils.fromEntity(
                TestUtils.getAircraft(AircraftType.VIP, AircraftSize.LARGE));
        sut.enqueue(vipLargeAircraft);

        sut.enqueue(
                ConversionUtils.fromEntity(
                        TestUtils.getAircraft(AircraftType.Passenger, AircraftSize.LARGE)));

        sut.enqueue(
                ConversionUtils.fromEntity(
                        TestUtils.getAircraft(AircraftType.VIP, AircraftSize.SMALL)));

        // execution
        Optional<Aircraft> removedOpt = sut.dequeue();

        // assertions
        assertTrue(removedOpt.isPresent());
        Aircraft removed = removedOpt.get();
        assertNotNull(removed.getQueueTimestamp());
        assertEquals(vipLargeAircraft.getId(), removed.getId());
        assertEquals(vipLargeAircraft.getType(), removed.getType());
        assertEquals(vipLargeAircraft.getSize(), removed.getSize());
    }

}
