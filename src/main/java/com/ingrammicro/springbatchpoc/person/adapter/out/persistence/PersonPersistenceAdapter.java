package com.ingrammicro.springbatchpoc.person.adapter.out.persistence;

import com.ingrammicro.springbatchpoc.person.domain.EnrichingStatus;
import com.ingrammicro.springbatchpoc.person.port.out.PersonPersistenceService;
import com.ingrammicro.springbatchpoc.person.domain.Person;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PersonPersistenceAdapter implements PersonPersistenceService {

    private final PersonRepository personRepository;

    @Override
    public void save(Person person) {
        personRepository.saveAndFlush(toEntity(person));
    }

    @Override
    public Person getNextToProcess() {
        Person person = new Person();
        if(personRepository.getOldestNotProcessed().isPresent()) {
            person = toDomain(personRepository.getOldestNotProcessed().get());
        }
        return person;
    }

    @Override
    public Person getById(long id) {
        return toDomain(personRepository.findById((int) id).orElse(new PersonEntity()));
    }

    @Override
    public void updateStatus(long id, String status) {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(id);
        personEntity.setStatus(status);
        personRepository.save(personEntity);
    }

    private PersonEntity toEntity(Person person) {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(person.getId());
        personEntity.setName(person.getName());
        personEntity.setLastName(person.getLastName());
        personEntity.setAge(person.getAge());
        personEntity.setCreationDate(person.getCreationDate());
        personEntity.setAddress(person.getAddress());
        personEntity.setStatus(person.getEnrichingStatus().toString());
        personEntity.setGender(person.getGender());
        personEntity.setCountry(person.getCountry());
        personEntity.setJobExecutionId(person.getJobExecutionId());
        return personEntity;
    }

    private Person toDomain(PersonEntity entity) {
        Person person = new Person();
        person.setId(entity.getId());
        person.setName(entity.getName());
        person.setLastName(entity.getLastName());
        person.setAge(entity.getAge());
        person.setCreationDate(entity.getCreationDate());
        person.setAddress(entity.getAddress());
        person.setEnrichingStatus(EnrichingStatus.valueOf(entity.getStatus()));
        person.setGender(entity.getGender());
        person.setCountry(entity.getCountry());
        person.setJobExecutionId(entity.getJobExecutionId());
        return person;
    }

}
