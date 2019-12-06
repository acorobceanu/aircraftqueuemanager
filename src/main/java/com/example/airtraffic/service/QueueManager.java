package com.example.airtraffic.service;

import com.example.airtraffic.exception.QueueIsNotStarted;
import com.example.airtraffic.model.Aircraft;

import java.util.List;
import java.util.Optional;

/**
 * Manager interface to perform requests on the aircraft queue.
 */
public interface QueueManager {
    /**
     * Starts queue manger if not already started. If queue manager already started this method does nothing.
     */
    void start();

    /**
     * Puts aircraft into the queue.
     *
     * @param aircraft {@link Aircraft} object to be added to the queue
     * @return Aircraft inserted into the queue
     * @throws QueueIsNotStarted If queue manager is not started
     */
    Aircraft enqueue(Aircraft aircraft) throws QueueIsNotStarted;

    /**
     * Pulls aircraft from the queue.
     *
     * @return {@link Optional} of {@link Aircraft} object if queue is not empty, {@link Optional#empty()} otherwise
     * @throws QueueIsNotStarted If queue manager is not started
     */
    Optional<Aircraft> dequeue() throws QueueIsNotStarted;

    /**
     * Lists all aircrafts currently in the queue
     *
     * @return List of {@link Aircraft} objects
     */
    List<Aircraft> listQueued();
}
