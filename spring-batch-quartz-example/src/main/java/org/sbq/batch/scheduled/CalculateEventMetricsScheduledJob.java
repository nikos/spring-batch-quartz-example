/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sbq.batch.scheduled;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ilya40umov
 */
@DisallowConcurrentExecution // because we store job state between executions
@PersistJobDataAfterExecution // because we store last fire time between executions
public class CalculateEventMetricsScheduledJob extends AbstractScheduledJob {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobRegistry jobRegistry = getJobRegistry();
        JobLauncher jobLauncher = getJobLauncher();
        long finishedAt = context.getMergedJobDataMap().getLong("finishedAt");
        Date startingFrom = new Date(finishedAt);
        Date endingAt = new Date();
        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("startingFrom", new JobParameter(startingFrom));
        parameters.put("endingAt", new JobParameter(endingAt));
        try {
            jobLauncher.run(jobRegistry.getJob("calculateEventMetricsJob"), new JobParameters(parameters));
            context.getMergedJobDataMap().putAsString("finishedAt", endingAt.getTime());
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
}
