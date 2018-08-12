package com.y_ghelp.test.demo.my.wb;

import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.apache.commons.lang3.StringUtils;

import com.xnx3.microsoft.Color;
import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.FindPic;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.Window;
import com.xnx3.robot.Robot;
import com.xnx3.robot.support.CoordBean;
import com.y_ghelp.test.demo.config.MYConfig;
import com.y_ghelp.test.demo.my.Base;

public class Layout extends JFrame{
    Com com = Base.com;
    Window window = Base.window;
    Mouse mouse = Base.mouse;
    Press press = Base.press;
    Color color = Base.color;
    Robot robot = Base.robot;
    FindPic findPic = Base.findPic;
    com.xnx3.microsoft.File file = Base.file;
    
    public void execute(){
    	//打开模拟器
    	openApp();
    	int hwnd = window.findWindow(0, null, Constant.appName);
        if(hwnd > 0){
        	System.out.println("模拟器句柄：" + hwnd);
            window.moveWindow(hwnd, 0, 0);
            window.setWindowActivate(hwnd); //激活窗口
        }
        //进入游戏
        openGameApp();
        //检查是否需要更新
        update();
        //登录
        login();
        //开始挖宝
        wb();
    }
    
    public void wb(){
    	System.out.println("wb start..");
    	//领藏宝图
    	lcbt();
    	//点击藏宝图，自动寻路挖宝
    	open_cbt();
    }
    
    public void open_cbt(){
    	System.out.println("wb open_cbt..");
    }
    public void lcbt(){
    	robot.delay(500);
    	List<CoordBean> list = Base.findPic(Constant.wb_0,10000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	robot.delay(500);
        	list = Base.findPic(Constant.wb_1,2000);
        	if(list.size() > 0){
        		//x+371,y-8
        		mouse.mouseClick(list.get(0).getX() + 371, list.get(0).getY() - 8, true);
        		list = Base.findPic(Constant.wb_2,45000);
        		if(list.size() > 0){
        			do{
        				mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);	
        			}while(Base.findPic(Constant.wb_3).size() <= 0);
        		}
        	}
    	}
    }
    
    public String[] loadUserInfo(){
    	String user = (String)MYConfig.getInstance().getConfig("user");
        if(StringUtils.isBlank(user)){
            System.out.println("未能找到账号..");
        }
        String[] users = user.split(";");
        for(String itemUser : users){
            String[] userInfo = itemUser.split(",");//下标：第0个账号，第1个密码，第2个是否含有小号
            if(userInfo.length == 3){
                if(!Base.listUsers.contains(userInfo[0])){//账号没有处理过
                	Base.listUsers.add(userInfo[0]);
                	return userInfo;
                }
            }else{
                System.out.println("账号配置错误..");    
            }
        }
        return null;
    }
    
    public void login(){
    	List<CoordBean> list = Base.findPic(Constant.login_1,2000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}
    	String[] user = loadUserInfo();
    	System.out.println("开始登录：" + user);
    	input_user(user);
    	robot.delay(200);
    	list = Base.findPic(Constant.login_2,2000);
    	if(list.size() > 0){
    		//点击登录
    		mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
    		list = Base.findPic(Constant.login_success,30000);
    		if(list.size() > 0){
    			System.out.println("登录成功..");
    			list = Base.findPic(Constant.login_3,2000);
    			if(list.size() > 0){
    				mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
    			}
    			list = Base.findPic(Constant.go_index,30000);
    			if(list.size() > 0){
    				mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
    				robot.delay(500);
    			}
    		}
    	}
    }
    
    
    /**
     * 输入账号密码
     * @param user
     */
    public void input_user(String[] user){
    	clearInput();
    	robot.delay(200);
    	List<CoordBean> list = Base.findPic(Constant.login_u,2000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX() + 40, list.get(0).getY() + 5, true);
    		robot.sendString(user[0]);
    	}
    	list = Base.findPic(Constant.login_p,2000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX() + 40, list.get(0).getY() + 5, true);
    		robot.sendString(user[1]);
    	}
    }
    
    public void clearInput(){
    	robot.delay(200);
    	List<CoordBean> list = Base.findPic(Constant.login_u,2000);
    	if(list.size() > 0){
    		mouse.mouseMoveTo(list.get(0).getX() + 40, list.get(0).getY() + 5);
    		Base.doubleClick();
    		robot.delay(200);
    		press.keyPress(press.DELETE);
    	}
    }
    
    public void openGameApp(){
    	//1分钟内循环查找游戏app。如果进入了桌面 肯定能找到
    	List<CoordBean> list = Base.findPic(Constant.app_game,60000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}
    	robot.delay(500);
    }
    
    public void update(){
    	List<CoordBean> list = Base.findPic(Constant.login_1,10000);
    	if(list.size() > 0){
    		return;
    	}
    	//20秒内循环查是否需要更新。
    	list = Base.findPic(Constant.update_1,20000);
    	if(list.size() > 0){
    		list = Base.findPic(Constant.update_2);
    		if(list.size() > 0){
    			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    			//60秒内循环查是否已经下载完成更新包，跳转到安装界面。
    	    	list = Base.findPic(Constant.update_3,60000);
    	    	if(list.size() > 0){
    	    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	    		list = Base.findPic(Constant.update_4,2000);
    	    		if(list.size() > 0){
    	    			System.out.println("更新完毕..");
    	    			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	    		}
    	    	}
    		}
    	}
    }
    
    public void openApp(){
    	WB.threadPool.execute(WB.openGame);
    }
    
    public Layout() {
    	JButton btnNewButton = new JButton("New button");
    	GroupLayout groupLayout = new GroupLayout(getContentPane());
    	groupLayout.setHorizontalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
    			.addGroup(groupLayout.createSequentialGroup()
    				.addGap(123)
    				.addComponent(btnNewButton)
    				.addContainerGap(218, Short.MAX_VALUE))
    	);
    	groupLayout.setVerticalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
    			.addGroup(groupLayout.createSequentialGroup()
    				.addGap(56)
    				.addComponent(btnNewButton)
    				.addContainerGap(183, Short.MAX_VALUE))
    	);
    	getContentPane().setLayout(groupLayout);
    }
}
