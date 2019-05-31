package com.y_ghelp.test.demo.my;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.xnx3.bean.ActiveBean;
import com.xnx3.microsoft.Color;
import com.xnx3.microsoft.File;
import com.xnx3.microsoft.FindPic;
import com.xnx3.microsoft.FindStr;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.robot.Robot;
import com.xnx3.robot.support.CoordBean;

public class Util {
	
	Logger logger = Logger.getLogger(Util.class);
	
	public Mouse mouse;
	public Press press;
	public Color color;
	public Robot robot;
	public FindPic findPic;
	public FindStr findStr;
	public File file;
	
	
	public Util(ActiveBean activeBean) {
		this.mouse = new Mouse(activeBean);   //鼠标模拟操作类
		this.press = new Press(activeBean);   //键盘模拟操作类
		this.color = new Color(activeBean);   //颜色相关的取色、判断类
		this.findPic = new FindPic(activeBean);
		this.findStr = new FindStr(activeBean);
		this.file = new com.xnx3.microsoft.File(activeBean);
	}
	
	
    public static String getRealPath(String img){
    	return Config.getPath() + Config.getImg_folder_name() + java.io.File.separator + img;
    }
    
    public List<CoordBean> findPic(String img){
		return findPic(img,0,0,Config.getScreenWidth(),Config.getScreenHeight());
    }
    
    public List<CoordBean> findStrE(String str,String color,double sim,int useDict){
    	return findStrE(str,Config.getScreenWidth(),Config.getScreenHeight(),color, sim, useDict);
    }
    
    public List<CoordBean> findStrE(String str,String color,double sim,int useDict,int maxDelay){
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
    
    public List<CoordBean> findStrE(String str,int w,int h,String color,double sim,int useDict){
		int[] strs = findStr.findStrE(0, 0, w, h, str, color, sim, useDict);
		CoordBean item = new CoordBean();
		List<CoordBean> list = new ArrayList<CoordBean>();
		if(strs[0] != -1){
			item.setX(strs[1]);
			item.setY(strs[2]);
			list.add(item);
		}
		return list;
    }

    public void doubleClick(){
		mouse.mouseClick(true);
		robot.delay(200);
		mouse.mouseClick(true);
    }
    
    public List<CoordBean> findPic(String img,int maxDelay){
    	return findPic(img,0,0,Config.getScreenWidth(),Config.getScreenHeight(),maxDelay);
    }
    
    public void screenImage(String name){
    	String dir = Config.home + java.io.File.separator + name;
    	java.io.File file1 = new java.io.File(dir);
    	if(!file1.exists()){
    		file1.mkdirs();
    	}
    	file.screenImage(0, 0, Config.getScreenWidth(), Config.getScreenHeight(), dir + "\\" + System.currentTimeMillis()+".png");
    }

    
    public String getDate(String fmt){
    	SimpleDateFormat sdf = new SimpleDateFormat(fmt);
    	return sdf.format(new Date());
    }
    
    public List<CoordBean> findPic(String img,int sx,int sy,int ex,int ey,int maxDelay){
    	logger.info("开始循环找图IMG【"+img+"】");
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
		logger.info("结束找图IMG【"+img+"】" + list.size());
		return list;
    }

    
    public List<CoordBean> findPic(String img,int sx,int sy,int ex,int ey,double sim){
    	return findPic(img, sx, sy, ex, ey,sim,false);
    }
    
    public List<CoordBean> findPic(String img,int sx,int sy,int ex,int ey){
    	return findPic(img, sx, sy, ex, ey,0.9,false);
    }
    
    public List<CoordBean> findPic(String img,int sx,int sy,int ex,int ey,double sim,boolean log){
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
    			logger.warn("未找到IMG【"+imgs+"】");
    		}
    		return list;
    	}else{
    		CoordBean item = new CoordBean();
    		item.setX(a[1]);
    		item.setY(a[2]);
    		list.add(item);
    		logger.info("找到IMG【"+imgs+"】坐標【"+item.getX()+","+item.getY()+"】..");
    	}
    	return list;
    }
	

}
