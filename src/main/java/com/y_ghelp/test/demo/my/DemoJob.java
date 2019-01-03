package com.y_ghelp.test.demo.my;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DemoJob implements Job{

    public DemoJob(){
    }
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getJobDetail().getJobDataMap();
        MYDemo mydemo = (MYDemo)data.get("mydemo");
        mydemo.addLog("DemoJob execute start " + new Date());
        mydemo.listUsers.clear();
        mydemo.threadPool.execute(mydemo.playGameTh);
        mydemo.addLog("DemoJob execute end " + new Date());
    }
}
