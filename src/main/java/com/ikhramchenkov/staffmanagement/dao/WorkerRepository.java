package com.ikhramchenkov.staffmanagement.dao;

import com.ikhramchenkov.staffmanagement.persistance.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkerRepository extends CrudRepository<Worker, Long> {
    Iterable<Worker> findAllByDepartment_Id(long departmentId);
}
