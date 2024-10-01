package com.ingrammicro.springbatchpoc.person.port.out;

import com.ingrammicro.springbatchpoc.person.domain.Person;

public interface PersonPersistenceService {

    void save(Person person);

    Person getNextToProcess();

    Person getById(int id);

    void updateExecutionAttempts(int id, int executionAttempts);

    void updateStatus(int id, String status);

}
