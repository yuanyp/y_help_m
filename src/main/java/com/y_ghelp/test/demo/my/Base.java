package com.y_ghelp.test.demo.my;

import java.awt.TrayIcon;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.xnx3.microsoft.Color;
import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.FindPic;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.Window;
import com.xnx3.robot.Robot;
import com.xnx3.robot.support.CoordBean;
import com.y_ghelp.test.demo.my.wb.Layout;

public class Base{
	public static Com com;
	public static Window window;
	public static Mouse mouse;
	public static Press press;
	public static Color color;
	public static Robot robot;
	public static FindPic findPic;
	public static com.xnx3.microsoft.File file;
	
	public static String imgHomeFolder = "game_img";
	public static int screenWidth;
	public static int screenHeight;
	
	public static TrayIcon tray;
    
	public static Layout frame = null;
	
	public static String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    
	public static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
            new ThreadPoolExecutor.DiscardOldestPolicy());
	
	public static Thread start;
	public static boolean execute = false;
	public static List<String> listUsers = new ArrayList<String>();//判断账号是否已经处理过
	
    static{
        com = new Com();
        window = new Window(com.getActiveXComponent());    //窗口操作类
        mouse = new Mouse(com.getActiveXComponent());   //鼠标模拟操作类
        press = new Press(com.getActiveXComponent());   //键盘模拟操作类
        color = new Color(com.getActiveXComponent());   //颜色相关的取色、判断类
        findPic = new FindPic(com.getActiveXComponent());
        robot = new Robot();
        screenWidth = robot.screenWidth;
        screenHeight = robot.screenHeight;
        if(StringUtils.isNotBlank(path)){
            path = path.replace("/", "\\");
            path = path.substring(1, path.length());                
        }
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
    }
    
    public static void setImgHomeFolder(String imgHome){
    	imgHomeFolder = imgHome;
    	System.out.println("setImgHomeFolder " + imgHomeFolder);
    }
    
    public static String getRealPath(String img){
    	return path + imgHomeFolder + File.separator + img;
    }
    
    public static List<CoordBean> findPic(String img){
		return findPic(img,0,0,screenWidth,screenHeight);
    }
    
    public static void doubleClick(){
		mouse.mouseClick(true);
		robot.delay(200);
		mouse.mouseClick(true);
    }
    
    public static List<CoordBean> findPic(String img,int maxDelay){
    	List<CoordBean> list = new ArrayList<CoordBean>();
    	int time = 0;
		while(time<maxDelay){
			list = findPic(img,0, 0, screenWidth, screenHeight);
			if(list.size()>0){
				return list;
			}else{
				robot.delay(200);
				time+=200;
			}
		}
		return list;
    }
    
    public static void setScreenWidthAndHeight(int ex,int ey){
    	screenWidth = ex;
    	screenHeight = ey;
    }
    
    public static List<CoordBean> findPic(String img,int sx,int sy,int ex,int ey){
    	List<CoordBean> list = new ArrayList<CoordBean>();
    	int[] a = findPic.findPic(sx,sy,ex,ey, getRealPath(img), "", 0.9, 0);
    	if(a[0] != 0){
    		System.out.println("未能找到IMG【"+img+"】..");
    		return list;
    	}else{
    		CoordBean item = new CoordBean();
    		item.setX(a[1]);
    		item.setY(a[2]);
    		list.add(item);
    		System.out.println("找到IMG【"+img+"】坐標【"+item.getX()+","+item.getY()+"】..");
    	}
    	return list;
    }
}
