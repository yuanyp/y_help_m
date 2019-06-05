package com.y_ghelp.test.demo;

import java.util.List;

import com.xnx3.bean.ActiveBean;
import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.DmSoft;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.Sleep;
import com.y_ghelp.test.demo.my.Dnplayer2Util;

/**
 * 雷电模拟器命令测试demo
 * 
 * @author yuanyp
 *
 */
public class Dnplayer2Demo {

	public static void main(String[] args) {
		DmSoft dm = new DmSoft();
		ActiveBean activeBean = new ActiveBean();
		activeBean.setDm(dm.getDM());
		Press press = new Press(activeBean);
		Mouse mouse = new Mouse(activeBean);
		Sleep sleep = new Sleep();
		List<String> list = Dnplayer2Util.list2NoBind(dm);
		int hwnd = Integer.parseInt(list.get(3));
		int result = dm.BindWindowEx(hwnd, Com.GDI, Com.WINDOWS, Com.WINDOWS, "", 0);
		System.out.println("绑定结果  > " + result);
		System.out.println("绑定模拟器结束 > " + hwnd);

		System.out.println("测试绑定截图");
		int r = dm.Capture(0, 0, 800, 600, "f://leidiantest.png");
		System.out.println(r);
		sleep.sleep(500);
		System.out.println("测试鼠标点击登录..");
		mouse.mouseClick(92, 718, true);
//		mouseLeftClick(742, 857);
		sleep.sleep(1500);
		System.out.println("测试刷新截图");
		dm.Capture(0, 0, 800, 600, "f://leidiantest1.png");
		System.out.println("测试按键ESC..");
		press.keyPress(press.ESC);
	}

}