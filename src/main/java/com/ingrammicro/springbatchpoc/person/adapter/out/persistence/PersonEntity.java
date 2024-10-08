package com.ingrammicro.springbatchpoc.person.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "PERSON")
@Data
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_generator")
    @SequenceGenerator(name = "person_generator", sequenceName = "person_seq", allocationSize = 1)
    private long id;

    private String name;

    @Column(name = "LAST_NAME")
    private String lastName;

    private int age;

    private String country;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    private String address;

    private String gender;

    private String status;

    @Column(name = "JOB_EXECUTION_ID")
    private long jobExecutionId;

}
