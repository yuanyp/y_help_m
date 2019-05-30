package com.y_ghelp.test.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.xnx3.bean.ActiveBean;
import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.DmSoft;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.Sleep;
import com.xnx3.microsoft.SystemUtil;

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
		List<String> list = list2();
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

	public static final String APPPATH = "F:\\\\ChangZhi\\\\dnplayer2";

	private static String getPanFu() {
		return "F:";
	}

	public static String exeCmd(String cmd) {
		String console = SystemUtil.cmd(getPanFu() + " && cd " + APPPATH + " && " + cmd);
		return console;
	}

	/**
	 * 打开模拟器
	 */
	public static void openDnplayer() {
		// 进入到安装目录
		String console = exeCmd("dir /b");
		System.out.println(console);
		if (console.contains("dnconsole.exe")) {
			System.out.println("成功进入到安装目录");
			console = SystemUtil.cmd("dnconsole.exe launch --name 雷电模拟器");
			System.out.println(console);
		}
	}

	/**
	 * 获取模拟器相关信息 返回值：0,雷电模拟器,2032678,1704928,1,7456,3500
	 * 0:索引，1:标题，2:顶层窗口句柄，3:绑定窗口句柄，4:是否进入android，5:进程PID，6:VBox进程PID
	 */
	public static List<String> list2() {
		String console = exeCmd("dnconsole.exe list2");
		if (StringUtils.isNotBlank(console)) {
			return Arrays.asList(console.split(","));
		} else {
			return new ArrayList<>();
		}
	}
	
	public static void mouseLeftClick(int x,int y) {
		exeCmd("ld input tap "+x+" " + y);
	}

}