package com.ingrammicro.springbatchpoc.person.config;

import com.ingrammicro.springbatchpoc.person.application.create.CreatePerson;
import com.ingrammicro.springbatchpoc.person.application.update.UpdatePerson;
import com.ingrammicro.springbatchpoc.person.port.in.CreatePersonService;
import com.ingrammicro.springbatchpoc.person.application.get.GetNextPersonToEnrich;
import com.ingrammicro.springbatchpoc.person.port.in.GetNextPersonToEnrichService;
import com.ingrammicro.springbatchpoc.person.application.enrich.EnrichPersonJobScheduler;
import com.ingrammicro.springbatchpoc.person.port.in.UpdatePersonService;
import com.ingrammicro.springbatchpoc.person.port.out.PersonPersistenceService;
import com.ingrammicro.springbatchpoc.person.adapter.out.persistence.PersonPersistenceAdapter;
import com.ingrammicro.springbatchpoc.person.adapter.out.persistence.PersonRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public PersonPersistenceService persistenceService(PersonRepository personRepository) {
        return new PersonPersistenceAdapter(personRepository);
    }

    @Bean
    public CreatePersonService createPersonService(PersonPersistenceService personPersistenceService) {
        return new CreatePerson(personPersistenceService);
    }

    @Bean
    public GetNextPersonToEnrichService getNextPersonToProcessService(PersonPersistenceService personPersistenceService) {
        return new GetNextPersonToEnrich(personPersistenceService);
    }

    @Bean
    public UpdatePersonService updatePersonService(PersonPersistenceService personPersistenceService) {
        return new UpdatePerson(personPersistenceService);
    }

    @Bean
    public EnrichPersonJobScheduler enrichPersonJobLauncher(GetNextPersonToEnrichService getNextPersonToProcess,
                                                            Job enrichPersonJob, JobLauncher jobLauncher) {
        return new EnrichPersonJobScheduler(getNextPersonToProcess, enrichPersonJob, jobLauncher);
    }

}
