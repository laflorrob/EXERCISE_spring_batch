package com.ingrammicro.springbatchpoc.person.application.update;

import com.ingrammicro.springbatchpoc.person.domain.Person;
import com.ingrammicro.springbatchpoc.person.port.in.UpdatePersonService;
import com.ingrammicro.springbatchpoc.person.port.out.PersonPersistenceService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdatePerson implements UpdatePersonService {

    private final PersonPersistenceService personPersistenceService;

    @Override
    public void updateStatus(int personId, String status) {
        Person person = personPersistenceService.getById(personId);
        person.setStatus(status);
        personPersistenceService.save(person);
    }

}
