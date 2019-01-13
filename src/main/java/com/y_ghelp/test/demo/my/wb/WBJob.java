package com.y_ghelp.test.demo.my.wb;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.y_ghelp.test.demo.my.Base;

public class WBJob implements Job{

    public WBJob(){
    }
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getJobDetail().getJobDataMap();
        WB wbjob = (WB)data.get("wbjob");
        wbjob.addLog("WBJob execute start " + new Date());
        Base.resetUsers();
        Base.press.groupPress(Base.robot.StringToKey("alt"), Base.robot.StringToKey("f9"));
        wbjob.addLog("WBJob execute end " + new Date());
    }
}
