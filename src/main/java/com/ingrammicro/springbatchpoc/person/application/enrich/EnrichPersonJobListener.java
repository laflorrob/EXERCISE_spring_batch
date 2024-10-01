package com.ingrammicro.springbatchpoc.person.application.enrich;

import com.ingrammicro.springbatchpoc.person.domain.Person;
import com.ingrammicro.springbatchpoc.person.port.in.UpdatePersonService;
import com.ingrammicro.springbatchpoc.person.port.out.PersonPersistenceService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

import java.util.Objects;

@Log4j2
@AllArgsConstructor
public class EnrichPersonJobListener implements JobExecutionListener {

    private final UpdatePersonService updatePersonService;

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        log.info("{} is executing enrich person job for person with id {}",
                Thread.currentThread().getName(), jobExecution.getJobParameters().getLong("PERSON_ID"));
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {

        long personId = Objects.requireNonNull(jobExecution.getJobParameters().getLong("PERSON_ID"));

        if (jobExecution.getStatus() == BatchStatus.COMPLETED ) {

            log.info("{} has finished execution of enriching person job for person with id {}",
                    Thread.currentThread().getName(), jobExecution.getJobParameters().getLong("PERSON_ID"));

            updatePersonService.updateStatus((int) personId, "PROCESSED");

        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {

            log.info("Some error has occurred while {} was executing enriching person job " +
                            "for person with id {}", Thread.currentThread().getName(), personId);

            updatePersonService.updateStatus((int) personId, "PENDING");
        }
    }

}
