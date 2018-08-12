package com.y_ghelp.test.demo.my.wb;

import java.awt.Frame;

import javax.swing.JFrame;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

public class SysHotKey implements HotkeyListener {
    
    final int start = 1;//开始
    final int end = 2;//结束
    final int state = 3;//切换窗口,显示\隐藏
    
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
                break;
            case end:
                System.out.println("end..");
                WB.execute = false;
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
        JIntellitype.getInstance().registerHotKey(state, JIntellitype.MOD_ALT ,(int) 'A');
        JIntellitype.getInstance().addHotKeyListener(this);
    }
}