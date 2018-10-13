package com.ikhramchenkov.staffmanagement.dao;

import com.ikhramchenkov.staffmanagement.persistance.Worker;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface WorkerRepository extends CrudRepository<Worker, Long> {
    @EntityGraph(value = "withDepartment", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<Worker> findAllByDepartment_Id(long departmentId);

    @Override
    @EntityGraph(value = "withDepartment", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<Worker> findAll();
}
