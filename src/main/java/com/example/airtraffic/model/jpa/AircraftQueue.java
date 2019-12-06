package com.example.airtraffic.model.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "aircraft_queue")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AircraftQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(columnDefinition = "boolean")
    private boolean started;

    @NotNull
    @Column(name = "queue_size", columnDefinition = "integer")
    private int queueSize;
}
