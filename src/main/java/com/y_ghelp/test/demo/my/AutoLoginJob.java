package com.y_ghelp.test.demo.my;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AutoLoginJob implements Job{

    public AutoLoginJob(){
    }
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getJobDetail().getJobDataMap();
        MYDemo mydemo = (MYDemo)data.get("mydemo");
        mydemo.addLog("AutoLoginJob execute start " + new Date());
        mydemo.listUsers.clear();
        mydemo.threadPool.execute(mydemo.login);
        mydemo.addLog("AutoLoginJob execute end " + new Date());
    }
}
