package com.ingrammicro.springbatchpoc.person.port.in;

import com.ingrammicro.springbatchpoc.person.domain.Person;

public interface CreatePersonService {

    void create(Person person);
}
