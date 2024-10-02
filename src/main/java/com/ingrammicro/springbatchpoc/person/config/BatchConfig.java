package com.ingrammicro.springbatchpoc.person.config;


import com.ingrammicro.springbatchpoc.person.application.enrich.*;
import com.ingrammicro.springbatchpoc.person.port.in.GetPersonService;
import com.ingrammicro.springbatchpoc.person.port.in.UpdatePersonService;
import com.ingrammicro.springbatchpoc.person.port.out.PersonPersistenceService;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AllArgsConstructor
public class BatchConfig {

    @Bean
    public Tasklet linkPersonToJobTasklet(PersonPersistenceService personPersistenceService) {
        return new LinkPersonToJobStep(personPersistenceService);
    }

    @Bean
    public Tasklet addAddressTasklet(PersonPersistenceService personPersistenceService) {
        return new AddAddressJobStep(personPersistenceService);
    }

    @Bean
    public Tasklet addGenderTasklet(PersonPersistenceService personPersistenceService) {
        return new AddGenderJobStep(personPersistenceService);
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[] { "PERSON_ID" });
        return listener;
    }

    @Bean
    public Step linkPersonToJobStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                               Tasklet linkPersonToJobTasklet) {
        return new StepBuilder("linkPersonToJobStep", jobRepository)
                .tasklet(linkPersonToJobTasklet, transactionManager)
                .startLimit(3)
                .build();
    }

    @Bean
    public Step addAddressStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                  Tasklet addAddressTasklet, ExecutionContextPromotionListener promotionListener) {
        return new StepBuilder("addAddressStep", jobRepository)
                .tasklet(addAddressTasklet, transactionManager)
                .startLimit(3)
                .build();
    }

    @Bean
    public Step addGenderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                  Tasklet addGenderTasklet) {
        return new StepBuilder("addGenderStep", jobRepository)
                .tasklet(addGenderTasklet, transactionManager)
                .build();
    }

    @Bean
    public JobExecutionListener enrichPersonJobListener(UpdatePersonService updatePersonService) {
        return new EnrichPersonJobListener(updatePersonService);
    }

    @Bean
    public Job enrichPersonJob(JobRepository jobRepository, Step linkPersonToJobStep, Step addAddressStep,
                               Step addGenderStep, JobExecutionListener enrichPersonJobListener) {
        return new JobBuilder("enrichPersonJob", jobRepository)
                .start(linkPersonToJobStep)
                .next(addAddressStep)
                .next(addGenderStep)
                .listener(enrichPersonJobListener)
                .build();
    }

}
