package com.example.pressync;

import org.springframework.http.ResponseEntity;

/**
 * CQRS command interface for write operations.
 * Each command encapsulates a single write operation (create, update, delete)
 * and returns a response indicating the result.
 *
 * @param <E> the request entity type (input DTO or primitive)
 * @param <T> the response body type (output DTO or String)
 */
public interface Command <E,T>{
    ResponseEntity<T> execute(E entity);
}
