package com.y_ghelp.test.demo.my;

import org.apache.commons.lang3.StringUtils;

import com.y_ghelp.test.demo.config.MYConfig;

public class ThreadQiangTanWei implements Runnable {

	private MYDemo myDemo;

	public void setMyDemo(MYDemo _myDemo) {
		this.myDemo = _myDemo;
	}

	@Override
	public void run() {
		try {
			String user = (String) MYConfig.getInstance().getConfig("user_login_making");
			if (StringUtils.isNotBlank(user)) {
				String[] users = user.split(";");
				if (users.length <= 0) {
					return;
				}
				for (String itemUser : users) {
					AutoQiangTanWei autoMaking = new AutoQiangTanWei(myDemo, myDemo.robot, myDemo.com);
					autoMaking.setUser(itemUser);
					autoMaking.main();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
