package com.y_ghelp.test.demo.my.wb;

import java.awt.PopupMenu;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import com.xnx3.Lang;
import com.xnx3.microsoft.SystemUtil;
import com.xnx3.robot.support.CoordBean;
import com.y_ghelp.test.demo.config.MYConfig;
import com.y_ghelp.test.demo.my.Base;

/**
 * 夜神模拟器挖宝
 * 【当前class目录下放上icon.png作为托盘图标】
 * @author yuanyp
 *
 */
public class WB extends Base{
	
	static int sx = 0;
	static int sy = 0;
	static int ex = 800;
	static int ey = 600;
	
	static int die_count = 0;
	static boolean die = false;//判断人物是否死亡
	
	public static boolean die(List<CoordBean> _list) {
		if(null != _list){
			_list.clear();
		}
		robot.delay(500);
		List<CoordBean> list = findPic(Constant.die,sx,sy,ex,ey,0.9,false);
		if(list.size() > 0){
			Base.screenDieImage();
			addErrorLog("检测到人物死亡【"+Base.runRole.toString()+"】");
			_list.addAll(list);//返回坐标
			WB.die = true;
			return true;
		}
		WB.die = false;
		return false;
	}
	
	public static Thread checkDie = new Thread(new Runnable() {
        public void run() {
            try {
            	//处理如果人物死亡自动复活
            	//检测人物死亡开始
            	Base.addLog("自动复活开启..");
            	boolean flag = true;
            	do{
            		List<CoordBean> list = new ArrayList<>();
            		boolean die = die(list);
            		if(die){
            			robot.delay(200);
            			int x = list.get(0).getX() + 5;
            			int y = list.get(0).getY() + 95;
            			Base.addLog("鼠标点击复活坐标1【"+x+","+y+"】..");
        				mouse.mouseClick(x, y, true);
        				robot.delay(500);
        				//68 86
        				int x1 = list.get(0).getX() - 68;
        				int y1 = list.get(0).getY() + 86;
        				Base.addLog("鼠标点击复活坐标2【"+x1+","+y1+"】..");
        				mouse.mouseClick(x1, y1, true);
            		}else {
            			WB.die = false;
            			flag = false;
            		}
            	}while(flag);
            	WB.die_count = WB.die_count + 1;//复活次数+1
            	Base.addLog("复活次数+1...【"+WB.die_count+"】");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
	
	public static Thread openGame = new Thread(new Runnable() {
        public void run() {
            try {
            	String appPath = (String)MYConfig.getInstance().getConfig("defaultYSPath");
                // 打开应用,此函数会阻塞当前线程，直到打开的关闭为止。故而须另开辟一个线程执行此函数
                String cmdExe = " start \"\" \"" + appPath + "\"";
                SystemUtil.cmd(cmdExe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
	
	
	public static void main(String[] args) {
		setImgHomeFolder(Constant.img_home);
		setDic(0, Constant.dm_dic);
		setScreenWidthAndHeight(800, 600);
		frame = new Layout();
		frame.setBounds(100, 100, 700, 250);
		frame.setVisible(true);
		frame.initJob(new WB());
		/********创建界面********/
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
