package com.example.airtraffic.controller;

import com.example.airtraffic.exception.AircraftNotFoundException;
import com.example.airtraffic.exception.QueueIsNotStarted;
import com.example.airtraffic.model.Aircraft;
import com.example.airtraffic.service.QueueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QueueController {

    private final QueueManager queueManager;

    @Autowired
    public QueueController(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @GetMapping("/aircraft/list")
    public List<Aircraft> listQueuedAircrafts() {
        return queueManager.listQueued();
    }

    @PostMapping(path = "/aircraft/insert",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Aircraft queueAircraft(@RequestBody Aircraft aircraft) throws QueueIsNotStarted {
        queueManager.enqueue(aircraft);
        return aircraft;
    }

    @GetMapping(path = "/aircraft/remove",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Aircraft dequeueAircraft() throws QueueIsNotStarted, AircraftNotFoundException {
        return queueManager
                .dequeue()
                .orElseThrow(() -> new AircraftNotFoundException());
    }
}
