package com.y_ghelp.test.demo.my.wb;

import java.util.List;

import com.xnx3.microsoft.Color;
import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.FindPic;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.Window;
import com.xnx3.robot.Robot;
import com.xnx3.robot.support.CoordBean;
import com.y_ghelp.test.demo.my.Base;

/**
 * 自动打皇家第一图（到第四层就出图）
 * @author yuanyp
 *
 */
public class AutoHuangJia {
	Com com = Base.com;
    Window window = Base.window;
    Mouse mouse = Base.mouse;
    Press press = Base.press;
    Color color = Base.color;
    Robot robot = Base.robot;
    FindPic findPic = Base.findPic;
    com.xnx3.microsoft.File file = Base.file;
	
	/**
	 * --1. 获取坐标
	--2. 190\275 传送门坐标 一直往右移动 (202\271|205,273) 然后往上移动 X会减少 
	
	0. 自动前往皇家
	1. 按 普通攻击能自动打怪
	2. 打完怪物之后(判断任务按普通攻击不移动了,就说明打完怪物了)  先往上,再往右 过程中循环找 传送门
	3. 到第4层之后 点击固定坐标传送出来,点击宝箱
	4. 切换账号
	 */
    public void execute() {
    	Base.addLog("开始皇家..");
    	go_to_npc();
    	clear_monster();
    	boolean flag = go_to_4();
    	if(flag) {
    		go_back();	
    	}else {
    		Base.addLog("未能到达第四层..");
    	}
    	Base.addLog("结束皇家..");
    }
    
    /**
     * 返回
     * @return
     */
    private void go_back(){
    	Base.addLog("返回雷鸣大陆..");
    	mouse.mouseClick(800, 600, true);
    }
    
    /**
     * 前往皇家第4层
     */
    private boolean go_to_4(){
    	//开始找传送门
    	Base.addLog("开始找传送门..");
    	boolean flag = true;
    	int i = 0;//循环找传送门次数(超过20次跳出循环)
    	int key = press.W;
    	List<CoordBean> list = null;
    	do {
    		i++;
    		Base.addLog("往上走1秒..");
    		if(i >= 10) {
    			key = press.D;
    		}
        	press.keyPressTime(key, 500);
        	list = Base.findPic(Constant.huangjia_1_chuansongmen);
        	if(list.size() > 0 || i >= 20) {
        		flag = false;
        	}
    	}while(flag);
    	
    	if(null != list && list.size() > 0) {
    		Base.addLog("找到传送门，并传送..");
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		robot.delay(1000);
    		//查找对话框
    		list = Base.findPic(Constant.huangjia_1_chuansongmen_1);
    		if(list.size() > 0) {
    			Base.addLog("点击传送门的对话框，执行传送..");
    			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    			robot.delay(3000);
    		}
    		return true;
    	}
    	Base.addLog("未能找到传送门..");
    	return false;
    }
    
    /**
     * 前往皇家NPC
     */
    private void go_to_npc() {
    	Base.addLog("前往皇家NPC..");
    	List<CoordBean> list = Base.findPic(Constant.wb_0,17000);
    	if(list.size() > 0){
    		Base.addLog("点击活动中心..");
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}
    	robot.delay(2000);
    	//点击前往皇家NPC按钮
    	list = Base.findPic(Constant.huangjia_go,3000);
    	if(list.size() > 0) {
    		Base.addLog("点击活动中心的前往“皇家”按钮..");
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}
    	//开始寻路
    	Base.xunlu();
    	//检查是否已经到了NPC身边（与NPC对话的关闭按钮）
    	list = Base.findPic(Constant.huangjia_go_npc,3000);
    	if(list.size() > 0) {
    		Base.addLog("与皇家NPC对话，点击前往皇家..");
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}
    	robot.delay(1000);
    	//再次确认框
    	list = Base.findPic(Constant.huangjia_go_npc_confirm,3000);
    	if(list.size() > 0) {
    		Base.addLog("与皇家NPC对话，再次确认前往皇家..");
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}
    	robot.delay(3000);
    	//进入皇家第一层
    	Base.addLog("进入皇家第一层..");
    }
    
    /**
     * 清理怪物
     */
    private void clear_monster() {
    	Base.addLog("开始清理怪物..");
    	boolean flag = true;
    	do {
    		press.keyPress(press.SPACE);
        	boolean a = Base.isMove(2);
        	if(a) {
        		Base.addLog("人物有移动继续清理怪物..");
        		robot.delay(1000);
        		//人物有移动
        		for(int i=0,j=3;i<j;i++) {
            		press.keyPress(press.F3);
            		robot.delay(800);
        		}
        	}else {
        		//怪物清理完毕,跳出循环
        		flag = false;
        	}
    	}while(flag);
    	Base.addLog("结束清理怪物..");
    }
	

}
