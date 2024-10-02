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

        Person person = getNextPersonToProcess.get();

        if(!person.isEmpty()) {

            try {

                JobParameters parameters = new JobParametersBuilder()
                        .addLong("PERSON_ID", person.getId())
                        .toJobParameters();

                jobLauncher.run(enrichPersonJob, parameters);

            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                     JobParametersInvalidException | StartLimitExceededException e) {

                throw new RuntimeException("Some error prevents launching enrich person job", e);

            }

        }

    }

}
