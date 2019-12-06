package com.example.airtraffic.service.impl;

import com.example.airtraffic.exception.QueueIsNotStarted;
import com.example.airtraffic.model.Aircraft;
import com.example.airtraffic.model.AircraftSize;
import com.example.airtraffic.model.jpa.AircraftQueue;
import com.example.airtraffic.model.jpa.AircraftState;
import com.example.airtraffic.repository.AircraftQueueRepository;
import com.example.airtraffic.repository.AircraftStateRepository;
import com.example.airtraffic.service.AircraftInQueueComparator;
import com.example.airtraffic.service.QueueManager;
import com.example.airtraffic.util.ConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

/**
 * Priority queue implementation of {@link QueueManager}
 */
@Component
@Slf4j
public class PriorityQueueManager implements QueueManager {

    // TODO current design is for single queue
    public static final Integer QUEUE_ID = 1;

    private final AircraftStateRepository aircraftStateRepository;
    private final AircraftQueueRepository aircraftQueueRepository;

    // unbounded queue satisfies the requirement of unlimited number of aircrafts in the queue
    private PriorityBlockingQueue<AircraftState> queue;

    private boolean isStarted = false;

    @Autowired
    public PriorityQueueManager(
            @NotNull AircraftInQueueComparator comparator,
            @NotNull AircraftQueueRepository aircraftQueueRepository,
            @NotNull AircraftStateRepository aircraftStateRepository) {
        this.aircraftStateRepository = aircraftStateRepository;
        this.aircraftQueueRepository = aircraftQueueRepository;
        queue = new PriorityBlockingQueue(1, comparator);
    }

    @PostConstruct
    public void setUp() {
        log.info("PriorityQueueManager created {}", this);
    }

    /**
     * Starts aircraft queue manager and creates a record in queue state table.
     * If queue manager already started, method lags a warning and returns.
     */
    @Override
    public synchronized void start() {
        if (isStarted()) {
            log.warn("Aircraft queue manager is already started");
            return;
        }
        // switch the flag
        this.isStarted = true;

        List<AircraftState> allQueued = aircraftStateRepository.findAllQueued();
        log.info("On startup found {} queued aircrafts in the repository, loading into the queue", allQueued.size());
        allQueued.stream()
                .forEach(this::insertAircraftIntoQueue);

        log.info("Aircraft queue manager started");
    }

    /**
     * Puts an aircraft into the priority queue.
     *
     * @param aircraft {@link Aircraft} object to be added to the queue
     * @throws QueueIsNotStarted If method is called while queue manager is not started
     */
    @Override
    public Aircraft enqueue(@NotNull Aircraft aircraft) throws QueueIsNotStarted {
        if (!isStarted()) {
            throw new QueueIsNotStarted();
        }
        log.debug("Received request to insert aircraft {} into the queue", aircraft);

        insertAircraftIntoQueue(
                persistIntoRepository(
                        aircraft));
        logQueue();
        return aircraft;
    }

    /**
     * Removes an aircraft from the queue according to the priority and returns it.
     *
     * @return {@link Optional} of removed aircraft or {@link Optional#empty()} if queue was empty.
     * @throws QueueIsNotStarted If method is called while queue manager is not started
     */

    @Override
    public Optional<Aircraft> dequeue() throws QueueIsNotStarted {
        if (!isStarted()) {
            throw new QueueIsNotStarted();
        }
        log.debug("Received request to remove aircraft from the queue");

        AircraftState aircraftState = queue.poll();
        if (aircraftState != null) {
            aircraftState.setDequeuedTimestamp(System.currentTimeMillis());
            aircraftStateRepository.save(aircraftState);

            // keep queue size up to date in the repository
            adjustQueueSize(-1);
        }

        logQueue();
        return Optional.ofNullable(ConversionUtils.fromEntity(aircraftState));
    }

    @Override
    public List<Aircraft> listQueued() {
        return aircraftStateRepository
                .findAllQueued().stream()
                .map(ConversionUtils::fromEntity)
                .collect(Collectors.toList());
    }

    private synchronized void adjustQueueSize(int count) {
        AircraftQueue aircraftQueue = aircraftQueueRepository
                .findById(QUEUE_ID)
                .orElseGet(() ->
                        AircraftQueue.builder()
                                .id(QUEUE_ID)
                                .queueSize(0)
                                .started(true)
                                .build());

        aircraftQueue.setQueueSize(
                aircraftQueue.getQueueSize() + count);
        aircraftQueueRepository.save(aircraftQueue);
        log.info("Queue size updated to {} in the repository", aircraftQueue.getQueueSize());
    }

    private synchronized boolean isStarted() {
        return this.isStarted;
    }

    private AircraftState persistIntoRepository(Aircraft aircraft) {
        AircraftState aircraftState = ConversionUtils.toEntity(aircraft);
        aircraftState.setQueuedTimestamp(System.currentTimeMillis());
        aircraftStateRepository.save(aircraftState);
        log.info("Persisted aircraft {} into the repository", aircraftState);

        // keep queue size up to date in the repository
        adjustQueueSize(1);

        return aircraftState;
    }

    private void insertAircraftIntoQueue(AircraftState aircraftState) {
        queue.offer(aircraftState);
        log.info("Placed aircraft {} into the queue", aircraftState);
    }

    private void logQueue() {
        StringBuffer sb = new StringBuffer("Current aircraft queue (by priority): \n");
        queue.iterator().forEachRemaining(a -> sb.append(String.format("\t%s\n", a)));
        log.debug(sb.toString());
    }

}
