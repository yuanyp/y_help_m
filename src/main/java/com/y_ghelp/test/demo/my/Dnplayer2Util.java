package com.y_ghelp.test.demo.my;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.xnx3.microsoft.DmSoft;
import com.xnx3.microsoft.SystemUtil;
import com.y_ghelp.test.demo.config.MYConfig;

public class Dnplayer2Util {
	
	static Logger log = Logger.getLogger(Dnplayer2Util.class);	

	/**
	 * 模拟器安装目录,从配置文件获取
	 */
	private static String APPPATH = "";
	
	public static String _NAME = "雷电模拟器";
	
	/**
	 * 2:顶层窗口句柄
	 */
	public static int _TOP_HWND = 2;
	/**
	 * 3:绑定窗口句柄，
	 */
	public static int _BIND_HWND = 3;
	/**
	 * 序号编号
	 */
	public static int _INDEX = 0;
	
	static {
		APPPATH = (String) MYConfig.getInstance().getConfig("dnplayer2Path");
	}
	
	/**
	 * 获取模拟器安装盘 如：F:
	 * @return
	 */
	private static String getPanFu() {
		if(StringUtils.isNotBlank(APPPATH)) {
			return APPPATH.substring(0,2);
		}else {
			log.error("未能获取模拟器安装目录");
			return "";
		}
	}

	/**
	 * 执行雷电模拟器命令
	 * @param cmd
	 * @return
	 */
	public static String exeCmd(String cmd) {
		String console = SystemUtil.cmd(getPanFu() + " && cd " + APPPATH + " && " + cmd);
		return console;
	}

	/**
	 * 根据模拟器名称
	 * 单开-打开模拟器
	 */
	public static void openDnplayer() {
		// 进入到安装目录
		String console = exeCmd("dir /b");
		log.info(console);
		if (console.contains("dnconsole.exe")) {
			log.info("成功进入到安装目录");
			console = SystemUtil.cmd("dnconsole.exe launch --name 雷电模拟器");
		}
	}

	/**
	 * 根据雷电多开器编号，启动模拟器 
	 * @param index
	 */
	public static void startDnplayer2(int index) {
		exeCmd("dnconsole.exe launch --index " + index);
	}
	
	/**
	 * 根据模拟器名称，启动模拟器 
	 * @param index
	 */
	public static void startDnplayer2ByName(String name) {
		exeCmd("dnconsole.exe launch --name " + name);
	}
	
	/**
	 * modify <--name mnq_name | --index mnq_idx>
    [--resolution <w,h,dpi>] // 自定义分辨率
    [--cpu <1 | 2 | 3 | 4>] // cpu设置
    [--memory <512 | 1024 | 2048 | 4096 | 8192>] // 内存设置
    [--manufacturer asus] // 手机厂商
    [--model ASUS_Z00DUO] // 手机型号
    [--pnumber 13812345678] // 手机号码
    [--imei <auto | 865166023949731>] // imei设置，auto就自动随机生成        
    [--imsi <auto | 460000000000000>]    
    [--simserial <auto | 89860000000000000000>]
    [--androidid <auto | 0123456789abcdef>]
    [--mac <auto | 000000000000>] //12位m16进制mac地址
    [--autorotate <1 | 0>]
    [--lockwindow <1 | 0>]

	例子，修改默认模拟器的分辨率为600*360,dpi 160,cpu为1核，内存1024，imei随机，这样写：
	dnconsole.exe modify --index 0 --resolution 600,360,160 --cpu 1 --memory 1024 --imei auto
	 * @param name
	 */
	public static void modify(int index,String resolution,int cpu,int memory) {
		
	}
	
	

	
	/**
	 * 调用该方法前请先打开模拟器
	 * 获取模拟器相关信息
	 *  返回值：
	 *  [
	 *   [0, 雷电模拟器, 0, 0, 0, -1, -1],
	 *   [1, 雷电模拟器-1, 1314134, 5508932, 1, 3928, 14456],
	 *   [2, 雷电模拟器-2, 921188, 1052214, 1, 32704, 28036]
	 *  ]
	 *  0:索引，1:标题，2:顶层窗口句柄，3:绑定窗口句柄，4:是否进入android，5:进程PID，6:VBox进程PID
	 *  
	 */
	public static List<List<String>> list2() {
		List<List<String>> ret = new ArrayList<>();
		String console = exeCmd("dnconsole.exe list2");
		if (StringUtils.isNotBlank(console)) {
			List<String> lists = Arrays.asList(console.split("\n"));
			log.info("dnconsole.exe list2" + lists);
			if(null != lists) {
				for(String item : lists) {
					String[] arrs = item.split(",");
					ret.add(Arrays.asList(arrs));
				}
			}
		}
		return ret;
	}
	
	/**
	 * 多开时-返回还没有绑定的模拟器
	 * 原理:获取窗口名称还没有被改过的模拟器,绑定模拟器时修改窗口名称
	 * @return
	 */
	public static List<String> list2NoBind(DmSoft dm) {
		 List<List<String>> lists = list2();
		 for(List<String> item : lists) {
			 int hwnd = Integer.parseInt(item.get(_TOP_HWND));//顶层窗口句柄
			 if(hwnd > 0) {
				 String title = dm.GetWindowTitle(hwnd);
				 if(StringUtils.isNotBlank(title) && title.contains(_NAME)) {
					 return item;
				 }
			 }
		 }
		 return null;
	}
	
	
	
	/**
	 * 鼠标点击
	 * @param x
	 * @param y
	 */
	public static void mouseLeftClick(int x,int y) {
		exeCmd("ld input tap "+x+" " + y);
	}
	
	/**
	 * 输入
	 * @param input
	 */
	public static void input(String input) {
		exeCmd("ld input " + input);
	}
	
	public static void main(String[] args) {
		DmSoft dm = new DmSoft();
		List<String> a = list2NoBind(dm);
		log.info(a);
//		int hwnd = Integer.parseInt(a.get(1).get(2));
//		DmSoft dm = new DmSoft();
//		dm.SetWindowText(hwnd, "yyp hhh");
//		a = list2();
//		log.info(a);		
	}

}
