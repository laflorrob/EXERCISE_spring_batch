package com.ingrammicro.springbatchpoc.person.application.create;

import com.ingrammicro.springbatchpoc.person.domain.EnrichingStatus;
import com.ingrammicro.springbatchpoc.person.port.in.CreatePersonService;
import com.ingrammicro.springbatchpoc.person.port.out.PersonPersistenceService;
import com.ingrammicro.springbatchpoc.person.domain.Person;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class CreatePerson implements CreatePersonService {

    private final PersonPersistenceService personPersistenceService;

    @Override
    public void create(Person person) {
        person.setCreationDate(LocalDateTime.now());
        person.setEnrichingStatus(EnrichingStatus.PENDING);
        personPersistenceService.save(person);
    }
}
