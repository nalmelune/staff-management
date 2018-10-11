package com.ikhramchenkov.staffmanagement.dao;

import com.ikhramchenkov.staffmanagement.persistance.Department;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
    @Override
    @CachePut("departmentCache")
    <S extends Department> S save(S s);

    @Override
    @CachePut("departmentCache")
    <S extends Department> Iterable<S> saveAll(Iterable<S> iterable);

    @Override
    @Cacheable("departmentCache")
    Optional<Department> findById(Long aLong);

    @Override
    @Cacheable("departmentCache")
    boolean existsById(Long aLong);

    @Override
    @Cacheable("departmentCache")
    Iterable<Department> findAll();

    @Override
    @Cacheable("departmentCache")
    Iterable<Department> findAllById(Iterable<Long> iterable);

    @Override
    @Cacheable("departmentCache")
    long count();

    @Override
    @CacheEvict("departmentCache")
    void deleteById(Long aLong);

    @Override
    @CacheEvict("departmentCache")
    void delete(Department department);

    @Override
    @CacheEvict("departmentCache")
    void deleteAll(Iterable<? extends Department> iterable);

    @Override
    @CacheEvict("departmentCache")
    void deleteAll();
}
