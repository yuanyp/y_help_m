package com.y_ghelp.test.demo.mouse;

import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.xnx3.Lang;
import com.xnx3.microsoft.Color;
import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.FindPic;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.SystemUtil;
import com.xnx3.microsoft.Window;
import com.xnx3.robot.Robot;

/**
 * 鼠标工具
 * 【当前class目录下放上icon.png作为托盘图标】
 * @author yuanyp
 *
 */
public class MouseTool{
    
    static TrayIcon tray;
    
    static Layout frame = null;
    
    static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
            new ThreadPoolExecutor.DiscardOldestPolicy());
    
    static Thread start;
    static Com com;
    static Window window;
    static Mouse mouse;
    static Press press;
    static Color color;
    static Robot robot;
    static FindPic findPic;
    static boolean execute = false;
    
    static{
        com = new Com();
        window = new Window(com.getActiveXComponent());    //窗口操作类
        mouse = new Mouse(com.getActiveXComponent());   //鼠标模拟操作类
        press = new Press(com.getActiveXComponent());   //键盘模拟操作类
        color = new Color(com.getActiveXComponent());   //颜色相关的取色、判断类
        findPic = new FindPic(com.getActiveXComponent());
        robot = new Robot();
        start = new Thread(new Runnable() {
            public void run() {
                try {
                    while(execute){
                       robot.delay(100);
                       frame.execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
	public static void main(String[] args) {
		/********创建界面********/
		frame = new Layout();
		frame.setBounds(100, 100, 700, 250);
		frame.setVisible(true);
		
		final SysHotKey sysHotKey = new SysHotKey(frame);
        sysHotKey.initHotkey();
        
		/************创建托盘************/
		//添加右键弹出按钮
		PopupMenu popupMenu=new PopupMenu();
		java.awt.MenuItem menuItemExit=new java.awt.MenuItem("Exit");	//退出按钮
		java.awt.MenuItem menuItemAbout=new java.awt.MenuItem("About");	//关于按钮
		menuItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    boolean unbind = com.unbind();
			    System.out.println("com.unbind " + unbind);
			    sysHotKey.destroy();
			}
		});
		menuItemAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Lang.showMessageDialog("鼠标工具");
			}
		});
		popupMenu.add(menuItemExit);
		popupMenu.add(menuItemAbout);
		
		//创建托盘，可以对tray创建监听事件
		tray=SystemUtil.createTray(MouseTool.class.getResource("/icon.png"), "显示文字", popupMenu);
		
		//创建托盘完毕后，拿到的tray对象可以进行在创建的托盘上弹出文字提示
		tray.displayMessage("标题", "托盘创建完毕", MessageType.INFO);
		
	}
}
