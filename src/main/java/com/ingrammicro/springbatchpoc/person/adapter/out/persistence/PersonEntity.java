package com.ingrammicro.springbatchpoc.person.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "PERSON")
@Data
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

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
    private int jobExecutionId;

    @Column(name = "JOB_EXECUTION_ATTEMPTS")
    private int jobExecutionAttempts;

}
