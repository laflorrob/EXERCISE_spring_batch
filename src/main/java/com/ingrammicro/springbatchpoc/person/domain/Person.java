package com.ingrammicro.springbatchpoc.person.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Person {

    private long id;
    private String name;
    private String lastName;
    private int age;
    private String country;
    private LocalDateTime creationDate;
    private String address;
    private EnrichingStatus enrichingStatus;
    private String gender;
    private long jobExecutionId;
    private int jobExecutionAttempts;


    public boolean isEmpty() {
        return name == null || name.isEmpty();
    }

}
