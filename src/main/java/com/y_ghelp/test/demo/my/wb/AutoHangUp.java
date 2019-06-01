package com.y_ghelp.test.demo.my.wb;

import java.util.List;

import org.apache.log4j.Logger;

import com.xnx3.bean.ActiveBean;
import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.DmSoft;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.Sleep;
import com.xnx3.robot.support.CoordBean;
import com.y_ghelp.test.demo.my.Common;
import com.y_ghelp.test.demo.my.Dnplayer2Util;
import com.y_ghelp.test.demo.my.Util;

/**
 * 
 * @author yuanyp
 *
 */
public class AutoHangUp {
	
	Logger log = Logger.getLogger(getClass());

	public boolean die = false;
	public boolean m3_start = false;

	private ActiveBean activeBean;
	private Sleep sleep;
	private Press press;
	private Mouse mouse;
	private DmSoft dm;
	private Util util;
	private int hwnd = 0;

	public AutoHangUp() {
		dm = new DmSoft();
		activeBean = new ActiveBean();
		activeBean.setDm(dm.getDM());
		press = new Press(activeBean);
		mouse = new Mouse(activeBean);
		util = new Util(activeBean);
		sleep = new Sleep();
		List<String> list = Dnplayer2Util.list2();
		hwnd = Integer.parseInt(list.get(3));
		int result = dm.BindWindowEx(hwnd, Com.GDI, Com.WINDOWS, Com.WINDOWS, "", 0);
		log.info("bind  > " + result);
	}

	/**
	 * m3_jiao1_2 m3_jiao1_2_top m3_jiao1_1
	 * @param flag
	 */
	private void m3_start(int flag) {
		log.info("start..");
		do {
			if (die) {
				log.info("die.. wait 5 seconds..");
				new Sleep().sleep(5000);
				m3_start(flag);
			}
			sleep.sleep(500);
			log.info("F2..");
			press.keyPress(press.F2);
			new Sleep().sleep(1500);
			log.info("F3..,wait 6 seconds..");
			press.keyPress(press.F3);
			new Sleep().sleep(6000);
			if (flag == 1) {
				goto_xy();// 回到中间
			} else if (flag == 2) {
				goto_xy_top();
			}
			for (int i = 0, j = 5; i < j; i++) {
				if (die) {
					break;
				}
				if (!m3_start) {
					break;
				}
				shua_m3jy();
				if (flag == 1) {
					goto_xy();// 回到中间
				} else if (flag == 2) {
					goto_xy_top();
				}
			}
			int cd = getCD();
			new Sleep().sleep(cd);// 等CD
		}while(m3_start);
		log.info("stop..");
	}

	private int getCD() {
		// Integer.parseInt(m3_textField_cd.getText()) * 1000;
		return 0;
	}


	private void shua_m3jy() {
		List<CoordBean> guaiwu = util.findPic(Common.m3_jingying+"|"+Common.m3_jingying1);
		new Sleep().sleep(200);
		if (null != guaiwu && guaiwu.size() > 0) {
			log.info("找到怪物坐标为：" + guaiwu.get(0).getX() + "," + guaiwu.get(0).getY());
			mouse.mouseMoveTo(guaiwu.get(0).getX(), guaiwu.get(0).getY() + 50);
			new Sleep().sleep(200);
			log.info("按下F1");
			press.keyPress(press.F1);
			new Sleep().sleep(2500);
		}
	}

	private void goto_xy() {
		if (!m3_start) {
			return;
		}
		if (die) {
			return;
		}
		log.info("goto_xy start ..");
		List<CoordBean> list1 = util.findPic(Common.m3_jiao1_1);
		int x = 0;
		int y = 0;
		if (list1.size() > 0) {
			x = list1.get(0).getX() + x;
			y = list1.get(0).getY() + 140;
		} else {
			List<CoordBean> list2 = util.findPic(Common.m3_jiao1_2);
			if (list2.size() > 0) {
				x = list2.get(0).getX() + 232;
				y = list2.get(0).getY();
			}
		}
		if (x > 0 && y > 0) {
			log.info("x,y("+x+","+y+")");
			f1(x, y);
			mouse.mouseClick(x, y, true);
		}
		new Sleep().sleep(500);
	}

	private void f1(int x, int y) {
		mouse.mouseMoveTo(x, y);
		new Sleep().sleep(200);
		press.keyPress(press.F1);
		log.info("按下F1");
		new Sleep().sleep(200);
	}

	private void goto_xy_top() {
		if (!m3_start) {
			return;
		}
		if (die) {
			return;
		}
		log.info("goto_xy_top start ..");

		List<CoordBean> list1 = util.findPic(Common.m3_jiao1_2);
		int x = 0;
		int y = 0;
		if (list1.size() > 0) {
			x = list1.get(0).getX() + 205;
			y = list1.get(0).getY() - 10;
		} else {
			List<CoordBean> list2 = util.findPic(Common.m3_jiao1_2_top);
			if (list2.size() > 0) {
				x = list2.get(0).getX() - 69;
				y = list2.get(0).getY() - 166;
			}
		}
		if (x > 0 && y > 0) {
			f1(x, y);
			mouse.mouseClick(x, y, true);
		}
		new Sleep().sleep(500);
	}
	
	public void destroy() {
		dm.SetWindowState(hwnd, 1);
		int r = dm.UnBindWindow();
		log.info("解绑结果>" + r);
	}
	
	public AutoHangUpThread getAutoHangUpThread(int flag) {
		AutoHangUpThread thread = new AutoHangUpThread();
		thread.flag = flag;
		return thread;
	}
	
	public class AutoHangUpThread implements Runnable{
		int flag = 0;
		@Override
		public void run() {
			try {
				m3_start(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
		}
    }
}
