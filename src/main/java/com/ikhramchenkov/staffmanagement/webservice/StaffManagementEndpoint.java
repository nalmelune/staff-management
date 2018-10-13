package com.ikhramchenkov.staffmanagement.webservice;

import com.ikhramchenkov.staffmanagement.dao.DepartmentRepository;
import com.ikhramchenkov.staffmanagement.dao.WorkerRepository;
import com.ikhramchenkov.staffmanagement.exceptions.DepartmentNotFoundException;
import com.ikhramchenkov.staffmanagement.exceptions.WorkerNotFoundException;
import com.ikhramchenkov.staffmanagement.exceptions.WorkerUnassignedAlready;
import com.ikhramchenkov.staffmanagement.jaxb.*;
import com.ikhramchenkov.staffmanagement.persistance.Department;
import com.ikhramchenkov.staffmanagement.persistance.Worker;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Optional;

@Endpoint
public class StaffManagementEndpoint {

    private Logger logger = LoggerFactory.getLogger(StaffManagementEndpoint.class);

    private ObjectFactory objectFactory = new ObjectFactory();

    private final WorkerRepository workerRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public StaffManagementEndpoint(WorkerRepository workerRepository, DepartmentRepository departmentRepository) {
        this.workerRepository = workerRepository;
        this.departmentRepository = departmentRepository;
    }

    @PayloadRoot(namespace = "http://ikhramchenkov.com/staffmanagement/jaxb/", localPart = "GetStaffRequest")
    @ResponsePayload
    public GetStaffResponse getStaff(@RequestPayload GetStaffRequest request) {
        logger.info("GetStaffRequest = {}", ToStringBuilder.reflectionToString(request));

        Optional<Long> departmentId = Optional.ofNullable(request.getDepartmentId());
        Iterable<Worker> foundEntities;
        if (departmentId.isPresent() && departmentId.get() != 0) {
            foundEntities = workerRepository.findAllByDepartment_Id(departmentId.get());
        } else {
            foundEntities = workerRepository.findAll();
        }

        GetStaffResponse response = objectFactory.createGetStaffResponse();

        for (Worker foundEntity : foundEntities) {
            WorkerWithDepartmentEntity workerWithDepartmentEntity = objectFactory.createWorkerWithDepartmentEntity();
            BeanUtils.copyProperties(foundEntity, workerWithDepartmentEntity);
            if (foundEntity.getDepartment() != null) {
                workerWithDepartmentEntity.setDepartment(new DepartmentEntity());
                BeanUtils.copyProperties(foundEntity.getDepartment(), workerWithDepartmentEntity.getDepartment(), "workers");
            }
            response.getWorker().add(workerWithDepartmentEntity);
        }

        logger.info("GetStaffResponse = {}", ToStringBuilder.reflectionToString(response));
        return response;
    }

    @PayloadRoot(namespace = "http://ikhramchenkov.com/staffmanagement/jaxb/", localPart = "AddWorkerRequest")
    @ResponsePayload
    public AddWorkerResponse addWorker(@RequestPayload AddWorkerRequest request) {
        logger.info("AddWorkerRequest = {}", ToStringBuilder.reflectionToString(request));

        Worker worker = new Worker();

        BeanUtils.copyProperties(request.getWorker(), worker);

        Optional.ofNullable(request.getWorker().getDepartmentId()).ifPresent(departmentId -> {
            Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new DepartmentNotFoundException(departmentId));
            worker.setDepartment(department);
        });

        Worker saved = workerRepository.save(worker);

        AddWorkerResponse response = objectFactory.createAddWorkerResponse();
        response.setId(saved.getId());

        logger.info("AddWorkerResponse = {}", ToStringBuilder.reflectionToString(response));

        return response;
    }

    @PayloadRoot(namespace = "http://ikhramchenkov.com/staffmanagement/jaxb/", localPart = "EditWorkerRequest")
    @ResponsePayload
    public EditWorkerResponse editWorkerResponse(@RequestPayload EditWorkerRequest request) {
        logger.info("EditWorkerRequest = {}", ToStringBuilder.reflectionToString(request));

        Worker workerToUpdate = workerRepository.findById(request.getWorker().getId()).orElseThrow(() -> new WorkerNotFoundException(request.getWorker().getId()));

        BeanUtils.copyProperties(request.getWorker(), workerToUpdate);

        Optional.ofNullable(request.getWorker().getDepartmentId()).ifPresent(departmentId -> {
            Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new DepartmentNotFoundException(departmentId));
            workerToUpdate.setDepartment(department);
        });

        workerRepository.save(workerToUpdate);

        EditWorkerResponse response = objectFactory.createEditWorkerResponse();

        logger.info("EditWorkerResponse = {}", ToStringBuilder.reflectionToString(response));

        return response;
    }

    @PayloadRoot(namespace = "http://ikhramchenkov.com/staffmanagement/jaxb/", localPart = "DeleteWorkerRequest")
    @ResponsePayload
    public DeleteWorkerResponse deleteWorkerRequest(@RequestPayload DeleteWorkerRequest request) {
        logger.info("DeleteWorkerRequest = {}", ToStringBuilder.reflectionToString(request));

        workerRepository.deleteById(request.getId());

        DeleteWorkerResponse response = objectFactory.createDeleteWorkerResponse();

        logger.info("DeleteWorkerResponse = {}", ToStringBuilder.reflectionToString(response));

        return response;
    }

    @PayloadRoot(namespace = "http://ikhramchenkov.com/staffmanagement/jaxb/", localPart = "AssignWorkerRequest")
    @ResponsePayload
    public AssignWorkerResponse assignWorker(@RequestPayload AssignWorkerRequest request) {
        logger.info("AssignWorkerRequest = {}", ToStringBuilder.reflectionToString(request));

        AssignWorkerResponse response = objectFactory.createAssignWorkerResponse();

        Department department = departmentRepository.findById(request.getDepartmentId()).orElseThrow(() -> new DepartmentNotFoundException(request.getDepartmentId()));
        Worker worker = workerRepository.findById(request.getWorkerId()).orElseThrow(() -> new WorkerNotFoundException(request.getWorkerId()));

        worker.setDepartment(department);
        workerRepository.save(worker);

        logger.info("AssignWorkerResponse = {}", ToStringBuilder.reflectionToString(response));

        return response;
    }

    @PayloadRoot(namespace = "http://ikhramchenkov.com/staffmanagement/jaxb/", localPart = "UnassignWorkerRequest")
    @ResponsePayload
    public UnassignWorkerResponse unassignWorker(@RequestPayload UnassignWorkerRequest request) {
        logger.info("UnassignWorkerRequest = {}", ToStringBuilder.reflectionToString(request));

        Worker worker = workerRepository.findById(request.getWorkerId()).orElseThrow(() -> new WorkerNotFoundException(request.getWorkerId()));

        if (worker.getDepartment() == null) {
            throw new WorkerUnassignedAlready();
        }

        worker.setDepartment(null);
        workerRepository.save(worker);

        UnassignWorkerResponse response = objectFactory.createUnassignWorkerResponse();
        logger.info("UnassignWorkerResponse = {}", ToStringBuilder.reflectionToString(response));

        return response;
    }
}