package com.geobudget.geobudget.exception;

public class EntityWasRemovedException extends RuntimeException {
    public EntityWasRemovedException(String message) {
        super(message);
    }
}
