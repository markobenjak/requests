package com.java.app.requests.ScheduledJobs;

import com.java.app.requests.Request.RequestsDao;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class LogSubmittedOrders extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger(LogSubmittedOrders.class);

    private final RequestsDao requestsDao;

    public LogSubmittedOrders(RequestsDao requestsDao) {
        this.requestsDao = requestsDao;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        requestsDao.deleteStoredRequests();

        log.info("Deleting old incoming requests");

    }
}
