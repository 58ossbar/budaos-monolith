package com.budaos.modules.job.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Title:定时任务配置
 * Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Configuration
public class ScheduleConfig {
	@Value("${com.budaos.schedule-cluster:false}")
	private boolean isCluster;
	@Value("${com.budaos.schedule-autostart:true}")
	private boolean autoStart;
	

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // 使用 Spring 默认的数据源，不再单独指定名称
        factory.setDataSource(dataSource);
        factory.setApplicationContextSchedulerContextKey("applicationContext");

        //quartz参数
        Properties prop = new Properties();
        prop.put("org.quartz.scheduler.instanceName", "budaosScheduler");
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        //线程池配置
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "20");
        prop.put("org.quartz.threadPool.threadPriority", "5");
        //JobStore 配置
        prop.put("org.quartz.jobStore.class", "org.springframework.scheduling.quartz.LocalDataSourceJobStore");
        //集群配置
        if(isCluster) {
        	prop.put("org.quartz.jobStore.isClustered", "true");
        	prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
        	prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
        }
        prop.put("org.quartz.jobStore.misfireThreshold", "12000");
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        factory.setQuartzProperties(prop);

        factory.setSchedulerName("budaosScheduler");
        //延时启动
        factory.setStartupDelay(60);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        //可选，QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        //设置自动启动，默认为 true
        factory.setAutoStartup(autoStart);
        return factory;
    }
}
