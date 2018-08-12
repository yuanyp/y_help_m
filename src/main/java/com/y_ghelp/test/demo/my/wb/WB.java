package com.y_ghelp.test.demo.my.wb;

import java.awt.PopupMenu;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.xnx3.Lang;
import com.xnx3.microsoft.SystemUtil;
import com.y_ghelp.test.demo.my.Base;

/**
 * 夜神模拟器挖宝
 * 【当前class目录下放上icon.png作为托盘图标】
 * @author yuanyp
 *
 */
public class WB extends Base{
	
	public static Thread openGame = new Thread(new Runnable() {
        public void run() {
            try {
                // 打开应用,此函数会阻塞当前线程，直到打开的关闭为止。故而须另开辟一个线程执行此函数
                String cmdExe = " start \"\" \"" + Constant.appPath + "\"";
                SystemUtil.cmd(cmdExe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
	
	public static boolean isMove(){
		//748 * 35 40*10
		file.screenImage(748,35,(748+40),(35+10), "c:\\logs\\bcd.png");
    	boolean a = findPic.isDisplayDead(748,35,(748+40),(35+10),2);
    	System.out.println("isMove " + !a);
    	return !a;
    }
	
	public static void main(String[] args) {
		setImgHomeFolder(Constant.img_home);
		setScreenWidthAndHeight(800, 600);
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
		tray=SystemUtil.createTray(WB.class.getResource("/icon.png"), "显示文字", popupMenu);
		
		//创建托盘完毕后，拿到的tray对象可以进行在创建的托盘上弹出文字提示
		tray.displayMessage("标题", "托盘创建完毕", MessageType.INFO);
		
	}
}
