package com.ingrammicro.springbatchpoc.person.port.in;

import com.ingrammicro.springbatchpoc.person.domain.Person;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface GetNextPersonToEnrichService {

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            isolation = Isolation.SERIALIZABLE)
    Person get();

}
