package com.y_ghelp.test.demo.my;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.xnx3.microsoft.SystemUtil;
import com.y_ghelp.test.demo.config.MYConfig;

public class Dnplayer2Util {
	

	private static String APPPATH = "";
	
	static {
		APPPATH = (String) MYConfig.getInstance().getConfig("dnplayer2Path");
	}
	
	private static String getPanFu() {
		if(StringUtils.isNotBlank(APPPATH)) {
			return APPPATH.substring(0,2);
		}else {
			System.out.println("未能获取模拟器安装目录");
			return "";
		}
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
	 * TODO 多开未处理
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
