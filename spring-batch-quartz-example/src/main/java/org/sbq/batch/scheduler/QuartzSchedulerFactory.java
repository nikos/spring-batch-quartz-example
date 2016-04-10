package org.sbq.batch.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ilya40umov
 */
@Component
public class QuartzSchedulerFactory extends SchedulerFactoryBean {

    //@Autowired
    //private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * XXX This method is invoked by Spring before afterPropertiesSet().
     */
    @PostConstruct
    private void setUp() {
        setConfigLocation(resourceLoader.getResource("classpath:/quartz.properties"));
        setApplicationContextSchedulerContextKey("applicationContext");
        setAutoStartup(false);
        setWaitForJobsToCompleteOnShutdown(true);
    }

}
