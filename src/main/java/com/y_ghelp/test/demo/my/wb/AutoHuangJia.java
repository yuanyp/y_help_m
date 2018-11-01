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
    	List<CoordBean> list = Base.findPic(Constant.wb_0,10000);
    	if(list.size() > 0){
    		robot.delay(2000);
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	robot.delay(2000);
        	list = get_huangjia_go();
        	if(list.size() > 0){
        		//x+371,y-8
        		robot.delay(200);
        		mouse.mouseMoveTo(list.get(0).getX() + 365, list.get(0).getY() + 28);
        		robot.delay(500);
        		mouse.mouseClick(list.get(0).getX() + 365, list.get(0).getY() + 28, true);
        		robot.delay(1000);
        		//判断是否在寻路
        		list = Base.findPic(Constant.xunlu);
    			if(list.size() <= 0){
    				//检查是否已经到了神界
    				list = Base.findStrE("神界", 
    						"bbbb06-3c3c07|bbbb13-3c3c13|bfbf13-404013", 0.9, 0,3000);
    				if(list.size() > 0){
    					Base.addLog("当前人物在神界");
    					press.keyPressTime(press.W, 380);
    					robot.delay(500);
    					press.keyPress(press.SPACE);
    					robot.delay(800);
    					boolean a = Base.go_to_lm();
    					if(a){
    						Base.addLog("点击传送到雷鸣");
    					}else{
    						Base.addLog("没有找到传送到雷鸣的对话框");
    					}
    				}else{
    					Base.addLog("不在神界");
    				}
    			}else{
    				Base.close_wb_page();
    			}
        	}else{
        		Base.close_wb_page();
        	}
    	}else{
    		Base.addLog("没有找到wb_0");
    		return;
    	}
    	
    	Base.addLog("开始皇家..");
    	boolean go_to_npc = go_to_npc();
    	if(go_to_npc){
    		clear_monster();
        	boolean flag = go_to_4();
        	if(flag) {
        		go_back();	
        	}else {
        		Base.addLog("未能到达第四层..");
        	}
    	}else{
    		Base.close_wb_page();
    	}
    	list = Base.findStrE("确定","c7bd97-383e38", 1, 0,5000);
    	if(list.size() > 0){
			mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);
    	}
    	Base.addLog("结束皇家..");
    }
    
    /**
     * 返回
     * @return
     */
    private void go_back(){
    	Base.addLog("返回雷鸣大陆..");
    	mouse.mouseClick(579, 207, true);
    	robot.delay(1000);
    	mouse.mouseClick(86, 338, true);
    }
    
    public List<CoordBean> get_chuansong_men(){
    	List<CoordBean> list = Base.findStrE("传", "29ce21-2a3121", 0.9, 1000);//= Base.findStrE("传|传", "29ce21-2a3121|0bb10b-0b4e0c", 0.9, 1000);
    	if(null == list || list.size() == 0){
    		list = Base.findStrE("传", "1e9614-1e6914", 0.9, 1000);
    		if(null == list || list.size() == 0){
    			list = Base.findPic(Constant.huangjia_1_chuansongmen);
    		}
    	}
    	return list;
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
        	list = get_chuansong_men();
        	if(list.size() > 0 || i >= 20) {
        		flag = false;
        	}
    	}while(flag);
    	
    	if(null != list && list.size() > 0) {
    		//TODO
    		
    		Base.addLog("找到传送门，并传送..");
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		robot.delay(3000);
    		//往下走一点
    		press.keyPressTime(press.S, 300);
    		robot.delay(500);
    		press.keyPress(press.SPACE);
    		robot.delay(1000);
    		//查找对话框
    		list = Base.findPic(Constant.huangjia_1_chuansongmen_1,1000);
    		if(null == list || list.size() == 0){
    			list = Base.findStrE("4","bab69e-242420", 0.9, 0,1000);
    		}
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
    
    
    public List<CoordBean> get_huangjia_go(){
    	List<CoordBean> list = Base.findStrE("武场","a4a5a6-555453", 1, 3000);
    	if(null == list || list.size() == 0){
    		list = Base.findPic(huangjia_go());
    	}
    	return list;
    }
    
    private String huangjia_go(){
    	return Constant.huangjia_go+"|"+Constant.huangjia_go_1+"|"+Constant.huangjia_go_2+"|"+Constant.huangjia_go_3;
    }
    
    /**
     * 前往皇家NPC
     */
    private boolean go_to_npc() {
    	Base.addLog("前往皇家NPC..");
    	List<CoordBean> list = Base.findPic(Constant.wb_0,10000);
    	if(list.size() > 0){
    		Base.addLog("点击活动中心..");
    		robot.delay(2000);
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}
    	robot.delay(2000);
    	//点击前往皇家NPC按钮
    	list = get_huangjia_go();
    	if(list.size() > 0) {
    		Base.addLog("点击活动中心的前往“皇家”按钮..");//365 28
    		mouse.mouseClick(list.get(0).getX() + 365, list.get(0).getY() + 28, true);
    	}else{
    		return false;
    	}
    	//开始寻路
    	list = Base.findPic(Constant.xunlu,1000);
    	if(list.size() > 0){
    		Base.xunlu();
    	}else{
    		return false;
    	}
    	//检查是否已经到了NPC身边（与NPC对话的关闭按钮）
		Base.addLog("与皇家NPC对话，点击前往皇家..");
		mouse.mouseClick(65, 339, true);
    	robot.delay(1000);
    	//再次确认框
		Base.addLog("与皇家NPC对话，再次确认前往皇家..");
		mouse.mouseClick(71, 340, true);
    	robot.delay(5000);
    	//620 180
    	mouse.mouseClick(620, 180, true);
    	robot.delay(1000);
    	mouse.mouseClick(82, 385, true);
    	robot.delay(1000);
    	mouse.mouseClick(75, 338, true);
    	//进入皇家第一层
    	Base.addLog("进入皇家第一层..");
    	return true;
    }
    
    /**
     * 清理怪物
     */
    private void clear_monster() {
    	Base.addLog("开始清理怪物..");
    	mouse.mouseClick(701, 353,true);
    	robot.delay(3000);
    	for(int k=0,v=3;k<v;k++){
    		boolean flag = true;
    		do {
        		press.keyPress(press.SPACE);
            	boolean a = Base.isMove(3);
            	if(a) {
            		Base.addLog("人物有移动继续清理怪物..");
            		robot.delay(2000);
            		//人物有移动
            		for(int i=0,j=3;i<j;i++) {
                		press.keyPress(press.F3);
                		robot.delay(2000);
            		}
            	}else {
            		//怪物清理完毕,跳出循环
            		flag = false;
            	}
        	}while(flag);
    	}
    	Base.addLog("结束清理怪物..");
    }
	

}
