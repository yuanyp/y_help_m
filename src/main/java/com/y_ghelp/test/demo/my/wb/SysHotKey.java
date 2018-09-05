package com.y_ghelp.test.demo.my.wb;

import java.awt.Frame;
import java.util.List;

import javax.swing.JFrame;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.xnx3.robot.support.CoordBean;
import com.y_ghelp.test.demo.my.Base;

public class SysHotKey implements HotkeyListener {
    
    final int start = 1;//开始
    final int end = 2;//结束
    final int state = 3;//切换窗口,显示\隐藏
    final int cut = 4;//截图
    final int test_search_img = 5;//测试找图
    
    JFrame frame;
    
    public SysHotKey(){}
    public SysHotKey(JFrame frame){
        this.frame=frame;
    }
    
    public void onHotKey(int key) {
        switch (key) {
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
                WB.screenImage();
                break;
            case test_search_img:
            	Base.addLog("test_search_img start..");
            	List<CoordBean> list = Base.findStrE("雷鸣", 
        				"9a957d-565247", 1, 0,1000);
        		if(list.size() > 0){
        			Base.addLog("当前人物在雷鸣");
        			for(CoordBean item : list){
        				System.out.println("雷鸣:" + item);
	            	}
        			return;
        		}else{
        			list = Base.findStrE("遗忘", 
        					"a6a08a-4b473b", 1, 0,1000);
        			if(list.size() > 0){
        				for(CoordBean item : list){
            				System.out.println("遗忘" + item);
    	            	}
        				Base.mouse.mouseMoveTo(list.get(0).getX(), list.get(0).getY());
        				Base.robot.delay(500);
        				Base.addLog("当前人物在神界");
        				//检查是否已经到了神界
        				list = Base.findStrE("神界", 
        						"bbbb06-3c3c07|bbbb13-3c3c13|bfbf13-404013", 1, 0,1000);
        				if(list.size() > 0){//50 80
        					for(CoordBean item : list){
                				System.out.println("神界" + item);
        	            	}
        				}else{
        					Base.addLog("没有回到神界");
        		    		return;
        				}
        			}
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
    void destroy() {
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
        JIntellitype.getInstance().registerHotKey(start,"ALT+F9");
        JIntellitype.getInstance().registerHotKey(end,"ALT+F10");
        JIntellitype.getInstance().registerHotKey(cut,"ALT+F11");
        JIntellitype.getInstance().registerHotKey(test_search_img,"ALT+F12");
        JIntellitype.getInstance().registerHotKey(state, JIntellitype.MOD_ALT ,(int) 'A');
        JIntellitype.getInstance().addHotKeyListener(this);
    }
}