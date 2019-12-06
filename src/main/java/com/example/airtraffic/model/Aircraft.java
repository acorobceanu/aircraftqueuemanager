package com.example.airtraffic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Aircraft {
    private int id;
    private AircraftType type;
    private AircraftSize size;
    private long queueTimestamp;
}
