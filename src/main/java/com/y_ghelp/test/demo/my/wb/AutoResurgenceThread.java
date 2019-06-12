package com.y_ghelp.test.demo.my.wb;

import java.util.ArrayList;
import java.util.List;

import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Sleep;
import com.xnx3.robot.support.CoordBean;
import com.y_ghelp.test.demo.my.Base;

public class AutoResurgenceThread implements Runnable{
	private int die_count = 0;
	private boolean die = false;//判断人物是否死亡
	private int sx = 0;
	private int sy = 0;
	private int ex = 800;
	private int ey = 600;
	public int zIndex2;
	private Sleep sleep;
	private Mouse mouse;
	
	public AutoResurgenceThread(Mouse mouse,Sleep sleep,int zIndex2) {
		this.mouse = mouse;
		this.sleep = sleep;
		this.zIndex2 = zIndex2;
	}
	
	public Mouse getMouse() {
		return mouse;
	}

	public void setMouse(Mouse mouse) {
		this.mouse = mouse;
	}

	public Sleep getSleep() {
		return sleep;
	}

	public void setSleep(Sleep sleep) {
		this.sleep = sleep;
	}

	public boolean die(List<CoordBean> _list) {
		if(null != _list){
			_list.clear();
		}
		sleep.sleep(500);
		List<CoordBean> list = Base.findPic(Constant.die,sx,sy,ex,ey,0.9,false);
		if(list.size() > 0){
			Base.screenDieImage();
			WB.addErrorLog("检测到人物死亡【"+Base.runRole.toString()+"】");
			_list.addAll(list);//返回坐标
			die = true;
			return true;
		}
		die = false;
		return false;
	}
	
	@Override
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
        			sleep.sleep(200);
        			int x = list.get(0).getX() + 5;
        			int y = list.get(0).getY() + 95;
        			Base.addLog("鼠标点击复活坐标1【"+x+","+y+"】..");
    				mouse.mouseClick(x, y, true);
	        		if(zIndex2 == 2) {
	        			sleep.sleep(500);
						//68 86
	    				int x1 = list.get(0).getX() - 68;
	    				int y1 = list.get(0).getY() + 86;
	    				Base.addLog("鼠标点击复活坐标2【"+x1+","+y1+"】..");
	    				mouse.mouseClick(x1, y1, true);
	        		}
        		}else {
        			die = false;
        			flag = false;
        		}
        	}while(flag);
        	die_count = die_count + 1;//复活次数+1
        	Base.addLog("复活次数+1...【"+die_count+"】");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void clearData(){
    	die_count = 0;
    }
	
	public boolean maxDieNum() {
		if(die_count >= 5){
			Base.addLog("复活次数大于等于5次直接返回...");
			return true;
		}
		return false;
	}
}