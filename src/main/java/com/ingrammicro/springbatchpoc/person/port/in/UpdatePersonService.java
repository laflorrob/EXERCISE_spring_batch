package com.ingrammicro.springbatchpoc.person.port.in;

import com.ingrammicro.springbatchpoc.person.domain.Person;

public interface UpdatePersonService {

    void updateStatus(int personId, String status);
}
