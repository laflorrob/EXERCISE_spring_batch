package com.ingrammicro.springbatchpoc.person.application.enrich;

import com.ingrammicro.springbatchpoc.person.domain.Person;
import com.ingrammicro.springbatchpoc.person.port.in.GetNextPersonToEnrichService;
import com.ingrammicro.springbatchpoc.person.port.in.UpdatePersonService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

@Log4j2
@AllArgsConstructor
public class EnrichPersonJobScheduler {

    private final GetNextPersonToEnrichService getNextPersonToProcess;
    private final Job enrichPersonJob;
    private final JobLauncher jobLauncher;

    @Scheduled(fixedRate = 1000)
    @Async("enrichPersonThreadPool")
    public void doTask() {

        Person person = getNextPersonToProcess();

        if(!person.isEmpty()) {
            launchJob(person);
        }

    }

    private Person getNextPersonToProcess() {

        Person person = new Person();

        try {

            person = getNextPersonToProcess.get();

        } catch(Exception e) {

            log.error("{} has been not allowed by exception {} to get some person to enrich",
                    Thread.currentThread().getName(), e.getClass().getName());

        }

        return person;

    }

    private void launchJob(Person person) {

        try {

            JobParameters parameters = new JobParametersBuilder()
                    .addLong("PERSON_ID", person.getId())
                    .toJobParameters();

            jobLauncher.run(enrichPersonJob, parameters);

        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException | StartLimitExceededException e) {

            log.error("Not possible to launch Job because {}", e.getMessage());
        }

    }

}
