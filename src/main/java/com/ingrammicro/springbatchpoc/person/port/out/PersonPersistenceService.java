package com.ingrammicro.springbatchpoc.person.port.out;

import com.ingrammicro.springbatchpoc.person.domain.Person;

public interface PersonPersistenceService {

    void save(Person person);

    Person getNextToProcess();

    Person getById(long id);

    void updateStatus(long id, String status);

}
