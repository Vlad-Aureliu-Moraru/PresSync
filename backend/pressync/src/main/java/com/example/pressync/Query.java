package com.example.pressync;

import org.springframework.http.ResponseEntity;

/**
 * CQRS query interface for read operations.
 * Each query encapsulates a single read operation (get by id, get all, stats)
 * and returns a response containing the requested data.
 *
 * @param <I> the input parameter type (ID, DTO, or Void)
 * @param <O> the output response body type
 */
public interface Query <I, O> {
    ResponseEntity<O> execute(I input);
}
