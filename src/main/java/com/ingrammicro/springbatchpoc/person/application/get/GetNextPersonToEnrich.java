package com.ingrammicro.springbatchpoc.person.application.get;

import com.ingrammicro.springbatchpoc.person.domain.EnrichingStatus;
import com.ingrammicro.springbatchpoc.person.port.in.GetNextPersonToEnrichService;
import com.ingrammicro.springbatchpoc.person.port.out.PersonPersistenceService;
import com.ingrammicro.springbatchpoc.person.domain.Person;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@AllArgsConstructor
public class GetNextPersonToEnrich implements GetNextPersonToEnrichService {

    private final PersonPersistenceService personPersistenceService;

    @Override
    public synchronized Person get() throws InterruptedException {

        log.info("{} is getting next person to enrich", Thread.currentThread().getName());

        Person person = personPersistenceService.getNextToProcess();

        Thread.sleep(ThreadLocalRandom.current().nextLong(5000, 10000));

        if (person.isEmpty()) {
            log.info("{} has not found any person to enrich", Thread.currentThread().getName());
            return person;
        }

        person.setEnrichingStatus(EnrichingStatus.PROCESSING);
        personPersistenceService.save(person);

        log.info("{} has found a person to enrich so enrichPersonJob will be launched with person id {} " +
                "as a parameter",  Thread.currentThread().getName(), person.getId());

        return person;

    }
}
