package com.y_ghelp.test.demo.my.wb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

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
    	Base.resetUsers();
    	//打开模拟器
    	openApp();
    	robot.delay(5000);
    	active();
        //进入游戏
        openGameApp();
        //检查是否需要更新
        update();
        boolean flag = true;
        do{
        	//登录
        	flag = login();
        	if(flag){
        		//开始挖宝
                wb();
        	}
        }while(flag);
        Base.addLog("结束..");
    }
    
    public void execute_huangjia(){
    	//打开模拟器
    	openApp();
    	robot.delay(5000);
    	active();
        //进入游戏
        openGameApp();
        //检查是否需要更新
        update();
        boolean flag = true;
        do{
        	//登录
        	flag = login();
        	if(flag){
        		//开始皇家
        		huangjia();
        	}
        }while(flag);
        Base.addLog("结束皇家..");
    }
    
    /**
     * 出征宝宝
     */
    private void chuzhengbaobao(int x,int y){
    	//66 * 58
    	robot.delay(200);
    	mouse.mouseClick(x + 66, y - 58, true);
    	robot.delay(200);
    	chuzhen_1();
    	robot.delay(200);
    	mouse.mouseClick(x + 66, y - 58, true);
    	//134 * 58
//    	robot.delay(200);
//    	mouse.mouseClick(x + 134, y - 58, true);
//    	robot.delay(200);
//    	chuzhen_1();
    }
    
    private void chuzhen_1(){
    	robot.delay(200);
    	List<CoordBean> list = Base.findStrE("解体",  "d9d8d7-262728", 1, 0);//解体
    	if(list.size() > 0){
    		Base.addLog("解体..");
    		return;
    	}
    	robot.delay(200);
    	list = Base.findStrE("合体",  "d9d8d7-262728", 1, 0);//合体
    	if(list.size() > 0){
    		Base.addLog("合体..");
    		mouse.mouseClick(list.get(0).getX() + 8, list.get(0).getY() + 8, true);
    		robot.delay(200);
    		return;
    	}
    	list = Base.findStrE("出征",  "d9d8d7-262728", 1, 0);//出征
    	if(list.size() > 0){
    		Base.addLog("出征..");
    		mouse.mouseClick(list.get(0).getX() + 8, list.get(0).getY() + 8, true);
    		robot.delay(200);
    	}
    }
    
    private void active(){
    	int hwnd = window.findWindow(0, null, Constant.appName);
        if(hwnd > 0){
        	Base.addLog("模拟器句柄：" + hwnd);
            window.moveWindow(hwnd, 0, 0);
            window.setWindowActivate(hwnd); //激活窗口
        }
    }
    
    public void huangjia(){
    	login_after();
		new AutoHuangJia().execute();
		boolean a = new Layout().switchUser();
    	if(a){
    		huangjia();
    	}
    }
    
    private void login_after() {
    	active();
    	//465 233
        List<CoordBean> list = Base.findPic(Constant.login_success,20000);
        if(list.size() > 0){
        	Base.addLog("登录成功");
        }
        
        list = Base.findPic(Constant.do_ok,3000);
		if(list.size() > 0){
			mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
			robot.delay(500);
		}
    	//收起任务栏
    	list = Base.findPic(Constant.go_index_1 + "|" + Constant.go_index_1_1,10000);
    	int x = -1;
    	int y = -1;
    	if(list.size() > 0){
    		x = list.get(0).getX();
    		y = list.get(0).getY();
    		mouse.mouseClick(x, y, true);
    		robot.delay(1000);
    	}else{
    		Base.addLog("没有找到收起人物栏的图标");
    		return;
    	}
    	//出征宝宝
    	chuzhengbaobao(x, y);
    	Base.addLog("判断是否在雷鸣领奖处..");
    	//判断是否在雷鸣领奖处
    	list = Base.findStrE("罗兰", "29ce21-2a3121", 0.9, 0,2000);
    	if(list.size() > 0){
    		Base.addLog("在雷鸣领奖处..");
    	}else{
    		list = Base.findStrE("德兰", "0bb10b-0b4e0c", 0.9, 0,1000);
    		if(list.size() > 0){//13 51
    			mouse.mouseClick(list.get(0).getX() + 13, list.get(0).getY() + 51, true);
    			robot.delay(1500);
    		}else{
    			list = Base.findPic(Constant.huanbaoliangongshagn01,1000);
    			if(list.size() > 0){
    				int sx = 322;
    	    		int sy = 165;
    				mouse.mouseClick(sx, sy, true);
        			robot.delay(1500);
    			}
    		}
    		//判断是否在环保练功场（未成神）
    		list = Base.findPic(Constant.go_index,20000);
    		if(list.size() > 0){
    			mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
    		}
    	}
		robot.delay(500);
    }
    
    
    public void wb(){
    	login_after();
    	//领藏宝图
    	lcbt();
    	//处理挖宝（山贼、宝树等）
    	do_all(null);
    	//切换账号
    	boolean a = switchUser();
    	if(a){
    		wb();
    	}
    }
    

    
    /**
     * 判断是否有小号
     * @return
     */
    private List<CoordBean> has_xiaohao(){
    	List<CoordBean> list = Base.getWB_CLOSE(5000);//272 383
    	if(list.size() > 0){
    		robot.delay(500);
    		//点击切换小号
    		list = Base.findPic(Constant.xiaohao_qiehuan_1 + "|" + Constant.xiaohao_qiehuan_2,3000);
    		if(list.size() > 0){
    			mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);	
    		}else{
    			mouse.mouseClick(list.get(0).getX() - 272, list.get(0).getY() + 383, true);
    		}
    	}else{
    		Base.addLog("发生错误未能找到wb_close..");
    		return Base.xiaohao;
    	}
    	if(Base.xiaohao.size() > 0){
    		Base.addLog("小号还未处理完.." + Base.xiaohao);
    		return Base.xiaohao;
    	}
    	robot.delay(3000);
    	list = Base.findPic(Constant.xiaohao_close,2000);
    	if(list.size() > 0){
    		String[] user = Base.listUsers.getLast();
    		if(user[2].equals("1") && !Base.listUserXiaoHao.contains(user[0])){//有小号，并且小号没有处理过
    			Base.listUserXiaoHao.add(user[0]);
    			CoordBean xiaohao1 = new CoordBean();//266*89
    			xiaohao1.setId(1);
        		xiaohao1.setX(list.get(0).getX() - 266);
        		xiaohao1.setY(list.get(0).getY() + 89);
        		
        		CoordBean xiaohao2 = new CoordBean();//153*89
        		xiaohao2.setId(2);
        		xiaohao2.setX(list.get(0).getX() - 153);
        		xiaohao2.setY(list.get(0).getY() + 89);
        		
        		CoordBean xiaohao3 = new CoordBean();//41*89
        		xiaohao3.setId(3);
        		xiaohao3.setX(list.get(0).getX() - 41);
        		xiaohao3.setY(list.get(0).getY() + 89);
        		
        		CoordBean xiaohao4 = new CoordBean();//376*178
        		xiaohao4.setId(4);
        		xiaohao4.setX(list.get(0).getX() - 376);
        		xiaohao4.setY(list.get(0).getY() + 178);
        		
        		CoordBean xiaohao5 = new CoordBean();//266*178
        		xiaohao5.setId(5);
        		xiaohao5.setX(list.get(0).getX() - 266);
        		xiaohao5.setY(list.get(0).getY() + 178);
        		Base.xiaohao.add(xiaohao1);
        		Base.xiaohao.add(xiaohao2);
        		Base.xiaohao.add(xiaohao3);
        		Base.xiaohao.add(xiaohao4);
        		Base.xiaohao.add(xiaohao5);
    		}
    	}
    	return Base.xiaohao;
    }
    
    public void clearData(){
    	WB.die_count = 0;
    }
    
    public boolean switchUser(){
    	Base.addLog("开始切换账号..");
    	clearData();//清空死亡次数
    	robot.delay(2000);
    	//先退出当前账号
    	//按K然后找到设置按钮
    	press.keyPress(press.K);
    	robot.delay(1000);
    	//系统设置坐标 776*438
    	mouse.mouseClick(776, 438, true);
    	robot.delay(500);
    	
    	//判断是否有小号
    	List<CoordBean> xiaohao = has_xiaohao();
    	robot.delay(1500);
    	//210 247
		List<CoordBean> list = Base.findPic(Constant.xiaohao_close,1500);
    	if(xiaohao.size() > 0){
    		Base.addLog("有小号.." + xiaohao);
    		robot.delay(500);
    		if(list.size() <= 0){
    			Base.addLog("找不到" + Constant.xiaohao_close);
    			return false;
    		}
    		if(xiaohao.get(0).getId() == 1){
    			mouse.mouseClick(list.get(0).getX() - 266, list.get(0).getY() + 89,true);
    		}else if(xiaohao.get(0).getId() == 2){
    			mouse.mouseClick(list.get(0).getX() - 153, list.get(0).getY() + 89,true);
    		}else if(xiaohao.get(0).getId() == 3){
    			mouse.mouseClick(list.get(0).getX() - 41, list.get(0).getY() + 89,true);
    		}else if(xiaohao.get(0).getId() == 4){
    			mouse.mouseClick(list.get(0).getX() - 376, list.get(0).getY() + 178,true);
    		}else if(xiaohao.get(0).getId() == 5){
    			mouse.mouseClick(list.get(0).getX() - 266, list.get(0).getY() + 178,true);
    		}
    		Base.addLog("移除.." + xiaohao + "中的：" + xiaohao.get(0));
    		Base.xiaohao.removeFirst();
    		robot.delay(500);
        	if(list.size() > 0){
        		Base.addLog("点击切换按钮");
        		robot.delay(500);
        		mouse.mouseClick(list.get(0).getX() - 210, list.get(0).getY() + 247,true);
        	}
        	robot.delay(5000);
        	return true;
    	}else{
    		//无小号
    		Base.addLog("无小号..");
    		if(list.size() > 0){
        		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(),true);
        	}
        	list = Base.findPic(Constant.logout +"|" + Constant.logout_1,5000);
    		if(list.size() > 0){
    			Base.addLog("找到图片logout");
    			mouse.mouseClick(list.get(0).getX() + 15, list.get(0).getY() + 12, true);
    		}else{
    			list = Base.getWB_CLOSE(5000);
    			//272 383//切换角色
    			//126 383//退出游戏
    			if(list.size() > 0){
    				list = Base.findStrE("退出","84b2a2-47484a", 0.9, 0);
    				if(list.size() > 0){
    					Base.addLog("找到文字退出");
    					mouse.mouseClick(list.get(0).getX() + 15, list.get(0).getY() + 12, true);
    				}else{
    					mouse.mouseClick(list.get(0).getX() - 126, list.get(0).getY() + 383, true);
    				}
    			}
    		}
    		list = Base.findPic(Constant.logout_ok,5000);
    		if(list.size() > 0){
    			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);//回到登录页面
    			robot.delay(2000);
    		}
    		return false;
    	}
    }
    
    /**
     * 返回是否寻路（打开藏宝图之后是否寻路）
     * @return
     */
    public boolean open_cbt(List<CoordBean> list){
    	press.keyPress(press.Z);
    	robot.delay(200);
    	open_beibao();
    	if(null != list && list.size() > 0){
			Base.addLog("点击藏宝图...");
			robot.delay(200);
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		robot.delay(500);
			Base.addLog("使用藏宝图...");
			//75 * 43
			mouse.mouseClick(list.get(0).getX() - 75, list.get(0).getY() + 43, true);
			robot.delay(500);
			list = Base.findPic(Constant.beibao_close,1500);
			if(list.size() > 0){
				mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
				robot.delay(800);
			}
			list = Base.findPic(Constant.xunlu,1000);
			if(list.size() > 0){
				return true;
			}
    	}else{
			Base.addLog("没有藏宝图了...");
    	}
    	return false;
    }
    
    private List<CoordBean> open_beibao(){
    	Base.addLog("wb open_cbt..");
    	//打开背包
    	Base.addLog("打开背包...");
    	press.keyPress(press.I);
    	robot.delay(800);
    	//打开任务栏 228*42 x-228 y+42
    	List<CoordBean> list = Base.findPic(Constant.beibao_close,2000);
    	if(list.size() > 0){
    		Base.addLog("打开任务栏...");
    		mouse.mouseClick(list.get(0).getX() - 228, list.get(0).getY() + 42, true);
    	}
    	robot.delay(500);
    	return list;
    }
    
    /**
     * 关闭背包
     */
    private void close_beibao(){
    	List<CoordBean> list = Base.findPic(Constant.beibao_close,2000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);
    		robot.delay(500);
    	}
    }
    /**
     * 查找藏宝图
     * @return
     */
    public List<CoordBean> find_cbt(){
    	List<CoordBean> list = open_beibao();
    	if(list.size() <= 0){
    		Base.addLog("find_cbt open_beibao return null");
    		return null;
    	}
    	int b_x = list.get(0).getX();
    	int b_y = list.get(0).getY();
    	//查找藏宝图
    	String cangbaotu = Constant.cangbaotu_2 + "|" + Constant.cangbaotu_3
    			+ "|" + Constant.cangbaotu_4+ "|" + Constant.cangbaotu_5
    			+ "|" + Constant.cangbaotu_6
    			+ "|" + Constant.cangbaotu_7
    			+ "|" + Constant.cangbaotu_8;
//    	String color = "a22513|aa2c15|9d2b11|ac1f10|a42a16|ac2210|a62918|a22a16";
//    	int[] caobaotu = Base.color.findColor(440, 219, 736, 462, color, 0.9, 0);
//    	CoordBean coordBean = new CoordBean();
    	
    	//
//    	XY=Plugin.Color.FindMutiColor(0,0,1280,1024,"162AA4","-6|6|3A5C6A,-19|10|0D257B,-17|-5|3A5B6B",1)
//    			dim MyArray
//    			MyArray = Split(XY, "|")
//    			X = CInt(MyArray(0)): Y = CInt(MyArray(1))
    	list = Base.findPic(cangbaotu,412,144,756,551,0.8);
    	boolean flag = true;
    	do{
    		Base.addLog("关闭背包...");
    		mouse.mouseClick(b_x + 8, b_y + 8, true);
    		robot.delay(500);
    		List<CoordBean> list1 = Base.findPic(Constant.beibao_close);
    		if(list1.size() <= 0){
    			flag = false;
    		}else{
    			b_x = list1.get(0).getX();
    			b_y = list1.get(0).getY();
    		}
    	}while(flag);//防止背包关不掉
    	
//    	if(caobaotu[0] != -1){
//    		list.clear();
//    		coordBean.setX(caobaotu[0]);
//        	coordBean.setY(caobaotu[1]);
//        	list.add(coordBean);
//    	}else{
//    		Base.addLog("关闭背包...");
//    		mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);
//    		list.clear();
//    	}
    	return list;

    }
    
    
    //do_1 处理山贼
    public void do_1(int x,int y){
    	mouse.mouseClick(x, y, true);
    	Base.addLog("处理山贼...");
    	int i = 0;//15次
    	boolean flag = true;
    	do{
    		do_0();
    		press.keyPress(press.F3);
    		if(i == 2){
    			press.keyPress(press.F2);
    			robot.delay(500);
    		}
    		i++;
    		Base.addLog("处理山贼... i " + i);
    		flag = Base.findPic(getBaoXiangImg()).size() <= 0;
    		if(flag){
    			if(i >= 15){
    				flag = false;
    			}
    		}else{
    			Base.screenImage("宝箱");
    		}
    	}while(flag);
    	Base.addLog("处理山贼结束...");
    }
    
    private void do_0(){
    	robot.delay(200);
		press.keyPress(press.F3);
		robot.delay(1000);
		press.keyPress(press.F1);
		robot.delay(1000);
		press.keyPress(press.F3);
		robot.delay(1000);
    }
    
    //do_2 处理盗墓贼
    public void do_2(int x,int y){
    	mouse.mouseClick(x, y, true);
    	Base.addLog("处理盗墓贼...");
    	List<CoordBean> list = Base.findPic(Constant.do_2_1,200);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}
    	int i = 0;//140次
    	boolean flag = true;
    	press.keyPress(press.F3);
		robot.delay(200);
    	do{
    		press.keyPress(press.SPACE);
    		robot.delay(200);
    		if(i == 2){
    			press.keyPress(press.F2);
    			robot.delay(500);
    		}
    		list = Base.findPic(Constant.do_2_1);
        	if(list.size() > 0){
        		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	}
    		i++;
    		Base.addLog("处理盗墓贼... i " + i);
    		flag = Base.findPic(getBaoXiangImg()).size() <= 0;
    		if(flag){
    			if(i >= 140){
    				flag = false;
    			}
    		}else{
    			Base.screenImage("宝箱");
    		}
    	}while(flag);
    	Base.addLog("处理盗墓贼结束...");
    }
    
    public String getBaoXiangImg(){
    	return Constant.do_baoxiang + "|" 
    			+ Constant.do_baoxiang_1 + "|" 
    			+ Constant.do_baoxiang_2 + "|"
    			+ Constant.do_baoxiang_5 + "|" 
    			+ Constant.do_baoxiang_6 + "|" 
    			+ Constant.do_baoxiang_7 + "|"
    			+ Constant.do_baoxiang_8 + "|"
    			+ Constant.do_baoxiang_9 + "|"
    			+ Constant.do_baoxiang_10 + "|"
    			+ Constant.do_baoxiang_11 + "|" 
    			+ Constant.do_baoxiang_12;
    }
    
    //do_3 处理宝树
    public void do_3(int x,int y){
    	mouse.mouseClick(x, y, true);
    	Base.addLog("处理宝树...");
    	int i = 0;//15次
    	boolean flag = true;
    	do{
    		do_0();
    		if(i == 2){
    			press.keyPress(press.F2);
    			robot.delay(500);
    		}
    		press.keyPress(press.SPACE);
    		i++;
    		Base.addLog("处理宝树... i " + i);
			if(i >= 15){
				flag = false;
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
    public void do_all(List<CoordBean> list){
    	active();
    	if(null == list){
    		list = find_cbt();//找到藏宝图的话.
    	}
    	if(null == list || list.size() <= 0){
    		Base.addLog("挖宝完毕");
    		//领取宝箱
    		baoxiang_click();
    		return;
    	}
    	//检测复活
    	List<CoordBean> die_list = new ArrayList<>();
    	boolean die = WB.die(die_list);
    	if(die) {
    		robot.delay(500);
			//如果人物死亡，执行复活
    		WB.threadPool.execute(WB.checkDie);
    		robot.delay(1000);
    		do{
    			robot.delay(1000);
    			Base.addLog("等待复活...");
    		}while(WB.die);
    		//判断是否已经复活过5次
    		if(WB.die_count >=5){
    			Base.addLog("复活次数大于等于5次直接返回...");
    			return;
    		}
    	}
    	//清理怪物，避免挖宝被打断
    	clear_gw();
    	//点击藏宝图，自动寻路挖宝
    	boolean xunlu = open_cbt(list);
    	if(xunlu){
    		Base.xunlu();//寻路
    	}else{
    		wbz();
    		list =  null;
    	}
    	robot.delay(500);
    	do_all(list);//继续挖宝
    }
    
    public void clear_gw(){
    	//清理小怪,避免打断挖宝
        robot.delay(200);
    	for(int i = 0,j=6;i<j;i++){
    		Base.addLog("释放技能F3...");
        	press.keyPress(press.F3);
        	robot.delay(800);
    	}
    }
    
    public void wbz(){
    	Base.addLog("使用藏宝图中..");
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
    		boolean flag = true;
    		int i = 0;
    		boolean m = true;
    		do{
    			list = Base.findPic(getBaoXiangImg(),5000);
        		//25 * 65
            	if(list.size() > 0 && i <=60){
            		if(m){//先移动到宝箱周围,防止过远点不到
            			m = false;
            			mouse.mouseClick(list.get(0).getX() + 125,list.get(0).getY() + 55, true);
            		}
            		i++;
            		Base.addLog("点击宝箱...");
            		mouse.mouseMoveTo(list.get(0).getX() + 30,list.get(0).getY() + 55);
            		robot.delay(200);
            		Base.doubleClick();
            		robot.delay(500);
            	}else{
            		flag = false;
            	}
    		}while(flag);
    	}else{
			Base.addLog("没有找到确定框可能直接出现经验...");
    	}
    }
    
    /**
     * 挖宝完毕之后点击宝箱
     */
    private void baoxiang_click(){
    	List<CoordBean> list = Base.findPic(Constant.wb_0,10000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	robot.delay(2000);
        	list = Base.findPic(Constant.wb_4,140,219,288,260,3000);//是否已经挖过宝了
        	if(list.size() > 0){
        		//115
        		mouse.mouseClick(list.get(0).getX() + 115, list.get(0).getY(), true);
        		robot.delay(500);
        		list = Base.findPic(Constant.baoxiang_click,3000);
        		if(list.size() > 0){
        			mouse.mouseClick(list.get(0).getX() + 15, list.get(0).getY() + 15, true);
            		robot.delay(500);
            		Base.close_wb_page();
        		}else{
        			Base.close_wb_page();
        		}
        	}else{
        		Base.close_wb_page();
        	}
    	}
    }
    
    /**
     * 领藏宝图
     */
    public void lcbt(){
    	Base.addLog("领藏宝图开始..");
    	robot.delay(3000);
    	List<CoordBean> list = Base.findPic(Constant.wb_0,12000);
    	if(list.size() > 0){
    		robot.delay(2000);
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	robot.delay(2000);
        	list = Base.findPic(Constant.wb_4,140,219,288,260,3000);//是否已经挖过宝了
        	if(list.size() > 0){
        		Base.addLog("该账号已经挖过宝..");
        		Base.close_wb_page();
        		return;
        	}
        	list = Base.findPic(Constant.wb_1,140,219,288,260,5000);
        	if(list.size() > 0){
        		//x+371,y-8
        		robot.delay(200);
        		mouse.mouseMoveTo(list.get(0).getX() + 366, list.get(0).getY() - 9);
        		robot.delay(500);
        		mouse.mouseClick(list.get(0).getX() + 366, list.get(0).getY() - 9, true);
        		robot.delay(500);
        		//判断当前是在雷鸣还是在神界
        		list = Base.findStrE("确定","c7bd97-383e38", 1, 0,2000);
    			if(list.size() > 0){
    				mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);
    				robot.delay(500);
    				sj_to_lm();
    			}else{
    				if(Base.is_xunlu()) {
    					Base.addLog("当前人物在雷鸣");
        				wb_2();	
    				}else {
    					Base.addLog("程序未能处理,当前人物未能到达雷鸣");
    					hallowmas_to_lm();
    				}
    			}
        	}else{
        		Base.close_wb_page();
        	}
    	}else{
    		Base.addLog("没有找到wb_0");
    	}
    	Base.addLog("领藏宝图结束..");
    }
    
    private void sj_to_lm() {
    	Base.addLog("神界去雷鸣开始..");
    	//检查是否已经到了神界
    	List<CoordBean> list = Base.findStrE("神界", 
				"bbbb06-3c3c07|bbbb13-3c3c13|bfbf13-404013", 0.9, 0,10000);
		if(list.size() > 0){
			Base.addLog("当前人物在神界");
			press.keyPressTime(press.W, 380);
			robot.delay(500);
			press.keyPress(press.SPACE);
			robot.delay(800);
			boolean a = Base.go_to_lm();
			if(a){
				lcbt();
			}else{
				Base.addLog("没有找到传送到雷鸣的对话框");
			}
		}else{
			Base.addLog("没有找到神界的传送师");
			hallowmas_to_lm();
		}
		Base.addLog("神界去雷鸣结束..");
    }
    
    private void hallowmas_to_lm() {
    	Base.addLog("万圣节期间,去雷鸣开始..");
    	//打开背包，关闭背包
    	open_beibao();
    	robot.delay(1000);
    	close_beibao();
    	List<CoordBean> list = Base.findStrE("传送", "30eb37-311437", 0.8, 0);
    	if(list.size() > 0){
    		Base.addLog("找到传送门...");
    		mouse.mouseClick(list.get(0).getX() + 15, list.get(0).getY() + 50, true);
    	}else{
    		Base.addLog("直接点击坐标(180, 213)...");
    		mouse.mouseClick(180, 213, true);
    	}
    	robot.delay(1000);
    	boolean a = Base.go_to_lm();
		if(a){
			lcbt();
		}else{
			Base.addLog("没有找到传送到雷鸣的对话框");
		}
		Base.addLog("万圣节期间,去雷鸣结束..");
    }

    
	private void wb_2(){
		Base.xunlu();
		List<CoordBean> list = Base.findPic(Constant.wb_2,145,318,269,365,10000);
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
			
			i = 0;
			flag = true;
			do{
				i++;
				list = Base.findPic(Constant.cangbaotu_close);
				if(list.size() > 0){
					flag = true;
					mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);
					robot.delay(500);
				}
				if(i > 10 || list.size() <= 0){
					flag = false;
				}
			}while(flag);
			robot.delay(500);
			list = open_beibao();
			int b_x = list.get(0).getX();
	    	int b_y = list.get(0).getY();
	    	mouse.mouseClick(b_x + 5, b_y + 5, true);
			robot.delay(500);
		}else {
			Base.addLog("未能到达领取藏宝图的NPC处");
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
            	if(Base.listUsers.size() > 0){
            		//账号没有处理过
            		if(!existsUser(userInfo[0],Base.listUsers)){
            			return userInfo;
            		}
            	}else{
            		return userInfo;
            	}
            }else{
                Base.addLog("账号配置错误..");    
            }
        }
        return null;
    }
    
    /**
     * 判断用户是否存在
     * @param userName
     * @param yUser
     * @return
     */
    public boolean existsUser(String userName,LinkedList<String[]> user){
    	for(String[] item : user){
    		if(userName.equals(item[0])){
    			return true;
    		}
    	}
    	return false;
    }
    
    
    /**
     * 登录
     */
    public boolean login(){
    	Base.addLog("login start ..");
    	List<CoordBean> list = Base.findPic(Constant.login_u+"|"+Constant.login_p,2000);//账号输入框
    	if(null == list || list.size() == 0){
    		list = Base.findPic(Constant.login_1,3000);
        	if(list.size() > 0){
        		robot.delay(3000);
        		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	}else{
        		list = Base.findPic(Constant.update_4,2000);
        		if(list.size() > 0){
        			Base.addLog("找到更新内容界面，关闭界面然后重新登录");
        			robot.delay(200);
        			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        			robot.delay(200);
        			return login();
        		}
        		Base.addLog("没有找到登录按钮login_1");
        		Base.screenImage("login_1");
        		return false;
        	}
    	}
    	String[] user = loadUserInfo();
    	if(null == user){
    		Base.addLog("没有找到用户，可能已经全部处理过了..");
    		return false;
    	}
    	Base.addLog("开始登录：" + user);
    	input_user(user);
    	robot.delay(200);
    	list = Base.findPic(Constant.login_2,2000);
    	if(list.size() > 0){
    		//点击登录
    		mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
			list = Base.findPic(Constant.login_e,5000);
			if(list.size() > 0){//如果账号密码不正确
				clearInput2();
				return login();//重新登录
			}
			list = Base.findPic(Constant.login_3,10000);
			if(list.size() > 0){
				mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
			}
    		list = Base.findPic(Constant.login_success,30000);
    		if(list.size() > 0){
    			Base.addLog("登录成功..");
    			//登录成功才添加进来
    			Base.listUsers.add(user);
    		}else{
        		Base.addLog("没有找到login_success");
        		Base.screenImage("login_success");
        		list = Base.findStrE("确定","c7bd97-383e38", 1, 0,2000);
        		if(list.size() > 0){
        			Base.addLog("找到确定按钮，点击确定，然后重新登录");
        			mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
        			return login();
        		}else{
        			list = Base.findPic(Constant.login_u+"|"+Constant.login_1,2000);
        			if(list.size() > 0){
        				Base.addLog("还停留在输入账号密码界面，重新登录");
        				return login();
        			}
        			return false;
        		}
        	}
    	}else{
    		Base.addLog("没有找到login_2");
    		Base.screenImage("login_2");
    		return false;
    	}
    	return true;
    }
    
    /**
     * 输入账号密码
     * @param user
     */
    public void input_user(String[] user){
    	clearInput();
    	robot.delay(500);
    	List<CoordBean> list = Base.findPic(Constant.login_u,2000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX() + 90, list.get(0).getY() + 13, true);
    		robot.delay(500);
    		robot.sendString(user[0]);
    	}
    	list = Base.findPic(Constant.login_p,2000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX() + 90, list.get(0).getY() + 13, true);
    		robot.delay(500);
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
    		robot.delay(10000);
    		Base.addLog("找到"+ Constant.app_game +"点击..");
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		robot.delay(500);
    		list = Base.findPic(Constant.app_game,1000);
    		if(list.size() > 0){
    			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		}
    		list = Base.findPic(Constant.app_game,1000);
    		if(list.size() > 0){
    			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		}
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
    	JButton btnNewButton = new JButton("开始登录挖宝");
    	btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	active();
            	Base.resetUsers();
                boolean flag = true;
                do{
                	//登录
                	flag = login();
                	if(flag){
                		//开始挖宝
                        wb();
                	}
                }while(flag);
                Base.addLog("结束..");
            }
        });
    	
    	JButton button = new JButton("开始皇家");
    	button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	active();
            	Base.resetUsers();
                boolean flag = true;
                do{
                	//登录
                	flag = login();
                	if(flag){
                		//开始皇家
                		huangjia();
                	}
                }while(flag);
                Base.addLog("结束..");
            }
        });
    	
    	JButton button_1 = new JButton("\u5207\u6362\u8D26\u53F7");
    	button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	active();
            	switchUser();
            }
        });
    	
    	JButton btn_reset = new JButton("\u91CD\u7F6E");
    	btn_reset.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {
    			Base.resetUsers();
    		}
    	});
    	
    	JButton btn_exit = new JButton("\u9000\u51FA");
    	btn_exit.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			SysHotKey.destroy();
    		}
    	});
    	GroupLayout groupLayout = new GroupLayout(getContentPane());
    	groupLayout.setHorizontalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
    			.addGroup(groupLayout.createSequentialGroup()
    				.addGap(123)
    				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
    					.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
    					.addGroup(groupLayout.createSequentialGroup()
    						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
    							.addComponent(btnNewButton)
    							.addComponent(button))
    						.addGap(18)
    						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
    							.addComponent(btn_exit, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
    							.addComponent(btn_reset, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))))
    				.addContainerGap(107, Short.MAX_VALUE))
    	);
    	groupLayout.setVerticalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
    			.addGroup(groupLayout.createSequentialGroup()
    				.addGap(56)
    				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
    					.addComponent(btnNewButton)
    					.addComponent(btn_reset))
    				.addGap(18)
    				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
    					.addComponent(button)
    					.addComponent(btn_exit))
    				.addGap(18)
    				.addComponent(button_1)
    				.addContainerGap(101, Short.MAX_VALUE))
    	);
    	getContentPane().setLayout(groupLayout);
    }
    
    public void initJob(WB wb){
        try {
        	Base.addLog("initJob");
            //1.创建Scheduler的工厂
            SchedulerFactory sf = new StdSchedulerFactory();
            //2.从工厂中获取调度器实例
            Scheduler scheduler = sf.getScheduler();

            //3.创建JobDetail
            JobDetail job = JobBuilder.newJob(WBJob.class)
                    .withDescription("this is a ram job") //job的描述
                    .withIdentity("ramJob", "ramGroup") //job 的name和group
                    .build();
            job.getJobDataMap().put("wbjob", wb);  

            //任务运行的时间，SimpleSchedle类型触发器有效
            long time=  System.currentTimeMillis() + 10*1000L; //10秒后启动任务
            Date statTime = new Date(time);

            //4.创建Trigger
                //使用SimpleScheduleBuilder或者CronScheduleBuilder
            Trigger t = TriggerBuilder.newTrigger()
                        .withDescription("")
                        .withIdentity("ramTrigger", "ramTriggerGroup")
                        .startAt(statTime)  //10秒后启动任务 0 0 11 * * ?
                        .withSchedule(CronScheduleBuilder.cronSchedule(MYConfig.getInstance().getConfig("quartz_con_wb").toString())) //60秒执行一次 --0/60 * * * * ? --0 0 0,09,12,22 * * ? --"0 0 0,09,12 * * ?"
                        .build();

            //5.注册任务和定时器
            scheduler.scheduleJob(job, t);

            //6.启动 调度器
            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
            Base.addLog(e.getMessage());
        }
    }
}
