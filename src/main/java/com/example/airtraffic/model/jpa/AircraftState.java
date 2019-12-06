package com.example.airtraffic.model.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "aircraft_state")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AircraftState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(columnDefinition = "text")
    private String type;

    @NotBlank
    @Column(columnDefinition = "text")
    private String size;

    @NotNull
    @Column(name = "queued_timestamp", columnDefinition = "bigint")
    private Long queuedTimestamp;

    @Column(name = "dequeued_timestamp", columnDefinition = "bigint")
    private Long dequeuedTimestamp;
}
