package com.ingrammicro.springbatchpoc.person.application.enrich;

import com.ingrammicro.springbatchpoc.person.domain.Person;
import com.ingrammicro.springbatchpoc.person.port.in.GetPersonService;
import com.ingrammicro.springbatchpoc.person.port.out.PersonPersistenceService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@AllArgsConstructor
public class AddAddressJobStep implements Tasklet {

    private final PersonPersistenceService personPersistenceService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        long personId = Objects.requireNonNull(getJobParameters(chunkContext).getLong("PERSON_ID"));
        getExecutionContext(chunkContext).putLong("PERSON_ID", personId);

        Person person = personPersistenceService.getById((int) personId);

        log.debug("{} has retrieved person from database {}",
                Thread.currentThread().getName(), person.toString());

        person.setJobExecutionAttempts(person.getJobExecutionAttempts() + 1);
        personPersistenceService.save(person);

        try {
            Thread.sleep(ThreadLocalRandom.current().nextLong(10000, 30000));
            if(personId == 2) {
                throw new RuntimeException("Some unexpected error has occurred");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        person.setAddress("some address...");
        personPersistenceService.save(person);

        log.debug("Person with id {} has been updated with address by {}",
                person.getId(), Thread.currentThread().getName());

        return RepeatStatus.FINISHED;

    }

    private ExecutionContext getExecutionContext(ChunkContext chunkContext) {
        return chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
    }

    private JobParameters getJobParameters(ChunkContext chunkContext) {
        return chunkContext.getStepContext().getStepExecution().getJobParameters();
    }

}
