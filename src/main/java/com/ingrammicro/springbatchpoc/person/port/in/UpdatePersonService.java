package com.ingrammicro.springbatchpoc.person.port.in;

import com.ingrammicro.springbatchpoc.person.domain.EnrichingStatus;
import com.ingrammicro.springbatchpoc.person.domain.Person;

public interface UpdatePersonService {

    void updateStatus(long personId, EnrichingStatus status);
}
