package com.ingrammicro.springbatchpoc.person.application.enrich;

import com.ingrammicro.springbatchpoc.person.domain.Person;
import com.ingrammicro.springbatchpoc.person.port.out.PersonPersistenceService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@AllArgsConstructor
public class LinkPersonToJobStep implements Tasklet {

    private final PersonPersistenceService personPersistenceService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        long personId = Objects.requireNonNull(getJobParameters(chunkContext).getLong("PERSON_ID"));
        Person person = personPersistenceService.getById(personId);

        log.debug("{} has retrieved person from database {}",
                Thread.currentThread().getName(), person.toString());

        person.setJobExecutionId(getJobExecutionId(chunkContext));
        personPersistenceService.save(person);

        log.debug("Person with id {} has been linked to job id {} by {}",
                person.getId(), getJobExecutionId(chunkContext), Thread.currentThread().getName());

        return RepeatStatus.FINISHED;

    }

    private JobParameters getJobParameters(ChunkContext chunkContext) {
        return getStepExecution(chunkContext).getJobParameters();
    }

    private Long getJobExecutionId(ChunkContext chunkContext) {
        return getStepExecution(chunkContext).getJobExecutionId();
    }

    private StepExecution getStepExecution(ChunkContext chunkContext) {
        return chunkContext.getStepContext().getStepExecution();
    }

}
