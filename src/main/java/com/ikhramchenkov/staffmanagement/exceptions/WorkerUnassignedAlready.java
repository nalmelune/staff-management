package com.ikhramchenkov.staffmanagement.exceptions;

public class WorkerUnassignedAlready extends RuntimeException {
    public WorkerUnassignedAlready() {
        super("Worker is unassigned already");
    }
}
