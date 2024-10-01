package com.ingrammicro.springbatchpoc.person.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Person {

    private int id;
    private String name;
    private String lastName;
    private int age;
    private String country;
    private LocalDateTime creationDate;
    private String address;
    private String status;
    private String gender;
    private int jobExecutionId;
    private int jobExecutionAttempts;


    public boolean isEmpty() {
        return name == null || name.isEmpty();
    }

}
