package com.ikhramchenkov.staffmanagement.exceptions;

import javax.persistence.EntityNotFoundException;

public class DepartmentNotFoundException extends EntityNotFoundException {
    public DepartmentNotFoundException(Long id) {
        super(String.format("Department with id %1$s is not found", id));
    }
}
