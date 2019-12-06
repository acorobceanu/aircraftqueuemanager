package com.example.airtraffic.repository;

import com.example.airtraffic.model.jpa.AircraftQueue;
import com.example.airtraffic.model.jpa.AircraftState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftQueueRepository extends JpaRepository<AircraftQueue, Integer> {
}
