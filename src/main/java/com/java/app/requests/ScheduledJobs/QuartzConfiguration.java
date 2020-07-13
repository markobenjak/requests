package com.java.app.requests.ScheduledJobs;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.quartz.TriggerBuilder.newTrigger;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail scheduleJob() {
        return JobBuilder.newJob(LogSubmittedOrders.class)
                .storeDurably()
                .withIdentity("deleteStoredRequests")
                .withDescription("Deleting of Stored Requests")
                .build();
    }

    @Bean
    public Trigger scheduleTrigger() {
        return newTrigger()
                .withIdentity("deleteRequestsTrigger")
                .forJob(scheduleJob())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(6).repeatForever())
                .build();
    }

}
