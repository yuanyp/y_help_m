package com.y_ghelp.test.demo.my;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.Sleep;
import com.xnx3.microsoft.SystemUtil;
import com.y_ghelp.test.demo.config.MYConfig;

/**
 * 自动获取卡片（天秤座）卡片任务
 * @author yuanyp
 *
 */
public class AutoGetCard {

	public MYDemo myDemo;
	
	public void a() {
		Base.addLog("开始登录账号..");
    	String user = (String)MYConfig.getInstance().getConfig("user_login_card");
        if(StringUtils.isBlank(user)){
        	Base.addLog("未能找到账号..");
            return;
        }
        String[] users = user.split(";");
        Base.addLog("users .." + users.length);
        for(String itemUser : users){
            String[] userInfo = itemUser.split(",");//下标：第0个账号，第1个密码，第2个是否含有小号
            if(userInfo.length == 3){
//            	myDemo.start_game(userInfo, false);
            }
        }
	}
	
	static Thread open = new Thread(new Runnable() {
        public void run() {
            try {
            	SystemUtil.cmd("notepad");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
	
	static Thread start1 = new Thread(new Runnable() {
        public void run() {
            try {
            	Base.press.keyPress(Press.NUM_1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
	
	static Thread start2 = new Thread(new Runnable() {
        public void run() {
            try {
            	Base.press.keyPress(Press.NUM_2);
            	Base.press.keyPress(Press.NUM_2);
            	Base.press.keyPress(Press.NUM_2);
            	Base.press.keyPress(Press.NUM_2);
            	Base.press.keyPress(Press.NUM_2);
            	Base.press.keyPress(Press.NUM_2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
	
	private static int hwnd1;
	private static int hwnd2;
	
	public static void main(String[] args) {
		try {
			Base.threadPool.execute(open);
			Thread.sleep(2000);
			int a = Base.window.findWindow(0, null, "记事本");
			if(a > 0) {
				Base.window.setWindowTitle(a, "记事本1");
			}
			System.out.println("记事本1：" + a);
			hwnd1 = a;
			

			
			Base.threadPool.execute(open);
			Thread.sleep(2000);
			int b = Base.window.findWindow(0, null, "记事本");
			if(b > 0) {
				Base.window.setWindowTitle(b, "记事本2");
			}
			System.out.println("记事本2：" + b);
			hwnd2 = b;
			
			
			List<Integer> list = Base.window.EnumWindow(0, "记事本", "", 1);
			System.out.println(list);
			
//			a = Base.window.findWindow(0, null, "窗口1");
//			System.out.println("窗口1：" + a);
//			b = Base.window.findWindow(0, null, "窗口2");
//			System.out.println("窗口2：" + b);
			
//			boolean flaga = Base.com.bind(a, Com.GDI, Com.WINDOWS, Com.WINDOWS, 0);
//			System.out.println("绑定结果a" + flaga);
//			Base.window.moveWindow(a, 0, 0);
			Thread.sleep(200);
			Base.file.screenImage(0, 0, 1920, 1080,  "f:\\" + System.currentTimeMillis()+".png");

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
