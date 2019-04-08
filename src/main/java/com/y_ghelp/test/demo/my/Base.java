package com.y_ghelp.test.demo.my;

import java.awt.TrayIcon;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.xnx3.microsoft.Color;
import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.FindPic;
import com.xnx3.microsoft.FindStr;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.Sleep;
import com.xnx3.microsoft.Window;
import com.xnx3.robot.Robot;
import com.xnx3.robot.support.CoordBean;
import com.y_ghelp.test.demo.config.MYConfig;
import com.y_ghelp.test.demo.my.wb.Constant;
import com.y_ghelp.test.demo.my.wb.Layout;

public class Base{
	
	static Logger log = Logger.getLogger(Base.class);
	
	public static Com com;
	public static Window window;
	public static Mouse mouse;
	public static Press press;
	public static Color color;
	public static Robot robot;
	public static FindPic findPic;
	public static FindStr findStr;
	public static com.xnx3.microsoft.File file;
	
	public static final String home = "c:\\temp_img";
	public static String imgHomeFolder = "game_img";
	public static int screenWidth;
	public static int screenHeight;
	
	public static TrayIcon tray;
    
	public static Layout frame = null;
	
	public static String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    
	public static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(8, 10, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
            new ThreadPoolExecutor.DiscardOldestPolicy());
	
