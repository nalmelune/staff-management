package com.ikhramchenkov.staffmanagement.exceptions;

import javax.persistence.EntityNotFoundException;

public class WorkerNotFoundException extends EntityNotFoundException {
    public WorkerNotFoundException(Long id) {
        super(String.format("Worker with id %1$s is not found", id));
    }
}
