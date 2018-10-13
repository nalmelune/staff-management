package com.ikhramchenkov.staffmanagement.persistance;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DEPARTMENTS")
public class Department {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "departments_seq", sequenceName = "DEPARTMENTS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departments_seq")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(targetEntity = Worker.class, cascade = CascadeType.DETACH)
    @JoinColumn(name = "WORKERS")
    private Set<Worker> workers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(Set<Worker> workers) {
        this.workers = workers;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
