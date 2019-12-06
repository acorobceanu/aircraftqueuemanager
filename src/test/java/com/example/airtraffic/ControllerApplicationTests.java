package com.example.airtraffic;

import com.example.airtraffic.service.AircraftInQueueComparator;
import com.example.airtraffic.service.QueueManager;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ControllerApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
    	assertNotNull(context);
        assertNotNull(context.getBean(QueueManager.class));
        assertNotNull(context.getBean(AircraftInQueueComparator.class));
    }

}
