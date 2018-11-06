package com.y_ghelp.test.demo.my.wb;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.xnx3.robot.Robot;
import com.xnx3.robot.support.CoordBean;
import com.y_ghelp.test.demo.my.Base;
import com.y_ghelp.test.demo.my.Common;
import com.y_ghelp.test.demo.my.MYDemo;

public class SysHotKey implements HotkeyListener {
    
	public static final int start_huangjia = 0;//开始皇家
	public static final int start = 1;//开始挖宝
	public static final int end = 2;//结束
	public static final int state = 3;//切换窗口,显示\隐藏
	public static final int cut = 4;//截图
	public static final int test_search_img = 5;//测试找图
    
    JFrame frame;
    
    public SysHotKey(){}
    public SysHotKey(JFrame frame){
        this.frame=frame;
    }
    
    public void onHotKey(int key) {
        switch (key) {
	        case start_huangjia:
	            System.out.println("start_huangjia..");
	            WB.execute = true;
	            WB.threadPool.execute(WB.start_huangjia);
	            break;
            case start:
                System.out.println("start..");
                WB.execute = true;
                WB.threadPool.execute(WB.start);
//                WB.threadPool.execute(WB.checkDie);
                break;
            case end:
                System.out.println("end..");
                WB.execute = false;
                WB.threadPool.shutdown();
                destroy();
                System.exit(0);
                break;
            case cut:
                System.out.println("截图..");
                WB.screenImage("截图");
                break;
            case test_search_img:
            	Base.addLog("test_search_img start..");
            	List<CoordBean> list = Base.findStrE("传送", "30eb37-311437", 0.8, 0);
            	Base.addLog("list.size" + list.size());
        		for(CoordBean item : list){
        			System.out.println(item.getX());
        			System.out.println(item.getY());
        		}
        		Base.addLog("test_search_img end..");
                break;
            case state:
                if(frame.getState() != Frame.ICONIFIED){
                    frame.setExtendedState(Frame.ICONIFIED);
                    frame.setVisible(false);
                }else{
                    frame.setExtendedState(Frame.NORMAL);
                    frame.setVisible(true);
                }
                String fromState = (frame.getState()==0)?"最大化":"最小化";
                System.out.println("失去焦点,的快捷键注册win+a\t现在窗口"+fromState);
                break;
        }

    }
    
    /**
     * 解除注册
     * */
    public static void destroy() {
    	JIntellitype.getInstance().unregisterHotKey(start_huangjia);
        JIntellitype.getInstance().unregisterHotKey(start);
        JIntellitype.getInstance().unregisterHotKey(end);
        JIntellitype.getInstance().unregisterHotKey(cut);
        JIntellitype.getInstance().unregisterHotKey(test_search_img);
        JIntellitype.getInstance().unregisterHotKey(state);
        System.exit(0);
    }
    
    /**
     * 开启注册
     * 
     */
    void initHotkey() {
    	JIntellitype.getInstance().registerHotKey(start_huangjia,"ALT+F8");
        JIntellitype.getInstance().registerHotKey(start,"ALT+F9");
        JIntellitype.getInstance().registerHotKey(end,"ALT+F10");
        JIntellitype.getInstance().registerHotKey(cut,"ALT+F11");
        JIntellitype.getInstance().registerHotKey(test_search_img,"ALT+F12");
        JIntellitype.getInstance().registerHotKey(state, JIntellitype.MOD_ALT ,(int) 'A');
        JIntellitype.getInstance().addHotKeyListener(this);
    }
}