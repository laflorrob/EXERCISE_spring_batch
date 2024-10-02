package com.ingrammicro.springbatchpoc.person.application.update;

import com.ingrammicro.springbatchpoc.person.domain.EnrichingStatus;
import com.ingrammicro.springbatchpoc.person.domain.Person;
import com.ingrammicro.springbatchpoc.person.port.in.UpdatePersonService;
import com.ingrammicro.springbatchpoc.person.port.out.PersonPersistenceService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdatePerson implements UpdatePersonService {

    private final PersonPersistenceService personPersistenceService;

    @Override
    public void updateStatus(long personId, EnrichingStatus status) {
        Person person = personPersistenceService.getById(personId);
        person.setEnrichingStatus(status);
        personPersistenceService.save(person);
    }

}
