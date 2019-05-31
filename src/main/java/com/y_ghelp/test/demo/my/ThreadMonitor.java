package com.y_ghelp.test.demo.my;

import com.xnx3.microsoft.Sleep;
import com.y_ghelp.test.demo.config.MYConfig;

public class ThreadMonitor implements Runnable {

	private MYDemo myDemo;

	public void setMyDemo(MYDemo _myDemo) {
		this.myDemo = _myDemo;
	}

	@Override
	public void run() {
		try {
			myDemo.addLog("监控开启..");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void monitor() {
		Util util = new Util(myDemo.com.getActiveXComponent());
		Sleep sleep = new Sleep();
		int millis = Integer.parseInt(MYConfig.getInstance().getConfig("monitor_millis") + "");
		while(true) {
			myDemo.workingMonitor = true;
//			util.findPic(img);
			sleep.sleep(millis);
			myDemo.workingMonitor = false;
		}
	}
}
