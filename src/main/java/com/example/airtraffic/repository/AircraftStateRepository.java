package com.example.airtraffic.repository;

import com.example.airtraffic.model.jpa.AircraftState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AircraftStateRepository extends JpaRepository<AircraftState, Integer> {
    @Query("FROM AircraftState WHERE dequeuedTimestamp IS NULL ORDER BY queuedTimestamp ASC")
    List<AircraftState> findAllQueued();
}
