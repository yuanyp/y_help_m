package com.y_ghelp.test.demo.my;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.xnx3.DateUtil;
import com.xnx3.microsoft.Color;
import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.FindPic;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.SystemUtil;
import com.xnx3.microsoft.Window;
import com.xnx3.robot.Robot;
import com.xnx3.robot.support.CoordBean;
import com.y_ghelp.test.demo.config.MYConfig;

public class MYUtil {
	
	static Logger log = Logger.getLogger(MYUtil.class);
	
	static Com com;
    static Window window;
    static Mouse mouse;
    static Press press;
    static Color color;
    static Robot robot;
    static FindPic findPic;
    static String gameName = "";
    static String defaultGamePath = "";
    static ThreadPoolExecutor threadPool = null;
    static Thread openGameTh;
	static{
	    com = new Com();
        window = new Window(com.getActiveXComponent());    //窗口操作类
        mouse = new Mouse(com.getActiveXComponent());   //鼠标模拟操作类
        press = new Press(com.getActiveXComponent());   //键盘模拟操作类
        color = new Color(com.getActiveXComponent());   //颜色相关的取色、判断类
        findPic = new FindPic(com.getActiveXComponent());
        robot = new Robot();
        gameName = (String) MYConfig.getInstance().getConfig("gameName");
        defaultGamePath = (String) MYConfig.getInstance().getConfig("defaultGamePath");
        // 构造一个线程池
        threadPool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        
        openGameTh = new Thread(new Runnable() {
            public void run() {
                try {
                    // 打开应用,此函数会阻塞当前线程，直到打开的关闭为止。故而须另开辟一个线程执行此函数
                    String cmdExe = " start \"\" \"" + defaultGamePath + "\"";
                    addLog(cmdExe);
                    SystemUtil.cmd(cmdExe);
                } catch (Exception e) {
                    addLog(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
	}
    
    /**
     * 日志纪录
     * @param str
     */
    public static void addLog(Object str){
    	log.info(str);
        System.out.println(str);
    }
    
    public static void readStart(List<String> listUsers) throws Exception{
    	addLog("start .." + DateUtil.currentDate("YYYY-MM-DD HH:mm:ss"));
		
    	String user = (String)MYConfig.getInstance().getConfig("user");
        if(StringUtils.isBlank(user)){
            addLog("未能找到账号..");
        }
        String[] users = user.split(";");
        addLog("users .." + users.length);
        for(String itemUser : users){
            String[] userInfo = itemUser.split(",");//下标：第0个账号，第1个密码，第2个是否含有小号
            if(userInfo.length == 3){
                if(!listUsers.contains(userInfo[0])){//账号没有处理过
                	addLog("listUsers add .." + userInfo[0]);
                    listUsers.add(userInfo[0]);
                    start_game(userInfo);
                }
            }else{
                addLog("账号配置错误..");    
            }
        }
        
        addLog("end .." + DateUtil.currentDate("YYYY-MM-DD HH:mm:ss"));
    }
    
    /**
     * 开始游戏
     * TODO <功能简述> <br/>
     * TODO <功能详细描述>
     * @throws Exception
     */
    public static void start_game(String[] user) throws Exception{
    	threadPool.execute(openGameTh);
    	robot.delay(1000);
        robot.setSourcePath(MYUtil.class);
        boolean showGame = robot.imageDelaySearch(0, 0, robot.screenWidth, robot.screenHeight, robot.getResourceImage(Resouce.startImg), Robot.SIM_ACCURATE, 10000);
        if(!showGame){
        	addLog("未能打开游戏..");
        	return;
        }
        int hwnd = window.findWindow(0, null, gameName);
        if(hwnd > 0){
            window.moveWindow(hwnd, 0, 0);
            window.setWindowActivate(hwnd); //激活窗口
            robot.setSourcePath(MYUtil.class);
            List<CoordBean> list = robot.imageSearch(0, 0, robot.screenWidth, robot.screenHeight, Resouce.startImg, Robot.SIM_ACCURATE);
            if(null == list || list.size() == 0){
                addLog("未能找到【"+gameName+"】的“开始游戏”按钮..");
            }
            mouse.mouseClick(list.get(0).getX() - 60, list.get(0).getY() - 60, true);//移动之后，左键点击
            
            boolean flag = true;
            do{
                login(user);
                addLog("flag.." + flag);
                boolean loginError = findLoingError(3000);
                addLog("loginError.." + loginError);
                if(loginError){//登录失败
                	addLog("服务器拥挤或者账号密码错误..");
                    press.keyPress(press.ENTER);
                    if(findLoingError(3000)){
                    	press.keyPress(press.ENTER);
                    }
                }else {
                	robot.setSourcePath(MYUtil.class);
                    boolean loginError1 = robot.imageDelaySearch(0, 0, robot.screenWidth, robot.screenHeight, robot.getResourceImage(Resouce.error1Img), Robot.SIM_ACCURATE, 1000);
                    addLog("loginError1.." + loginError1);
                    if(loginError1){
                    	addLog("账号密码错误..");
                    	robot.setSourcePath(MYUtil.class);
                        list = robot.imageSearch(0, 0, robot.screenWidth, robot.screenHeight, Resouce.error1Img, Robot.SIM_ACCURATE);
                        if(null == list || list.size() == 0){
                            addLog("未能找到账号密码错误之后弹框的关闭按钮..");
                        }
                        mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);//移动之后，左键点击
                    }else{
                    	flag = false;
                    }
                }
            }while(flag);
            
            robot.setSourcePath(MYUtil.class);
            boolean loginSuccess = robot.imageDelaySearch(0, 0, robot.screenWidth, robot.screenHeight, robot.getResourceImage(Resouce.emailImg), Robot.SIM_ACCURATE, 20000);
            if(!loginSuccess){
                addLog("未知错误，登录失败..");
            }else{
            	addLog("登录成功..");
            }
        }else{
            addLog("未能找到游戏【"+gameName+"】...");
        }
    }
    
    /**
     * 找账号密码输入框
     * @param time
     * @return
     */
    public static boolean findInputPass(int time){
    	robot.setSourcePath(MYUtil.class);
    	boolean flag = robot.imageDelaySearch(0, 0, robot.screenWidth, robot.screenHeight, robot.getResourceImage(Resouce.inputUserImg), Robot.SIM_ACCURATE,time);//1分钟内循环找输入账号和密码的框
    	return flag;
    }
    
    public static boolean findImg(String img,int m,List<CoordBean> list){
    	robot.setSourcePath(MYUtil.class);
    	if(null != list && list.size() > 0){
    		list.clear();
    	}
    	return robot.imageDelaySearch(0, 0, robot.screenWidth, robot.screenHeight, robot.getResourceImage(img), Robot.SIM_ACCURATE, m,list);
    }
    
    public static void login(String[] user) throws Exception{
    	addLog("开始登录..." + user[0]);
    	robot.delay(5000);//休息5秒，等待打开游戏登录界面
        
        boolean flag = findInputPass(80000);
        addLog("是否找到输入框..." + flag);
        List<CoordBean> list = new ArrayList<CoordBean>();
        if(!flag){
        	if(findImg(Resouce.quxiaoImg, 1000, list)){//如果出现取消按钮
        		addLog("點擊取消按鈕...");
        		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	}else{
        		addLog("发生错误，未能进入到登录界面..." + user[0]);
            	press.keyPress(press.ENTER);
        	}
        	login(user);
        }
        robot.setSourcePath(MYUtil.class);
        list = robot.imageSearch(0, 0, robot.screenWidth, robot.screenHeight, Resouce.inputUserImg, Robot.SIM_ACCURATE);
        if(null == list || list.size() == 0){
        	addLog("发生错误，未能进入到登录界面..." + user[0]);
        	return;
        }
        int hwnd = window.findWindow(0, null, gameName);
        addLog("游戏【"+gameName+"】句柄..." + hwnd);
        if(hwnd <= 0){
        	addLog("未能找到游戏【"+gameName+"】...");
        	return;
        }
        window.setWindowActivate(hwnd); //激活窗口
        robot.delay(200);
        //定位到输入账号的输入框
        mouse.mouseClick(list.get(0).getX() - 50, list.get(0).getY() + 12, true);//移动之后，左键点击
        
        //清空账号输入框
        clearInput();
        input_user(user);
        addLog("登录结束..."  + user[0]);
    }
    
    /**
     * 输入账号密码
     * @return 
     * @throws Exception
     */
    public static void input_user(String[] user) throws Exception{
        input(user[0], press);//账号
        robot.delay(200);
        addLog("按下TAB");
        press.keyPress(press.TAB);
        robot.delay(200);
        input(user[1], press);//密码
        robot.delay(200);
        enter(user);
    }
    
    public static void enter(String[] user) throws Exception{
        addLog("按下ENTER");
        press.keyPress(press.ENTER);
        
        robot.setSourcePath(MYUtil.class);
        List<CoordBean> list = new ArrayList<CoordBean>();
        boolean flag = robot.imageDelaySearch(0, 0, robot.screenWidth, robot.screenHeight, robot.getResourceImage(Resouce.quxiaoImg), Robot.SIM_ACCURATE,3000,list);
    	if(flag){//如果出现取消按钮
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		login(user);
    	}else{
    		//按下enter之后，如果还停留在登录页面,继续按ENTER
		    flag = robot.imageDelaySearch(0, 0, robot.screenWidth, robot.screenHeight, robot.getResourceImage(Resouce.inputUserImg), Robot.SIM_ACCURATE,3000);
	        if(flag){
	        	enter(user);
	        }
    	}
    }
    
    /**
     * 输入 不支持 特殊字符，只支持 数字和字母
     * TODO <功能简述> <br/>
     * TODO <功能详细描述>
     * @param input
     * @param press
     * @throws Exception
     */
    public static void input(String input,Press press) throws Exception{
    	robot.delay(200);
    	robot.sendString(input);
        robot.delay(200);
    }
    
    public static void clearInput() throws Exception{
    	for(int i=0;i<20;i++){
    		press.keyPress(press.BACKSPACE);
    		robot.delay(100);
    	}
    }
    
    public static boolean findLoingError(int time){
    	robot.setSourcePath(MYUtil.class);
        boolean loginError = robot.imageDelaySearch(0, 0, robot.screenWidth, robot.screenHeight, robot.getResourceImage(Resouce.errorImg), Robot.SIM_ACCURATE, time);
        return loginError;
    } 
    
    /**
     * 传入人物的坐标
     * @param x
     * @param y
     */
    public static void moveTop(int x,int y){
    	int ry = y - 80;
    	if(ry <= 0 ){
    		return;
    	}
    	mouse.mouseClick(x, ry, true);
    	robot.delay(1500);
    }
    
    public static void moveBottom(int x,int y){
    	int ry = y + 80;
    	if(ry <= 0 ){
    		return;
    	}
    	mouse.mouseClick(x, ry, true);
    	robot.delay(1500);
    }
    
    public static void moveLeft(int x,int y){
    	int rx = x - 80;
    	if(rx <= 0 ){
    		return;
    	}
    	mouse.mouseClick(rx, y, true);
    	robot.delay(1500);
    }
    
    public static void moveRight(int x,int y){
    	int rx = x + 80;
    	if(rx <= 0 ){
    		return;
    	}
    	mouse.mouseClick(rx, y, true);
    	robot.delay(1500);
    }
}