	public static Thread start;
	public static Thread start_huangjia;
	public static boolean execute = false;
	public static LinkedList<String[]> listUsers = new LinkedList<String[]>();//判断账号是否已经处理过
	public static LinkedList<String> listUserXiaoHao = new LinkedList<String>();//判断大号的小号是否已经处理过
	public static LinkedList<CoordBean> xiaohao = new LinkedList<CoordBean>();//判断小号
	public static Map<String,String> runRole = new HashMap<>();//当前正在进行的角色
    static{
    	initCom();
        robot = new Robot();
        screenWidth = robot.screenWidth;
        screenHeight = robot.screenHeight;
        if(StringUtils.isNotBlank(path)){
            path = path.replace("/", "\\");
            path = path.substring(1, path.length());
        }
        PropertyConfigurator.configure(path + "log4j.properties");
        start = new Thread(new Runnable() {
            public void run() {
                try {
                   robot.delay(100);
                   frame.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        start_huangjia = new Thread(new Runnable() {
            public void run() {
                try {
                   robot.delay(100);
                   frame.execute_huangjia();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public static void initCom() {
    	com = new Com();
        window = new Window(com.getActiveXComponent());    //窗口操作类
        mouse = new Mouse(com.getActiveXComponent());   //鼠标模拟操作类
        press = new Press(com.getActiveXComponent());   //键盘模拟操作类
        color = new Color(com.getActiveXComponent());   //颜色相关的取色、判断类
        findPic = new FindPic(com.getActiveXComponent());
        findStr = new FindStr(com.getActiveXComponent());
        file = new com.xnx3.microsoft.File(com.getActiveXComponent());
    }
    
    public static void closeApp() {
    	try {
			Runtime.getRuntime().exec("TASKKILL /IM LdBoxHeadless.exe");
			new Sleep().sleep(5000);
		} catch (IOException e) {
			log.error("", e);
		}
    }
    
	public static String getAppName(){
		return MYConfig.getInstance().getConfig("appName") +"";
	}
	
    public static void setImgHomeFolder(String imgHome){
    	imgHomeFolder = imgHome;
    	System.out.println("setImgHomeFolder " + imgHomeFolder);
    }
    
    public static void setDic(int x, String fileName){
    	com.setDict(0, fileName);
    }
    
    public static String getRealPath(String img){
    	return path + imgHomeFolder + File.separator + img;
    }
    
    public static List<CoordBean> findPic(String img){
		return findPic(img,0,0,screenWidth,screenHeight);
    }
    
    public static List<CoordBean> findStrE(String str,String color,double sim,int useDict){
    	return findStrE(str,screenWidth,screenHeight,color, sim, useDict);
    }
    
    public static List<CoordBean> findStrE(String str,String color,double sim,int useDict,int maxDelay){
        List<CoordBean> list = new ArrayList<CoordBean>();
    	int time = 0;
    	while(time<maxDelay){
    		list = findStrE(str,color, sim, useDict);
    		if(list.size()>0){
    			return list;
    		}else{
    			robot.delay(200);
    			time+=200;
    		}
    	}
    	return list;
    }
    
    public static List<CoordBean> findStrE(String str,int w,int h,String color,double sim,int useDict){
		int[] strs = Base.findStr.findStrE(0, 0, w, h, str, color, sim, useDict);
		CoordBean item = new CoordBean();
		List<CoordBean> list = new ArrayList<CoordBean>();
		if(strs[0] != -1){
			item.setX(strs[1]);
			item.setY(strs[2]);
			list.add(item);
		}
		return list;
    }
    
	public static boolean isMove(int time){
		//748 * 35 40*10
		file.screenImage(748,30,(748+45),(30+15), "c:\\logs\\bcd.png");
    	boolean a = findPic.isDisplayDead(748,30,(748+45),(30+15),time);
    	addLog("isMove " + !a);
    	return !a;
    }
    
    public static void doubleClick(){
		mouse.mouseClick(true);
		robot.delay(200);
		mouse.mouseClick(true);
    }
    
    public static List<CoordBean> findPic(String img,int maxDelay){
    	return findPic(img,0,0,screenWidth,screenHeight,maxDelay);
    }
    
    public static void screenImage(String name){
    	String dir = home + File.separator + name;
    	File file1 = new File(dir);
    	if(!file1.exists()){
    		file1.mkdirs();
    	}
    	file.screenImage(0, 0, screenWidth, screenHeight, dir + "\\" + System.currentTimeMillis()+".png");
    }
    
    public static void screenDieImage(){
    	String date = getDate("YYYYMMdd");
    	String dir = home + File.separator + date;
    	File file1 = new File(dir);
    	if(!file1.exists()){
    		file1.mkdirs();
    	}
    	file.screenImage(0, 0, screenWidth, screenHeight, dir + "\\" + System.currentTimeMillis()+".png");
    }
    
    public static String getDate(String fmt){
    	SimpleDateFormat sdf = new SimpleDateFormat(fmt);
    	return sdf.format(new Date());
    }
    
    public static List<CoordBean> findPic(String img,int sx,int sy,int ex,int ey,int maxDelay){
    	addLog("开始循环找图IMG【"+img+"】");
    	List<CoordBean> list = new ArrayList<CoordBean>();
    	int time = 0;
		while(time<maxDelay){
			list = findPic(img,sx, sy, ex, ey);
			if(list.size()>0){
				return list;
			}else{
				robot.delay(200);
				time+=200;
			}
		}
		addLog("结束找图IMG【"+img+"】" + list.size());
		return list;
    }
    
    public static void setScreenWidthAndHeight(int ex,int ey){
    	screenWidth = ex;
    	screenHeight = ey;
    }
    
    public static List<CoordBean> findPic(String img,int sx,int sy,int ex,int ey,double sim){
    	return findPic(img, sx, sy, ex, ey,sim,false);
    }
    
    public static List<CoordBean> findPic(String img,int sx,int sy,int ex,int ey){
    	return findPic(img, sx, sy, ex, ey,0.9,false);
    }
    
    public static List<CoordBean> findPic(String img,int sx,int sy,int ex,int ey,double sim,boolean log){
    	List<CoordBean> list = new ArrayList<CoordBean>();
    	String imgs = "";
    	if(img.indexOf("|") != -1){
    		String[] imgarr = img.split("\\|");
    		for(String item_img : imgarr){
    			imgs += getRealPath(item_img) + "|";	
    		}
    		imgs = imgs.substring(0, imgs.length() - 1);
    	}else{
    		imgs = getRealPath(img);
    	}
    	int[] a = findPic.findPic(sx,sy,ex,ey, imgs, "",sim, 0);
    	if(a[0] == -1){
    		if(log){
    			addLog("未找到IMG【"+imgs+"】");
    		}
    		return list;
    	}else{
    		CoordBean item = new CoordBean();
    		item.setX(a[1]);
    		item.setY(a[2]);
    		list.add(item);
    		addLog("找到IMG【"+imgs+"】坐標【"+item.getX()+","+item.getY()+"】..");
    	}
    	return list;
    }
    
    /**
     * 判断是否在子信托寻路
     * @return
     */
    public static boolean is_xunlu() {
    	List<CoordBean> list = Base.findPic(Constant.xunlu);
    	if(list.size() <= 0){
    		return false;
    	}
    	return true;
    }
    
    public static void xunlu(){
		boolean flag = true;
		Base.addLog("开始自动寻路中...");
		do{
			//自动寻路
			List<CoordBean> list = Base.findPic(Constant.xunlu);
			if(list.size() <= 0){
				//自动寻路完毕
    			Base.addLog("自动寻路完毕...");
				flag = false;
			}
			robot.delay(1000);
		}while(flag);
		Base.addLog("结束自动寻路中...");
    }
    
    public static void addLog(Object str){
    	log.info(str);
        System.out.println(str);
    }
    public static void addErrorLog(Object str){
    	log.error(str);
    }
    
    public static boolean go_to_lm(){
    	boolean f = false;
    	List<CoordBean> list = Base.findStrE("交易", 
				"bbbb06-3c3c07|bbbb13-3c3c13|bfbf13-404013|9a9782-36352f", 0.9, 0,1000);
		if(list.size() > 0){
			mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);
			f = true;
		}
		robot.delay(500);
		return f;
    }
    
    public static List<CoordBean> getWB_CLOSE(int time){
    	List<CoordBean> list = Base.findPic(Constant.wb_close+"|"+Constant.wb_close_1,time);//272 383
    	if(null != list && list.size() > 0){
    		CoordBean item = list.get(0);
        	item.setX(item.getX() + 5);
        	item.setY(item.getY() + 5);
        	return list;
    	}
    	return new ArrayList<CoordBean>();
    }
    
    public static void close_wb_page(){
    	List<CoordBean> list = getWB_CLOSE(2000);
		if(list.size() > 0){
			Base.addLog("执行关闭click..");
			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
		}else{
			Base.addLog("执行关闭ESC..");
			press.keyPress(press.ESC);
		}
		robot.delay(2000);
    }
    
    public static void resetUsers(){
    	Base.listUsers.clear();
		Base.xiaohao.clear();
		Base.listUserXiaoHao.clear();
    }
}
