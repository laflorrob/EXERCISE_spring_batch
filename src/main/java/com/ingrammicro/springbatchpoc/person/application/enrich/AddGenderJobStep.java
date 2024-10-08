package com.ingrammicro.springbatchpoc.person.application.enrich;

import com.ingrammicro.springbatchpoc.person.domain.Person;
import com.ingrammicro.springbatchpoc.person.port.out.PersonPersistenceService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@AllArgsConstructor
public class AddGenderJobStep implements Tasklet {

    private final PersonPersistenceService personPersistenceService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        long personId = Objects.requireNonNull(getJobParameters(chunkContext).getLong("PERSON_ID"));
        Person person = personPersistenceService.getById(personId);

        log.debug("{} is adding gender to person with id {}",
                Thread.currentThread().getName(), person.getId());

        Thread.sleep(ThreadLocalRandom.current().nextLong(10000, 30000));

        person.setGender("some gender...");
        personPersistenceService.save(person);

        log.debug("Person with id {} has been updated with gender by {}",
                person.getId(), Thread.currentThread().getName());

        return RepeatStatus.FINISHED;

    }

    private JobParameters getJobParameters(ChunkContext chunkContext) {
        return chunkContext.getStepContext().getStepExecution().getJobParameters();
    }

}
