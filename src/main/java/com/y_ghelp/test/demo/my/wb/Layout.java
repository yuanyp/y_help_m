package com.y_ghelp.test.demo.my.wb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.apache.commons.io.FileUtils;
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
import com.xnx3.microsoft.Sleep;
import com.xnx3.microsoft.Window;
import com.xnx3.robot.Robot;
import com.xnx3.robot.support.CoordBean;
import com.y_ghelp.test.demo.config.MYConfig;
import com.y_ghelp.test.demo.my.Base;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Layout extends JFrame{
    Com com = Base.com;
    Window window = Base.window;
    Mouse mouse = Base.mouse;
    Press press = Base.press;
    Color color = Base.color;
    Robot robot = Base.robot;
    FindPic findPic = Base.findPic;
    com.xnx3.microsoft.File file = Base.file;
    Sleep sleep = Base.sleep;
    
    JButton btnHangUp;
    JButton btnHangUp_2;
    AutoHangUp autoHangUp;
    AutoResurgenceThread autoResurgenceThread;
    
    public void execute(){
    	Base.resetUsers();
    	//打开模拟器
    	openApp();
    	sleep.sleep(5000);
    	active();
        //进入游戏
        openGameApp();
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
    
    /**
     * 出征宝宝
     */
    private void chuzhengbaobao(int x,int y){
    	//66 * 58
    	sleep.sleep(200);
    	mouse.mouseClick(x + 66, y - 58, true);
    	sleep.sleep(200);
    	chuzhen_1();
    	sleep.sleep(200);
    	mouse.mouseClick(x + 66, y - 58, true);
    	//134 * 58
//    	sleep.sleep(200);
//    	mouse.mouseClick(x + 134, y - 58, true);
//    	sleep.sleep(200);
//    	chuzhen_1();
    }
    
	public AutoResurgenceThread setAutoResurgenceThread(int zIndex2) {
		this.autoResurgenceThread = new AutoResurgenceThread();
		this.autoResurgenceThread.zIndex2 = zIndex2;
		return this.autoResurgenceThread;
	}
    
    private void chuzhen_1(){
    	sleep.sleep(200);
    	List<CoordBean> list = Base.findStrE("解体",  "d9d8d7-262728", 1, 0);//解体
    	if(list.size() > 0){
    		Base.addLog("解体..");
    		return;
    	}
    	sleep.sleep(200);
    	list = Base.findStrE("合体",  "d9d8d7-262728", 1, 0);//合体
    	if(list.size() > 0){
    		Base.addLog("合体..");
    		mouse.mouseClick(list.get(0).getX() + 8, list.get(0).getY() + 8, true);
    		sleep.sleep(200);
    		return;
    	}
    	list = Base.findStrE("出征",  "d9d8d7-262728", 1, 0);//出征
    	if(list.size() > 0){
    		Base.addLog("出征..");
    		mouse.mouseClick(list.get(0).getX() + 8, list.get(0).getY() + 8, true);
    		sleep.sleep(200);
    	}
    }
    
    private int active(){
    	int hwnd = window.findWindow(0, null, Base.getAppName());
        if(hwnd > 0){
        	Base.addLog("模拟器句柄：" + hwnd);
            window.moveWindow(hwnd, 0, 0);
            window.setWindowActivate(hwnd); //激活窗口
        }
        return hwnd;
    }
    
    /**
     * 活动关闭页面的关闭按钮图片
     * @return
     */
    private String getHDClose(){
    	String imgs = MYConfig.getInstance().getConfig("hd_close_imgs") + "";
    	if(StringUtils.isBlank(imgs)) {
    		imgs = Constant.hd_close +"|"+ Constant.hd_close1+"|"+ Constant.hd_close2;
    	}
    	return imgs;
    }
    
    /**
     * 登录之后，查找活动关闭按钮的时间
     * @return
     */
    private int getHDCloseTime() {
    	try {
    		return Integer.parseInt(MYConfig.getInstance().getConfig("hd_close_time") + "");	
    	}catch(Exception e) {
    		return 20000;//20秒
    	}
    	
    }
    
    private void login_after() {
    	active();
        
    	List<CoordBean> list = Base.findPic(getHDClose(),getHDCloseTime());
        if(list.size() > 0){
			mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
			sleep.sleep(500);
		}
    	
    	//465 233
        list = Base.findPic(Constant.login_success,20000);
        if(list.size() > 0){
        	Base.addLog("登录成功");
        }
        
        list = Base.findPic(Constant.do_ok,3000);
		if(list.size() > 0){
			mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
			sleep.sleep(500);
		}
    	//收起任务栏
    	list = Base.findPic(Constant.go_index_1 + "|" + Constant.go_index_1_1,10000);
    	int x = -1;
    	int y = -1;
    	if(list.size() > 0){
    		x = list.get(0).getX();
    		y = list.get(0).getY();
    		mouse.mouseClick(x, y, true);
    		sleep.sleep(1000);
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
    			sleep.sleep(1500);
    		}else{
    			list = Base.findPic(Constant.huanbaoliangongshagn01,1000);
    			if(list.size() > 0){
    				int sx = 322;
    	    		int sy = 165;
    				mouse.mouseClick(sx, sy, true);
        			sleep.sleep(1500);
    			}
    		}
    		//判断是否在环保练功场（未成神）
    		list = Base.findPic(Constant.go_index,20000);
    		if(list.size() > 0){
    			mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
    		}
    	}
    	handhuodong();
		sleep.sleep(500);
    }
    
    private void handhuodong(){
    	String huodongImgs = (String) MYConfig.getInstance().getConfig("huodong_imgs");
    	if(StringUtils.isNotBlank(huodongImgs)){
    		List<CoordBean> list = Base.findPic(huodongImgs,5000);
    		if(list.size() > 0){
    			//[{"x":400,"y":300,"delay":2000},{"x":400,"y":300,"delay":2000}];
    			String huodong_xy_delay = (String) MYConfig.getInstance().getConfig("huodong_xy_delay");
    			mouse.mouseClick(list.get(0).getX() + 8, list.get(0).getY() + 8, true);
    			new Sleep().sleep(800);
    			if(StringUtils.isNotBlank(huodong_xy_delay)){
    				JSONArray jsonArr = JSONArray.fromObject(huodong_xy_delay);
    				for(int i=0,j=jsonArr.size();i<j;i++){
    					JSONObject jsonObj = (JSONObject)jsonArr.get(i);
    					int x = jsonObj.getInt("x");
    					int y = jsonObj.getInt("y");
    					int delay = jsonObj.getInt("delay");
    					mouse.mouseClick(x, y, true);
    					new Sleep().sleep(delay);
    				}
    			}
    		}
    	}
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
    		sleep.sleep(500);
    		//点击切换小号
    		List<CoordBean> list1 = Base.findPic(Constant.xiaohao_qiehuan_1 + "|" + Constant.xiaohao_qiehuan_2,3000);
    		if(list1.size() > 0){
    			mouse.mouseClick(list1.get(0).getX() + 5, list1.get(0).getY() + 5, true);	
    		}else{
    			//154 350
    			mouse.mouseClick(list.get(0).getX() - 154, list.get(0).getY() + 350, true);
    		}
    	}else{
    		Base.addLog("发生错误未能找到wb_close..");
    		return Base.xiaohao;
    	}
    	if(Base.xiaohao.size() > 0){
    		Base.addLog("小号还未处理完.." + Base.xiaohao);
    		return Base.xiaohao;
    	}
    	sleep.sleep(3000);
    	list = Base.findPic(Constant.xiaohao_close,2000);
    	if(list.size() > 0){
    		String[] user = null;
    		try{
    			if(Base.listUsers.size() > 0){
    				user = Base.listUsers.getLast();
    			}
    		}catch(Exception e){
    			if(Base.listUsers.size() > 0){
    				user = Base.listUsers.get(0);
    			}
    			e.printStackTrace();
    		}
    		if(null == user){
    			return new ArrayList<CoordBean>();
    		}
    		int xiaohaoNum = Integer.parseInt(user[2]);
    		if(xiaohaoNum > 0 && !Base.listUserXiaoHao.contains(user[0])){//有小号，并且小号没有处理过
    			Base.listUserXiaoHao.add(user[0]);
    			for(int i=1;i<=xiaohaoNum;i++) {
    				CoordBean xiaohao = new CoordBean();//266*89
    				if(i == 1) {
            			xiaohao.setId(1);
                		xiaohao.setX(list.get(0).getX() - 266);
                		xiaohao.setY(list.get(0).getY() + 89);
    				}else if(i == 2) {
    					xiaohao.setId(2);
                		xiaohao.setX(list.get(0).getX() - 153);
                		xiaohao.setY(list.get(0).getY() + 89);
    				}else if(i == 3) {
    					xiaohao.setId(3);
                		xiaohao.setX(list.get(0).getX() - 41);
                		xiaohao.setY(list.get(0).getY() + 89);
    				}else if(i == 4) {
    					xiaohao.setId(4);
                		xiaohao.setX(list.get(0).getX() - 376);
                		xiaohao.setY(list.get(0).getY() + 178);
    				}else if(i == 5) {
    					xiaohao.setId(5);
                		xiaohao.setX(list.get(0).getX() - 266);
                		xiaohao.setY(list.get(0).getY() + 178);
    				}
    				Base.xiaohao.add(xiaohao);
    			}
    		}
    	}
    	return Base.xiaohao;
    }
    
    public boolean switchUser(){
    	Base.addLog("开始切换账号..");
    	autoResurgenceThread.clearData();//清空死亡次数
    	sleep.sleep(2000);
    	//先退出当前账号
    	//按K然后找到设置按钮
    	press.keyPress(press.K);
    	sleep.sleep(1000);
    	//系统设置坐标 776*438
    	mouse.mouseClick(776, 438, true);
    	sleep.sleep(500);
    	List<CoordBean> xiaohao = has_xiaohao();
    	//判断是否有小号
    	sleep.sleep(1500);
    	//210 247
		List<CoordBean> list = Base.findPic(Constant.xiaohao_close,1500);
    	if(xiaohao.size() > 0){
    		Base.addLog("有小号.." + xiaohao);
    		sleep.sleep(500);
    		if(list.size() <= 0){
    			Base.addLog("找不到" + Constant.xiaohao_close);
    			return false;
    		}
    		mouse.mouseClick(xiaohao.get(0).getX(), xiaohao.get(0).getY(),true);
    		Base.addLog("移除.." + xiaohao + "中的：" + xiaohao.get(0));
    		Base.runRole.put("main_role_sub", xiaohao.get(0).getId() + "");
    		Base.xiaohao.removeFirst();
    		sleep.sleep(500);
        	if(list.size() > 0){
        		Base.addLog("点击切换按钮");
        		sleep.sleep(500);
        		mouse.mouseClick(list.get(0).getX() - 210, list.get(0).getY() + 247,true);
        	}
        	sleep.sleep(5000);
        	//判断是否还停留在切换小号页面；
        	list = Base.findPic(Constant.xiaohao_close,1500);
        	if(list.size() > 0) {
        		exit(list);
        		return false;
        	}else {
        		return true;	
        	}
    	}else{
    		exit(list);
    		return false;
    	}
    }
    
    public void exit(List<CoordBean> list) {
    	//无小号
		Base.addLog("无小号..");
		if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(),true);
    	}
    	list = Base.findPic(Constant.logout +"|" + Constant.logout_1  
    			+"|" + Constant.logout_2  +"|" + Constant.logout_3,5000);
		if(list.size() > 0){
			Base.addLog("找到图片logout");
			mouse.mouseClick(list.get(0).getX() + 15, list.get(0).getY() + 12, true);
		}else{
			list = Base.getWB_CLOSE(5000);
			//272 383//切换角色
			//126 383//退出游戏
			if(list.size() > 0){
				List<CoordBean> list1 = Base.findStrE("退出","84b2a2-47484a", 0.9, 0);
				if(list1.size() > 0){
					Base.addLog("找到文字退出");
					mouse.mouseClick(list1.get(0).getX() + 15, list1.get(0).getY() + 12, true);
				}else{
					mouse.mouseClick(list.get(0).getX() - 53, list.get(0).getY() + 347, true);
				}
			}
		}
		list = Base.findPic(Constant.logout_ok,5000);
		if(list.size() > 0){
			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);//回到登录页面
			sleep.sleep(2000);
			//标记该账号已经完成挖宝
			finshRole();
		}
    }
    
    public void finshRole() {
    	try {
	    	File file = finshRoleInit();
	    	String str = FileUtils.readFileToString(file);
	    	Object ruStr = Base.runRole.get("main_role");
	    	StringBuilder sb = new StringBuilder(str);
	    	if(sb.length() > 0) {
	    		List<String> arrs = Arrays.asList(str.split(","));
	    		if(!arrs.contains(ruStr)) {
	    			sb.append("," + ruStr);	
	    		}
	    	}else {
	    		sb.append(ruStr);
	    	}
	    	FileUtils.writeStringToFile(file, sb.toString());
    	} catch (IOException e) {
			e.printStackTrace();
			Base.addLog(e.getMessage());
		}
    }
    
    /**
     * 初始化txt存储文件
     * @return
     * @throws IOException 
     */
    public File finshRoleInit() throws IOException {
    	String date = Base.getDate("YYYYMMdd");
    	String folder = Base.home + File.separator + "finsh_role";
    	File file1 = new File(folder);
    	if(!file1.exists()){
    		file1.mkdirs();
    	}
    	String fileStore = folder + File.separator + date + ".txt";
    	File file = new File(fileStore);
    	if(!file.exists()) {
			file.createNewFile();
    	}
    	return file;
    }
    
    /**
     * 获取已经挖宝完成的账号
     * @return
     */
    public List<String> getFinshRole() {
    	String ret = "";
    	try {
    		File file = finshRoleInit();
    		ret = FileUtils.readFileToString(file);
    	}catch(Exception e) {
    		e.printStackTrace();
			Base.addLog(e.getMessage());
    	}
    	return Arrays.asList(ret.split(","));
    }
    
    /**
     * 返回是否寻路（打开藏宝图之后是否寻路）
     * @return
     */
    public boolean open_cbt(List<CoordBean> list){
    	press.keyPress(press.Z);
    	sleep.sleep(200);
    	open_beibao();
    	if(null != list && list.size() > 0){
			Base.addLog("点击藏宝图...");
			sleep.sleep(200);
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		sleep.sleep(500);
			Base.addLog("使用藏宝图...");
			list = Base.findPic(Constant.cangbaotu_shiyong+"|"+Constant.cangbaotu_shiyong_1,1000);
			if(list.size() > 0){
				//75 * 43
				mouse.mouseClick(list.get(0).getX() + 8, list.get(0).getY() + 8, true);
			}
			sleep.sleep(500);
			list = Base.findPic(Constant.beibao_close,1500);
			if(list.size() > 0){
				mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
				sleep.sleep(800);
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
    	Base.addLog("打开背包前检查活动界面是否关闭..");
    	List<CoordBean> list_wb_close = Base.getWB_CLOSE(200);
		if(list_wb_close.size() > 0){
			Base.addLog("关闭活动界面");
			mouse.mouseClick(list_wb_close.get(0).getX(), list_wb_close.get(0).getY(), true);
			sleep.sleep(200);
		}
    	//打开背包
    	Base.addLog("打开背包...");
    	press.keyPress(press.I);
    	sleep.sleep(800);
    	//打开任务栏 228*42 x-228 y+42
    	List<CoordBean> list = Base.findPic(Constant.beibao_close,2000);
    	if(list.size() > 0){
    		Base.addLog("打开任务栏...");
    		mouse.mouseClick(list.get(0).getX() - 228, list.get(0).getY() + 42, true);
    	}
    	sleep.sleep(500);
    	return list;
    }
    
    /**
     * 关闭背包
     */
    private void close_beibao(){
    	List<CoordBean> list = Base.findPic(Constant.beibao_close,2000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);
    		sleep.sleep(500);
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
    	list = Base.findPic(cangbaotu,412,144,756,551,0.8);
    	boolean flag = true;
    	int i=0;
    	do{
    		Base.addLog("关闭背包...");
    		mouse.mouseClick(b_x + 8, b_y + 8, true);
    		sleep.sleep(500);
    		List<CoordBean> list1 = Base.findPic(Constant.beibao_close);
    		if(list1.size() <= 0){
    			flag = false;
    		}else{
    			b_x = list1.get(0).getX();
    			b_y = list1.get(0).getY();
    		}
    		i++;
    		if(i>5){
    			Base.addLog("未能关闭背包...");
    			flag = false;
    		}
    	}while(flag);//防止背包关不掉
    	return list;
    }
    
    
    //do_1 处理山贼
    public void do_1(int x,int y){
    	mouse.mouseClick(x, y, true);
    	Base.addLog("处理山贼...");
    	int i = 0;//18次
    	boolean flag = true;
    	do{
    		i++;
    		do_0();
    		if(i % 3 == 0){
    			press.keyPress(press.F2);
    			sleep.sleep(500);
    		}
    		Base.addLog("处理山贼... i " + i);
    		flag = Base.findPic(getBaoXiangImg()).size() <= 0;
    		if(flag){
    			if(i >= 18){
    				flag = false;
    			}
    		}
    	}while(flag);
    	Base.addLog("处理山贼结束...");
    }
    
    private void do_0(){
    	sleep.sleep(200);
		press.keyPress(press.F3);
		sleep.sleep(1000);
		press.keyPress(press.F1);
		sleep.sleep(1000);
		press.keyPress(press.F3);
		sleep.sleep(1000);
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
		sleep.sleep(200);
    	do{
    		press.keyPress(press.SPACE);
    		sleep.sleep(200);
    		if(i == 2){
    			press.keyPress(press.F2);
    			sleep.sleep(500);
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
    		i++;
    		do_0();
    		if(i % 3 == 0){
    			press.keyPress(press.F2);
    			sleep.sleep(500);
    		}
    		press.keyPress(press.SPACE);
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
    	clearOK();
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
    	if(autoResurgenceThread.die(die_list)) {
    		sleep.sleep(500);
			//如果人物死亡，执行复活
    		WB.threadPool.execute(autoResurgenceThread);
    		sleep.sleep(1000);
    		do{
    			sleep.sleep(1000);
    			Base.addLog("等待复活...");
    		}while(autoResurgenceThread.die(die_list));
    		//判断是否已经复活过5次
    		if(autoResurgenceThread.maxDieNum()){
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
    	sleep.sleep(500);
    	do_all(list);//继续挖宝
    }
    
    /**
     * 判断是否出现确定按钮，如果有确认按钮 清空掉；
     */
    private void clearOK(){
    	Base.addLog("清空确认按钮开始..");
    	List<CoordBean> list = Base.findPic(Constant.do_ok);
    	if(list.size() > 0){
    		sleep.sleep(200);
    		mouse.mouseClick(list.get(0).getX() + 5,list.get(0).getY() + 5, true);
    	}
    	sleep.sleep(200);
    	list = Base.findStrE("确定","c7bd97-383e38", 1, 0,200);
    	if(list.size() > 0){
    		sleep.sleep(200);
    		mouse.mouseClick(list.get(0).getX() + 5,list.get(0).getY() + 5, true);
    	}
    	list = Base.findPic(Constant.close_liaotian+"|"+Constant.close_liaotian_1,254,321,444,447,0.9,true);
    	if(list.size() > 0){
    		sleep.sleep(200);
    		mouse.mouseClick(list.get(0).getX() +2,list.get(0).getY() + 2, true);
    	}
    	sleep.sleep(200);
    	Base.addLog("清空确认按钮结束..");
    }
    
    public void clear_gw(){
    	//清理小怪,避免打断挖宝
        sleep.sleep(200);
    	for(int i = 0,j=6;i<j;i++){
    		Base.addLog("释放技能F3...");
        	press.keyPress(press.F3);
        	sleep.sleep(800);
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
    		sleep.sleep(200);
    		boolean flag = true;
    		int i = 0;
    		boolean m = true;
    		do{
    			list = Base.findPic(getBaoXiangImg(),1000);
        		//25 * 65
            	if(list.size() > 0 && i <=60){
            		i++;
            		if(m){//先移动到宝箱周围,防止过远点不到
            			m = false;
            			int x = list.get(0).getX() + 125;
            			int y = list.get(0).getY() + 55;
            			if(x > Base.screenWidth) {
            				x = list.get(0).getX() - 125;
            				Base.addLog("宝箱x超过屏幕宽度:"+x);
            			}
            			if(y > Base.screenHeight) {
            				y = list.get(0).getY() - 55;
            				Base.addLog("宝箱y超过屏幕高度:"+y);
            			}
            			Base.addLog("移动到宝箱周围("+x+","+y+")..");
            			mouse.mouseClick(x,y, true);
            			sleep.sleep(2000);
            		}else {
            			if(i == 20 || i == 30 || i == 40){
                			do_0();//防止怪物与宝箱重叠,释放技能杀怪
                		}
                		Base.addLog("点击宝箱...");
                		mouse.mouseMoveTo(list.get(0).getX() + 30,list.get(0).getY() + 55);
                		sleep.sleep(200);
                		Base.doubleClick();
                		sleep.sleep(500);
            		}
            	}else{
            		Base.screenImage("宝箱");
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
        	sleep.sleep(2000);
        	list = Base.findPic(Constant.wb_4,3000);//是否已经挖过宝了
        	if(list.size() > 0){
        		//115
        		mouse.mouseClick(list.get(0).getX() + 115, list.get(0).getY(), true);
        		sleep.sleep(500);
        		list = Base.findPic(Constant.baoxiang_click,3000);
        		if(list.size() > 0){
        			mouse.mouseClick(list.get(0).getX() + 15, list.get(0).getY() + 10, true);
            		sleep.sleep(500);
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
    	sleep.sleep(3000);
    	List<CoordBean> list = Base.findPic(Constant.wb_0,12000);
    	if(list.size() > 0){
    		sleep.sleep(2000);
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	sleep.sleep(2000);
        	list = Base.findPic(Constant.wb_4,3000);//是否已经挖过宝了
        	if(list.size() > 0){
        		Base.addLog("该账号已经挖过宝..");
        		Base.close_wb_page();
        		return;
        	}
        	list = Base.findPic(Constant.wb_1+"|"+Constant.wb_1_1,5000);
        	if(list.size() > 0){
        		//x+371,y-8
        		sleep.sleep(200);
        		mouse.mouseMoveTo(list.get(0).getX() + 366, list.get(0).getY() - 9);
        		sleep.sleep(500);
        		mouse.mouseClick(list.get(0).getX() + 366, list.get(0).getY() - 9, true);
        		sleep.sleep(500);
        		//判断当前是在雷鸣还是在神界
        		list = Base.findStrE("确定","c7bd97-383e38", 0.9, 0,2000);
    			if(list.size() > 0){
    				mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);
    				sleep.sleep(500);
    				sj_to_lm();
    			}else{
    				if(Base.is_xunlu()) {
    					Base.addLog("当前人物在雷鸣");
        				wb_2();	
    				}else {
    					Base.addLog("程序未能处理,当前人物未能到达雷鸣");
    					return;
//    					hallowmas_to_lm();
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
//    	List<CoordBean> list = Base.findStrE("神界", 
//				"bbbb06-3c3c07|bbbb13-3c3c13|bfbf13-404013", 0.8, 0,10000);
//		if(list.size() > 0){
			Base.addLog("当前人物在神界");
			sleep.sleep(2000);
			press.keyPressTime(press.W, 380);
			sleep.sleep(500);
			press.keyPress(press.SPACE);
			sleep.sleep(800);
			boolean a = Base.go_to_lm();
			if(a){
				sleep.sleep(3000);
				lcbt();
			}else{
				Base.addLog("没有找到传送到雷鸣的对话框，尝试点击坐标（319,150）；");
				mouse.mouseClick(319, 150, true);
				sleep.sleep(800);
				a = Base.go_to_lm();
				if(a){
					lcbt();
				}else{
					Base.addLog("未能到达雷鸣");
				}
			}
//		}else{
//			Base.addLog("没有找到神界的传送师");
////			hallowmas_to_lm();
//		}
		Base.addLog("神界去雷鸣结束..");
    }
    
//    private void hallowmas_to_lm() {
//    	Base.addLog("万圣节期间,去雷鸣开始..");
//    	//打开背包，关闭背包
//    	open_beibao();
//    	sleep.sleep(1000);
//    	close_beibao();
//    	List<CoordBean> list = Base.findStrE("传送", "30eb37-311437", 0.8, 0,5000);
//    	if(list.size() > 0){
//    		Base.addLog("找到传送门...");
//    		mouse.mouseClick(list.get(0).getX() + 15, list.get(0).getY() + 50, true);
//    	}else{
//    		Base.addLog("直接点击坐标(180, 213)...");
//    		mouse.mouseClick(180, 213, true);
//    	}
//    	sleep.sleep(1000);
//    	boolean a = Base.go_to_lm();
//		if(a){
//			lcbt();
//		}else{
//			Base.addLog("没有找到传送到雷鸣的对话框");
//		}
//		Base.addLog("万圣节期间,去雷鸣结束..");
//    }

    
	private void wb_2(){
		Base.xunlu();
		List<CoordBean> list = Base.findPic(Constant.wb_2,145,318,269,365,10000);
		if(list.size() > 0){
			Base.addLog("藏宝图NPC 0/5 " + list.get(0).getX() + "," + list.get(0).getY());
			int i = 0;
			boolean flag = true;
			do{
				mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
				sleep.sleep(500);
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
					sleep.sleep(500);
				}
				if(i > 10 || list.size() <= 0){
					flag = false;
				}
			}while(flag);
			sleep.sleep(500);
			list = open_beibao();
			int b_x = list.get(0).getX();
	    	int b_y = list.get(0).getY();
	    	mouse.mouseClick(b_x + 5, b_y + 5, true);
			sleep.sleep(500);
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
        List<String> listFinshRole = getFinshRole();//已经挖宝完成的账号
        Base.addLog("已经挖宝完成的账号【"+listFinshRole+"】");
        for(String itemUser : users){
            String[] userInfo = itemUser.split(",");//下标：第0个账号，第1个密码，第2个是否含有小号
            if(userInfo.length == 3){
        		//账号没有处理过,并且没有挖宝完成
        		if(!existsUser(userInfo[0],Base.listUsers) && !listFinshRole.contains(userInfo[0])){
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
        		sleep.sleep(3000);
        		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	}else{
        		list = Base.findPic(Constant.update_4,2000);
        		if(list.size() > 0){
        			Base.addLog("找到更新内容界面，关闭界面然后重新登录");
        			sleep.sleep(200);
        			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        			sleep.sleep(200);
        			return login();
        		}
        		//判断是会否闪退到了桌面
        		list = Base.findPic(Constant.app_game,1000);
        		if(list.size() > 0){
        			openGameApp();
        			return login();
        		}else {
        			Base.addLog("没有找到登录按钮login_1");
        			Base.screenImage("login_1");
        			return false;
        		}
        	}
    	}
    	String[] user = loadUserInfo();
    	if(null == user){
    		Base.addLog("没有找到用户，可能已经全部处理过了..");
    		return false;
    	}
    	Base.addLog("开始登录：" + user[0]);
    	List<CoordBean> listPassword = input_user(user);
    	sleep.sleep(200);
    	list = Base.findPic(Constant.login_2,2000);
    	if(list.size() > 0){
    		//点击登录
    		mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
    		sleep.sleep(5000);
			list = Base.findPic(Constant.login_e+"|"+Constant.login_2 +"|"+Constant.login_u+"|"+Constant.login_p,3000);
			if(list.size() > 0){//如果账号密码不正确
				clearInput();
				return login();//重新登录
			}
			list = Base.findPic(Constant.login_3,10000);
			if(list.size() > 0){
				mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
			}
			
			list = Base.findPic(getHDClose(),getHDCloseTime());
	        if(list.size() > 0){
				mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
				sleep.sleep(500);
			}
	        
    		list = Base.findPic(Constant.login_success,30000);
    		if(list.size() > 0){
    			Base.runRole.put("main_role", user[0]);
    			Base.addLog("登录成功..");
    			//登录成功才添加进来
    			Base.listUsers.add(user);
    		}else{
        		Base.addLog("没有找到login_success");
        		Base.screenImage("login_success");
        		list = Base.findStrE("确定","c7bd97-383e38", 1, 0,2000);
        		boolean flag_qd = false;
        		if(list.size() > 0){
        			Base.addLog("找到确定按钮，点击确定，然后重新登录");
        			mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 5, true);
        			flag_qd = true;
        			sleep.sleep(200);
        		}
        		list = Base.findPic(Constant.login_fanmang);
        		boolean flag_qx = false;
    			if(list.size() > 0){
    				Base.addLog("服务器繁忙,继续重试");
    				mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);
    				flag_qx = true;
    				sleep.sleep(200);
    			}
    			if(flag_qd || flag_qx) {
    				return login();
    			}
    			list = Base.findPic(Constant.login_u+"|"+Constant.login_1,2000);
    			if(list.size() > 0){
    				Base.addLog("还停留在输入账号密码界面，重新登录");
    				return login();
    			}
    			return false;
        	}
    	}else{
    		//TODO 判断是否有验证码(有验证码换号登录)
    		list = Base.findPic(Constant.login_fanmang);//服务器人数已满
			if(list.size() > 0){
				Base.addLog("服务器繁忙,继续重试");
				mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);
				sleep.sleep(200);
				return login();
			}
			//登录页面变白,处理
			Base.addLog("没有找到登录按钮login_2,尝试点击密码框(可能页面变白)");
			mouse.mouseClick(listPassword.get(0).getX() + 90, listPassword.get(0).getY() + 13, true);
			sleep.sleep(2000);
			Base.addLog("没有找到登录按钮login_2,尝试点击账号框(可能页面变白)");
			mouse.mouseClick(listPassword.get(0).getX() + 90, listPassword.get(0).getY() - 70, true);
			sleep.sleep(2000);
			list = Base.findPic(Constant.login_u);
	    	if(list.size() > 0){
	    		Base.addLog("重新找到账号密码输入框，重新登录");
	    		return login();
			}else{
				Base.addLog("没有找到登录按钮login_2");
				Base.screenImage("login_2");	
			}
    		return false;
    	}
    	return true;
    }
    
    /**
     * 输入账号密码
     * @param user
     */
    public List<CoordBean> input_user(String[] user){
    	clearInput();
    	sleep.sleep(500);
    	List<CoordBean> list = Base.findPic(Constant.login_u,2000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX() + 90, list.get(0).getY() + 13, true);
    		sleep.sleep(500);
    		robot.sendString(user[0]);
    	}
    	list = Base.findPic(Constant.login_p,2000);
    	if(list.size() > 0){
    		mouse.mouseClick(list.get(0).getX() + 90, list.get(0).getY() + 13, true);
    		sleep.sleep(500);
    		robot.sendString(user[1]);
    	}
    	return list;
    }
    
    /**
     * 清空输入框
     */
    public void clearInput(){
    	sleep.sleep(200);
    	List<CoordBean> list = Base.findPic(Constant.login_u,2000);
    	if(list.size() > 0){
    		mouse.mouseMoveTo(list.get(0).getX() + 90, list.get(0).getY() + 13);
    		sleep.sleep(200);
    		Base.doubleClick();
    		sleep.sleep(200);
    		press.keyPress(press.DELETE);
    		sleep.sleep(200);
    		mouse.mouseClick(list.get(0).getX() + 90, list.get(0).getY() + 13,true);
    		for(int i=0,j=10;i<j;i++){
    			sleep.sleep(100);
        		press.keyPress(press.DELETE);	
    		}
    		for(int i=0,j=10;i<j;i++){
    			sleep.sleep(100);
        		press.keyPress(press.BACKSPACE);	
    		}
    	}
    }
    
    /**
     * 打开游戏app
     */
    public void openGameApp(){
    	String img = Constant.app_game + "|" + Constant.app_game1+ "|" +Constant.app_game2;
    	//1分钟内循环查找游戏app。如果进入了桌面 肯定能找到
    	List<CoordBean> list = Base.findPic(img,60000);
    	if(list.size() > 0){
    		sleep.sleep(10000);
    		Base.addLog("找到"+ img +"点击..");
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		sleep.sleep(500);
    		list = Base.findPic(img,1000);
    		if(list.size() > 0){
    			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		}
    		list = Base.findPic(img,1000);
    		if(list.size() > 0){
    			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		}
    	}
    	sleep.sleep(500);
    	//检查是否需要更新
        update();
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
    	autoResurgenceThread = setAutoResurgenceThread(2);
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
    	
    	JButton button = new JButton("测试");
    	button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int hwnd = active();
            	bind(hwnd);
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
    			if(null != autoHangUp) {
    				autoHangUp.destroy();	
    			}
    			SysHotKey.destroy();
    		}
    	});
    	
    	btnHangUp = new JButton("挂材料");
    	btnHangUp.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {
    			int handFlag = 1;
    			if(btnHangUp.getText().equals("挂材料")){
    				if(autoHangUp == null) {
    					autoHangUp = new AutoHangUp();
    				}
    				autoHangUp.m3_start = true;
    				btnHangUp.setText("暂停");
    				WB.threadPool.execute(autoHangUp.getAutoHangUpThread(handFlag));
                }else if(btnHangUp.getText().equals("暂停")){
                	autoHangUp.m3_start = false;
                	btnHangUp.setText("挂材料");
                }
    		}
    	});
    	
    	btnHangUp_2= new JButton("挂材料（上）");
    	btnHangUp_2.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			int handFlag = 2;
    			if(btnHangUp_2.getText().equals("挂材料（上）")){
    				if(autoHangUp == null) {
    					autoHangUp = new AutoHangUp();
    					autoHangUp.m3_start = true;
    					WB.threadPool.execute(autoHangUp.getAutoHangUpThread(handFlag));
    				}
    				autoHangUp.m3_start = true;
            		btnHangUp.setText("暂停");
                }else if(btnHangUp_2.getText().equals("暂停")){
                	autoHangUp.m3_start = false;
                	btnHangUp.setText("挂材料（上）");
                }
    		}
    	});
    	GroupLayout groupLayout = new GroupLayout(getContentPane());
    	groupLayout.setHorizontalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
    			.addGroup(groupLayout.createSequentialGroup()
    				.addGap(123)
    				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
    					.addComponent(btnNewButton)
    					.addComponent(button)
    					.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
    				.addGap(18)
    				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
    					.addComponent(btnHangUp_2)
    					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
    						.addComponent(btn_exit, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
    						.addComponent(btn_reset, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
    						.addComponent(btnHangUp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    				.addContainerGap(95, Short.MAX_VALUE))
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
    				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
    					.addComponent(button_1)
    					.addComponent(btnHangUp))
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(btnHangUp_2)
    				.addContainerGap(71, Short.MAX_VALUE))
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
    
    public void bind(int hwnd) {
    	Sleep sleep = new Sleep();
    	sleep.sleep(200);
    	Base.addLog("绑定模拟器开始,顶级窗口 > " + hwnd);
    	List<Integer> hwnds = window.EnumWindow(hwnd,"","TheRender",1 + 4 +8 + 16);
    	if(null != hwnds && hwnds.size() > 0) {
    		Base.addLog("绑定TheRender窗口 > " + hwnds.get(0));
    		boolean result = com.bindEx(hwnds.get(0), com.GDI, com.WINDOWS, com.WINDOWS, "", 0);
    		Base.addLog("绑定结果  > " + result);
    	}
    	Base.addLog("绑定模拟器结束 > " + hwnd);
    	
    	Base.addLog("测试绑定截图");
    	Base.screenImage("测试绑定截图");
    	
    	Base.addLog("测试找图登录框..");
    	List<CoordBean> list = Base.findPic(Constant.login_u+"|"+Constant.login_p,2000);//账号输入框
    	Base.addLog("测试找图登录框.." + list.size());
    	if(list.size() > 0) {
    		mouse.mouseClick(list.get(0).getX() + 90, list.get(0).getY() + 13, true);
    		Base.addLog("鼠标点击.." + list.get(0).getX() + 90 +"," + list.get(0).getY() + 13);
    		sleep.sleep(500);
    	}
    	Base.addLog("测试找图副本中心..");
       	List<CoordBean> list1 = Base.findPic(Constant.wb_0,20000);
    	if(list1.size() > 0){
    		mouse.mouseClick(list1.get(0).getX(), list1.get(0).getY(), true);
    		sleep.sleep(2000);
    	}
    	Base.addLog("测试找图副本中心.." + list1.size());
    	
    	Base.addLog("测试按键ESC..");
    	press.keyPress(press.ESC);
    	sleep.sleep(1000);
    	
    	Base.addLog("延迟70秒..");
    	sleep.sleep(70000);
    	
    	Base.addLog("释放10次技能..");
    	for(int i=1,j=11;i<j;i++) {
    		Base.addLog("释放技能F3..");
    		press.keyPress(press.F3);
    		sleep.sleep(1000);
    	}
    }
}
