package com.y_ghelp.test.demo.my.wb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        	Base.addLog("模拟器句柄：" + hwnd);
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
    	int hwnd = window.findWindow(0, null, Constant.appName);
        if(hwnd > 0){
        	Base.addLog("模拟器句柄：" + hwnd);
            window.moveWindow(hwnd, 0, 0);
            window.setWindowActivate(hwnd); //激活窗口
        }
    	Base.addLog("wb start..");
    	//收起任务栏
    	List<CoordBean> list = Base.findPic(Constant.go_index_1,2000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}
    	//领藏宝图
    	lcbt();
    	//点击藏宝图，自动寻路挖宝
    	open_cbt();
    	//处理挖宝（山贼、宝树等）
    	do_all();
    	//切换账号
    	switchUser();
    }
    
    public void switchUser(){
    	Base.addLog("开始切换账号..");
    	//先退出当前账号
    	//回到登录页面
    	//重新输入账号密码
    }
    
    public boolean open_cbt(){
    	Base.addLog("wb open_cbt..");
    	//打开背包
    	Base.addLog("打开背包...");
    	press.keyPress(press.I);
    	robot.delay(200);
    	//打开任务栏 228*42 x-228 y+42
    	List<CoordBean> list = Base.findPic(Constant.beibao_close,2000);
    	int x_close = 0;
    	int y_close = 0;
    	if(list.size() > 0){
    		x_close = list.get(0).getX();
    		y_close = list.get(0).getY(); 
    		Base.addLog("打开任务栏...");
    		mouse.mouseClick(list.get(0).getX() - 228, list.get(0).getY() + 42, true);
    	}
    	robot.delay(500);
    	//查找藏宝图
    	String cangbaotu = Constant.cangbaotu_2 + "|" + Constant.cangbaotu_3
    			+ "|" + Constant.cangbaotu_4+ "|" + Constant.cangbaotu_5
    			+ "|" + Constant.cangbaotu_6;
    	list = Base.findPic(cangbaotu,412,144,756,551,5000);
    	if(list.size() > 0){
    		Base.addLog("点击藏宝图...");
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		robot.delay(1000);
			Base.addLog("使用藏宝图...");
			//75 * 43
			mouse.mouseClick(list.get(0).getX() - 75, list.get(0).getY() + 43, true);
			robot.delay(200);
			mouse.mouseClick(x_close, y_close, true);
			robot.delay(500);
			boolean flag = true;
			do{
    			//自动寻路
    			list = Base.findPic(Constant.xunlu,2000);
    			Base.addLog("自动寻路中...");
    			if(list.size() <= 0){
    				//自动寻路完毕
        			Base.addLog("自动寻路完毕...");
    				flag = false;
    			}
			}while(flag);
    		return true;
    	}else{
    		Base.addLog("没有藏宝图了...");
    		return false;
    	}
    }
    
    //do_1 处理山贼
    public void do_1(int x,int y){
    	mouse.mouseClick(x, y, true);
    	Base.addLog("处理山贼...");
    	int i = 0;//500次 100秒
    	boolean flag = true;
    	do{
    		do_0();
    		robot.delay(200);
    		press.keyPress(press.F3);
    		i++;
    		flag = Base.findPic(getBaoXiangImg()).size() <= 0;
    		if(flag){
    			if(i >= 500){
    				flag = false;
    			}
    		}
    	}while(flag);
    	Base.addLog("处理山贼结束...");
    }
    
    private void do_0(){
    	robot.delay(200);
		press.keyPress(press.F3);
		robot.delay(800);
		press.keyPress(press.F1);
		robot.delay(800);
		press.keyPress(press.F2);
		robot.delay(300);
    }
    
    //do_2 处理盗墓贼
    public void do_2(int x,int y){
    	mouse.mouseClick(x, y, true);
    	Base.addLog("处理盗墓贼...");
    	List<CoordBean> list = Base.findPic(Constant.do_2_1,200);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}
    	int i = 0;//300次 60秒
    	boolean flag = true;
    	press.keyPress(press.F3);
		robot.delay(200);
    	do{
    		press.keyPress(press.SPACE);
    		list = Base.findPic(Constant.do_2_1);
        	if(list.size() > 0){
        		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	}
    		i++;
    		flag = Base.findPic(getBaoXiangImg()).size() <= 0;
    		if(flag){
    			if(i >= 300){
    				flag = false;
    			}
    		}
    	}while(flag);
    	Base.addLog("处理盗墓贼结束...");
    }
    
    public String getBaoXiangImg(){
    	return Constant.do_baoxiang + "|" + Constant.do_baoxiang_1 + "|" + Constant.do_baoxiang_2;
    }
    
    //do_3 处理宝树
    public void do_3(int x,int y){
    	mouse.mouseClick(x, y, true);
    	Base.addLog("处理宝树...");
    	int i = 0;//300次 60秒
    	boolean flag = true;
    	do{
    		do_0();
    		press.keyPress(press.SPACE);
    		i++;
    		flag = Base.findPic(Constant.do_jingyan).size() <= 0;
    		if(flag){
    			if(i >= 300){
    				flag = false;
    			}
    		}
    	}while(flag);
    	Base.addLog("处理宝树结束...");
    }
    
    /**
     * 挖宝..
     * do_1 处理山贼
     * do_2 处理盗墓贼
     * do_3 处理宝树
     */
    public void do_all(){
    	int hwnd = window.findWindow(0, null, Constant.appName);
        if(hwnd > 0){
        	Base.addLog("模拟器句柄：" + hwnd);
            window.moveWindow(hwnd, 0, 0);
            window.setWindowActivate(hwnd); //激活窗口
        }
    	//清理小怪,避免打断挖宝
        robot.delay(200);
    	for(int i = 0,j=4;i<j;i++){
        	press.keyPress(press.F3);
        	robot.delay(500);
    	}

    	//使用藏宝图
    	boolean r = open_cbt();
    	if(r){
    		//判断出现哪一类
        	List<CoordBean> list = Base.findPic(Constant.do_ok,6000);
        	if(list.size() > 0){
        		List<CoordBean> list1 = Base.findPic(Constant.do_1);
        		List<CoordBean> list2 = Base.findPic(Constant.do_2);
        		List<CoordBean> list3 = Base.findPic(Constant.do_3);
        		if(list1.size() > 0){
        			do_1(list.get(0).getX(),list.get(0).getY());
            	}else if(list2.size() > 0){
            		do_2(list.get(0).getX(),list.get(0).getY());
            	}else if(list3.size() > 0){
            		do_3(list.get(0).getX(),list.get(0).getY());
            	}
        		list = Base.findPic(getBaoXiangImg(),2000);
        		//25 * 65
            	if(list.size() > 0){
            		Base.addLog("点击宝箱...");
            		mouse.mouseMoveTo(list.get(0).getX() + 25,list.get(0).getY() + 65);
            		robot.delay(200);
            		Base.doubleClick();
            	}
        	}else{
        		Base.addLog("直接出现经验...");
        	}
        	robot.delay(500);
        	do_all();//继续挖宝
    	}else{
    		Base.addLog("挖宝完毕...");
    	}
    }
    
    /**
     * 领藏宝图
     */
    public void lcbt(){
    	robot.delay(500);
    	List<CoordBean> list = Base.findPic(Constant.wb_0,10000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	robot.delay(500);
        	list = Base.findPic(Constant.wb_1,140,219,288,260,3000);
        	if(list.size() > 0){
        		//x+371,y-8
        		robot.delay(200);
        		mouse.mouseMoveTo(list.get(0).getX() + 366, list.get(0).getY() - 9);
        		robot.delay(500);
        		mouse.mouseClick(list.get(0).getX() + 366, list.get(0).getY() - 9, true);
        		list = Base.findPic(Constant.wb_2,145,318,269,365,45000);
        		if(list.size() > 0){
        			Base.addLog("藏宝图NPC 0/5 " + list.get(0).getX() + "," + list.get(0).getY());
        			int i = 0;
        			boolean flag = true;
        			do{
        				mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        				robot.delay(500);
        				i++;
        				if(i > 10 || Base.findPic(Constant.wb_3,145,318,269,365).size() > 0){
        					flag = false;
        				}
        			}while(flag);
        		}
        	}
    	}
    }
    
    /**
     * 加载账号信息
     * @return
     */
    public String[] loadUserInfo(){
    	String user = (String)MYConfig.getInstance().getConfig("user");
        if(StringUtils.isBlank(user)){
            Base.addLog("未能找到账号..");
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
                Base.addLog("账号配置错误..");    
            }
        }
        return null;
    }
    
    /**
     * 登录
     */
    public void login(){
    	List<CoordBean> list = Base.findPic(Constant.login_1,2000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}
    	String[] user = loadUserInfo();
    	Base.addLog("开始登录：" + user);
    	input_user(user);
    	robot.delay(200);
    	list = Base.findPic(Constant.login_2,2000);
    	if(list.size() > 0){
    		//点击登录
    		mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
    		list = Base.findPic(Constant.login_3,5000);
			if(list.size() > 0){
				mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
			}
			list = Base.findPic(Constant.login_e,5000);
			if(list.size() > 0){//如果账号密码不正确
				clearInput2();
				login();//重新登录
			}
    		list = Base.findPic(Constant.login_success,30000);
    		if(list.size() > 0){
    			Base.addLog("登录成功..");
    			list = Base.findPic(Constant.do_ok,3000);
    			if(list.size() > 0){
    				mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
    				robot.delay(500);
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
    		mouse.mouseClick(list.get(0).getX() + 90, list.get(0).getY() + 13, true);
    		robot.sendString(user[0]);
    	}
    	list = Base.findPic(Constant.login_p,2000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX() + 90, list.get(0).getY() + 13, true);
    		robot.sendString(user[1]);
    	}
    }
    
    /**
     * 清空输入框
     */
    public void clearInput2(){
    	robot.delay(200);
    	List<CoordBean> list = Base.findPic(Constant.login_u,2000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX() + 90, list.get(0).getY() + 13,true);
    		for(int i=0,j=10;i<j;i++){
    			robot.delay(100);
        		press.keyPress(press.DELETE);	
    		}
    		for(int i=0,j=10;i<j;i++){
    			robot.delay(100);
        		press.keyPress(press.BACKSPACE);	
    		}
    	}
    }
    /**
     * 清空输入框
     */
    public void clearInput(){
    	robot.delay(200);
    	List<CoordBean> list = Base.findPic(Constant.login_u,2000);
    	if(list.size() > 0){
    		mouse.mouseMoveTo(list.get(0).getX() + 90, list.get(0).getY() + 13);
    		robot.delay(200);
    		Base.doubleClick();
    		robot.delay(200);
    		press.keyPress(press.DELETE);
    	}
    }
    
    /**
     * 打开游戏app
     */
    public void openGameApp(){
    	//1分钟内循环查找游戏app。如果进入了桌面 肯定能找到
    	List<CoordBean> list = Base.findPic(Constant.app_game,60000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}
    	robot.delay(500);
    }
    
    /**
     * 游戏更新
     */
    public void update(){
    	List<CoordBean> list = Base.findPic(Constant.login_1,20000);
    	if(list.size() > 0){
    		return;
    	}
    	//20秒内循环查是否需要更新。
    	list = Base.findPic(Constant.update_1,30000);
    	if(list.size() > 0){
    		list = Base.findPic(Constant.update_2);
    		if(list.size() > 0){
    			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    			//60秒内循环查是否已经下载完成更新包，跳转到安装界面。
    	    	list = Base.findPic(Constant.update_3,80000);
    	    	if(list.size() > 0){
    	    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	    		list = Base.findPic(Constant.update_4,2000);
    	    		if(list.size() > 0){
    	    			Base.addLog("更新完毕..");
    	    			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	    		}
    	    	}
    		}
    	}
    }
    
    /**
     * 打开安卓模拟器
     */
    public void openApp(){
    	WB.threadPool.execute(WB.openGame);
    }
    
    public Layout() {
    	JButton btnNewButton = new JButton("\u9886\u53D6\u85CF\u5B9D\u56FE");
    	btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	wb();
            }
        });
    	
    	JButton button = new JButton("\u5F00\u59CB\u6316\u5B9D");
    	button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	do_all();
            }
        });
    	GroupLayout groupLayout = new GroupLayout(getContentPane());
    	groupLayout.setHorizontalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
    			.addGroup(groupLayout.createSequentialGroup()
    				.addGap(123)
    				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
    					.addComponent(button)
    					.addComponent(btnNewButton))
    				.addContainerGap(218, Short.MAX_VALUE))
    	);
    	groupLayout.setVerticalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
    			.addGroup(groupLayout.createSequentialGroup()
    				.addGap(56)
    				.addComponent(btnNewButton)
    				.addGap(18)
    				.addComponent(button)
    				.addContainerGap(142, Short.MAX_VALUE))
    	);
    	getContentPane().setLayout(groupLayout);
    }
}
