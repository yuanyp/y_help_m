package com.y_ghelp.test.demo.my;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

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
/**
 * 
 * win8 || win10 需要手动注册；用管理员打开cmd 
 * regsvr32 C:\\dm.dll
 * regsvr32 C:\\Plug365New.dll
 *
 */
public class MYDemo extends JFrame{
	
	public MYDemo() {
//		init_layout();
	}
	
	static Logger log = Logger.getLogger(MYDemo.class);
	String gameName = (String) MYConfig.getInstance().getConfig("gameName");
    String defaultGamePath = (String) MYConfig.getInstance().getConfig("defaultGamePath");
    
    private MYTwoStars twoStars;
    
    private JPanel contentPane;
    private JTextArea textArea;
    private JTextField textField;
    private JTextField textField1;
    private JButton button_start_yongsheng;
    private boolean m3_end = false;
    
    private int m_var = 120 + 28;
    private int rw_hight = 130;
    Com com;
    Window window;
    Mouse mouse;
    Press press;
    Color color;
    Robot robot;
    FindPic findPic;
    com.xnx3.microsoft.File file;
    
    
    
    Thread openGameTh;
    Thread playGameTh;
    Thread sclTh;
    Thread m3startTh;
    Thread testTh;
    List<String> listUsers = new ArrayList<String>();//判断账号是否已经处理过
    
    String btnStartName = "启动";
    
    String btnContinueName = "继续";
    String btnStopName = "暂停";
    String path;
    String img_folder_name = "game_img";
    
    boolean flag_dx = true;
    boolean flag_gw = true;
    
    // 构造一个线程池
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
            new ThreadPoolExecutor.DiscardOldestPolicy());
    private JTextField m3_textField_cd;
    private JTextField m3_textField_x;
    private JTextField m3_textField_y;
    
    private void init(){
        com = new Com();
        window = new Window(com.getActiveXComponent());    //窗口操作类
        mouse = new Mouse(com.getActiveXComponent());   //鼠标模拟操作类
        press = new Press(com.getActiveXComponent());   //键盘模拟操作类
        color = new Color(com.getActiveXComponent());   //颜色相关的取色、判断类
        findPic = new FindPic(com.getActiveXComponent());
        file = new com.xnx3.microsoft.File(com.getActiveXComponent());
        
        path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        if(StringUtils.isNotBlank(path)){
            path = path.replace("/", "\\");
            path = path.substring(1, path.length());                
        }
        PropertyConfigurator.configure(path + "log4j.properties");
        robot = new Robot();
        if(!com.isCreateSuccess()){
            addLog("创建Com对象失败");
            return;
        }
        Toolkit tk = Toolkit.getDefaultToolkit();  
        tk.addAWTEventListener(new ImplAWTEventListener(), AWTEvent.KEY_EVENT_MASK);  
    }
    
    private void init_layout(){
    	this.setBounds(100, 100, 645, 445);
        contentPane = new JPanel();
        this.setContentPane(contentPane);
        JLabel lblNewLabel = new JLabel("应用路径");
        
        //应用程序路径URL
        textField = new JTextField();
        textField.setText(defaultGamePath);
        textField.setColumns(10);
        
        //測試找圖
        textField1 = new JTextField();
        textField1.setText("qi.bmp");
        
        openGameTh = new Thread(new Runnable() {
            public void run() {
                try {
                    // 打开应用,此函数会阻塞当前线程，直到打开的关闭为止。故而须另开辟一个线程执行此函数
                    String cmdExe = " start \"\" \"" + getAppPath() + "\"";
                    addLog(cmdExe);
                    SystemUtil.cmd(cmdExe);
                } catch (Exception e) {
                    addLog(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        
        m3startTh = new Thread(new Runnable() {
            public void run() {
                try {
                    activeGame();
                    robot.delay(1000);
                    if(button_start_yongsheng.getText().equals("开始")){
                        button_start_yongsheng.setText("暂停");
                        m3_start_yongsheng();
                    }else if(button_start_yongsheng.getText().equals("暂停")){
                        button_start_yongsheng.setText("开始");
                        m3_end_yongsheng();
                    }
                } catch (Exception e) {
                    addLog(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        
        testTh = new Thread(new Runnable() {
            public void run() {
                try {
                    activeGame();
                    robot.delay(1000);
                    goto_xy();
                } catch (Exception e) {
                    addLog(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        
        playGameTh = new Thread(new Runnable() {
            public void run() {
                try {
                    readStart();
                } catch (Exception e) {
                    addLog(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        
        //找图
        final JButton btnSearchImg = new JButton("找图");
        btnSearchImg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	while(true){
            		List<CoordBean> list = findPic(textField1.getText());
                	if(null != list){
                		mouse.mouseMoveTo(list.get(0).getX(), list.get(0).getY());
                	}
                	robot.delay(1000);
            	}
            }
        });
        
        //退出
        final JButton exitSearchImg = new JButton("退出");
        exitSearchImg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //用完后一定要记得释放，释放内存
                com.unbind();
                System.exit(0);
            }
        });
        
        //刷金字塔1
        final JButton button_jzt_1 = new JButton("\u5237\u91D1\u5B57\u58541");
        button_jzt_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    shua_jzt(1);
                } catch (Exception e1) {
                    addLog(e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        
        //刷金字塔4
        final JButton button_jzt_4 = new JButton("\u5237\u91D1\u5B57\u58544");
        button_jzt_4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    shua_jzt(4);
                } catch (Exception e1) {
                    addLog(e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        
        //刷材料
        final JButton btnJZT = new JButton("刷幻界大陆第二、三图");
        btnJZT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sclTh = new Thread(new Runnable() {
                        public void run() {
                            try {
                            	shuacail_main();
                            } catch (Exception e) {
                                addLog(e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
                    threadPool.execute(sclTh);
                } catch (Exception e1) {
                    addLog(e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        
        //启动应用程序
        final JButton btnNewButton = new JButton("启动");
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(btnNewButton.getText().equals(btnStartName)){
                        btnNewButton.setText(btnStopName);
                        threadPool.execute(playGameTh);
                    } else if (btnNewButton.getText().equals(btnStopName)){
                        btnNewButton.setText(btnStartName);
                        threadPool.shutdownNow();
                    }
                } catch (Exception e1) {
                    addLog(e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        
        //日志
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        
        JButton button_jzt_2 = new JButton("\u5237\u91D1\u5B57\u58542");
        button_jzt_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    shua_jzt(2);
                } catch (Exception e1) {
                    addLog(e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        
        JButton button_jzt_3 = new JButton("\u5237\u91D1\u5B57\u58543");
        button_jzt_3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    shua_jzt(3);
                } catch (Exception e1) {
                    addLog(e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        
        JButton button_mi3 = new JButton("刷永生");
        button_mi3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	activeGame();
                	robot.delay(1000);
                	shua_m3_zoulu();
                } catch (Exception e1) {
                    addLog(e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        
        button_start_yongsheng = new JButton("开始");
        button_start_yongsheng.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                threadPool.execute(m3startTh);
            }
        });
        
        m3_textField_cd = new JTextField();
        m3_textField_cd.setText("1");
        m3_textField_cd.setColumns(10);
        
        m3_textField_x = new JTextField();
        m3_textField_x.setText("-50");
        m3_textField_x.setColumns(10);
        
        m3_textField_y = new JTextField();
        m3_textField_y.setText("280");
        m3_textField_y.setColumns(10);
        
        JLabel label = new JLabel("冷却时间");
        
        JLabel lblX = new JLabel("X");
        
        JLabel lblY = new JLabel("Y");
        
        JButton button_test = new JButton("测试中间坐标");
        button_test.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                threadPool.execute(testTh);
            }
        });
        
        //布局
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addGap(21)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_contentPane.createSequentialGroup()
                                    .addComponent(button_jzt_1, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(button_jzt_2, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(button_jzt_3, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(button_jzt_4))
                                .addGroup(gl_contentPane.createSequentialGroup()
                                    .addComponent(btnNewButton)
                                    .addPreferredGap(ComponentPlacement.UNRELATED)
                                    .addComponent(exitSearchImg))))
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addComponent(lblNewLabel)
                            .addGap(5)
                            .addComponent(textField, GroupLayout.PREFERRED_SIZE, 533, GroupLayout.PREFERRED_SIZE)))
                    .addGap(99))
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(textArea, GroupLayout.PREFERRED_SIZE, 606, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(90, Short.MAX_VALUE))
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addGap(23)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                            .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(btnJZT)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(button_mi3))
                            .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(btnSearchImg)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(textField1, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                                .addGap(284)))
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                .addComponent(label, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                                .addComponent(m3_textField_cd, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(ComponentPlacement.UNRELATED)
                            .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_contentPane.createSequentialGroup()
                                    .addComponent(m3_textField_x, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
                                    .addGap(15)
                                    .addComponent(m3_textField_y, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
                                    .addGap(18)
                                    .addComponent(button_start_yongsheng)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(button_test))
                                .addGroup(gl_contentPane.createSequentialGroup()
                                    .addGap(12)
                                    .addComponent(lblX, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                                    .addGap(18)
                                    .addComponent(lblY, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)))
                            .addGap(319))))
        );
        gl_contentPane.setVerticalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addGap(9)
                            .addComponent(lblNewLabel))
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addGap(6)
                            .addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                    .addGap(18)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(btnNewButton)
                        .addComponent(exitSearchImg))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(button_jzt_1)
                        .addComponent(button_jzt_4)
                        .addComponent(button_jzt_2)
                        .addComponent(button_jzt_3))
                    .addGap(14)
                    .addComponent(textArea, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
                    .addGap(13)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(label)
                        .addComponent(lblX)
                        .addComponent(lblY))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(m3_textField_cd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(m3_textField_x, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(button_start_yongsheng)
                        .addComponent(m3_textField_y, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(button_test))
                    .addGap(38)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(btnSearchImg)
                        .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(btnJZT)
                        .addComponent(button_mi3))
                    .addGap(286))
        );
        contentPane.setLayout(gl_contentPane);
    }
    
    private void m3_start_yongsheng(){
        addLog("m3_start_yongsheng...");
        m3_end = true;
        while(m3_end){
            robot.delay(500);
            addLog("m3_start_yongsheng work...");
            robot.press(press.F2);//放技能
            robot.delay(800);
            robot.press(press.F3);//放技能
            robot.delay(6000);
            goto_xy();//回到中间
            int cd = Integer.parseInt(m3_textField_cd.getText()) * 1000;
            for(int i=0,j=5;i<j;i++){
            	shua_m3jy();
            	goto_xy();
			}
            robot.delay(cd);//等CD
        }
    }
    
    private void jian_yongsheng(){
        boolean f = true;
        while(f){
            List<CoordBean> c1 = findPic(Common.m3_yongsheng);
            if((null != c1 && c1.size() > 0)){
                log.info("找到材料");
                robot.press(press.F4);
                log.info("按下F4");
                robot.delay(500);
                List<CoordBean> c2 = findPic(Common.m3_yuangushouling);
                robot.delay(200);
                List<CoordBean> c3 = findPic(Common.m3_shuguanglaoying);
                robot.delay(200);
                if( (null != c2 && c2.size() > 0) || 
                        (null != c3 && c3.size() > 0) ){
                    log.info("找到材料");
                    robot.press(press.F4);
                    log.info("按下F4");
                    robot.delay(500);
                }
            }else{
                f = false;
            }
        }
    }
    
    private void goto_xy(){
        addLog("goto_xy start ..");
        List<CoordBean> list = new ArrayList<CoordBean>();
        boolean jiao1 = findImg(Common.m3_jiao1_1, 1000, list);
        if(jiao1){
            int x = Integer.parseInt(m3_textField_x.getText());
            int y = Integer.parseInt(m3_textField_y.getText());
            mouse.mouseClick(list.get(0).getX() + x, list.get(0).getY() + y, true);
        }else if(findImg(Common.m3_jiao1_2, 1000, list)){
        	mouse.mouseClick(list.get(0).getX() + 450 , list.get(0).getY(), true);
        }
        robot.delay(500);
    }
    
    private void m3_end_yongsheng(){
        addLog("m3_end_yongsheng...");
        m3_end = false;
    }
    
    /**
     * 刷金字塔
     */
    private void shua_jzt(int num){
    	int hwnd = window.findWindow(0, null, gameName);
        if(hwnd > 0){ 
            window.moveWindow(hwnd, 0, 0);
            window.setWindowActivate(hwnd); //激活窗口
            
            robot.delay(1000);
            
//            List<CoordBean> list = new ArrayList<CoordBean>();
//            if(findImg(zhuziImg, 500, list)){
//            	mouse.mouseClick(list.get(0).getX(),list.get(0).getY() + 150,true);
//            }
//            robot.delay(1000);
//            //点击对话框释放怪物
//            list = findPic(fangguaiwuImg);
//            if(null == list){
//            	addLog("找不到图片["+fangguaiwuImg+"]");
//            	return;
//            }
//            mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5,true);
//            robot.delay(500);
        	daguai();
        	
        	if(num == 1){
        		//金字塔第1图
        		move1();
        	}
        	
        	if(num == 2){
        		//金字塔第2图
        		move2();
        	}
        	
        	if(num == 3){
        		//金字塔第3图
        		move3();
        	}
        	
        	if(num == 4){
        		//金字塔第4图
        		move4();
        	}
        	
        }else{
            addLog("未能找到游戏【"+gameName+"】...");
        }
    }
    
    private void shua_m3_rukou(){
    	shua_m3();
    	List<CoordBean> list = new ArrayList<CoordBean>();
    	boolean jiao1 = findImg(Common.m3_jiao1, 1000, list);
    	if(jiao1){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY() - 140, true);
    		robot.delay(4000);
    	}
    	shua_m3();
    	
    	boolean jiao2 = findImg(Common.m3_jiao2, 1000, list);
    	if(jiao2){
    		mouse.mouseClick(list.get(0).getX() + 70, list.get(0).getY(), true);
    		robot.delay(4000);
    	}
    	
    	shua_m3();
    	boolean jiao3 = findImg(Common.m3_jiao3, 1000, list);
    	if(jiao3){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		robot.delay(4000);
    	}
    	shua_m3();
    	
    	boolean jiao4 = findImg(Common.m3_jiao4, 1000, list);
    	if(jiao4){
    		mouse.mouseClick(list.get(0).getX() - 70, list.get(0).getY(), true);
    		robot.delay(4000);
    	}
    	shua_m3();
    }
    
    private void shua_m3_zoulu(){
    	while(true){
    		moveLeft();
    		moveLeft();
    		shua_m3_rukou();
    		moveLeft();
    		moveLeft();
    		shua_m3_rukou();
    		moveLeft();
    		moveLeft();
    		shua_m3_rukou();
    		
    		moveTop();
    		shua_m3_rukou();
    		moveTop();
    		shua_m3_rukou();
    		
    		moveRight();
    		moveRight();
    		shua_m3_rukou();
    		moveRight();
    		moveRight();
    		shua_m3_rukou();
    		moveRight();
    		moveRight();
    		shua_m3_rukou();
    		
    		moveBottom();
    		moveBottom();
    		shua_m3_rukou();
    	}
    }
    
    private void shua_m3jy(){
		List<CoordBean> guaiwu = findPic(Common.m3_jingying);
		robot.delay(200);
		if(null != guaiwu && guaiwu.size() > 0){
			log.info("找到蓝龙坐标为：" + guaiwu.get(0).getX() + "," + guaiwu.get(0).getY());
			mouse.mouseMoveTo(guaiwu.get(0).getX(), guaiwu.get(0).getY() + 50);
			robot.delay(200);
			robot.press(press.F1);
			log.info("按下F1");
			robot.delay(2500);
		}
    }
    
    //刷永生
    private boolean shua_m3(){
    	shua_m3jy();
		List<CoordBean> c1 = findPic(Common.m3_yongsheng);
		if((null != c1 && c1.size() > 0)){
			log.info("找到材料");
			press.keyPress(press.F4);
			log.info("按下F4");
			robot.delay(2000);
			List<CoordBean> c2 = findPic(Common.m3_yuangushouling);
			robot.delay(2000);
			List<CoordBean> c3 = findPic(Common.m3_shuguanglaoying);
			robot.delay(2000);
			if( (null != c2 && c2.size() > 0) || 
					(null != c3 && c3.size() > 0) ){
				log.info("找到材料");
				press.keyPress(press.F4);
				log.info("按下F4");
			}
			return true;
		}
		return false;
    }
    
    private void shuacail_main(){
    	boolean flag = true;
    	while(true){
    		activeGame();
    		addLog("shuacail_main 1>" + flag);
        	robot.delay(500);
        	press.keyPress(press.NUM_8);
        	robot.delay(500);
        	shuacailiao(Common.juyanlingzhu_Img,Common.juyanlingzhu_zsImg,Common.juyanlingzhu_ysImg
        			,Common.baoxiang_r_1,Common.baoxiang_r_2
        			,Common.baoxiang_r_3,Common.baoxiang_r_4
        			,Common.baoxiang_r_5,flag);
        	robot.delay(500);
        	press.keyPress(press.NUM_9);
        	robot.delay(500);
        	addLog("shuacail_main 2>" + flag);
        	shuacailiao(Common.kuangfengbaojun,Common.kuangfen_left,Common.kuangfen_right
        			,Common.baoxiang_k_1,Common.baoxiang_k_2
        			,Common.baoxiang_k_3,Common.baoxiang_k_4
        			,Common.baoxiang_k_5,flag);
    	}
    }
    
    /**
     * 幻界大陆 刷材料
     * Common.juyanlingzhu_Img
     */
    private void shuacailiao(String lingzhuImg,String leftImg,String rightImg,
    		String cail1,String cail2,String cail3,String cail4,String cail5,boolean flag){
		robot.delay(500);
		closeAll();
		List<CoordBean> list = findPic(lingzhuImg,1000);
		robot.delay(200);
		if(null != list && list.size() > 0 && flag){
			flag = false;
			log.info("找到主坐标为：" + list.get(0).getX() + "," + list.get(0).getY());
			robot.press(press.F3);
			robot.delay(5000);
			list = findPic(Common.zhanlipinbaoxiang_Img,5000);
			if(null != list && list.size() > 0 ){
				jian_zhanlipinbaoxiang(leftImg,rightImg,cail1,cail2,cail3,cail4,cail5);
			}else{
				f1(list,lingzhuImg);
				jian_zhanlipinbaoxiang(leftImg,rightImg,cail1,cail2,cail3,cail4,cail5);
			}
		}else{
			f1(list,lingzhuImg);
			jian_zhanlipinbaoxiang(leftImg,rightImg,cail1,cail2,cail3,cail4,cail5);
			flag = true;
		}
    }
    
    private void f1(List<CoordBean> list,String guaiwuImg){
    	for(int i=0,j=4;i<j;i++){
			list = findPic(guaiwuImg,1000);
			if(null != list && list.size() > 0){
				robot.delay(200);
				mouse.mouseMoveTo(list.get(0).getX(), list.get(0).getY() + 50);
				robot.delay(200);
    			robot.press(press.F1);
    			log.info("按下F1");
    			robot.delay(3000);
			}
		}
    }
    
    /**
     * 关闭可能弹出的其他窗口
     */
    private void closeAll(){
    	List<CoordBean> list = new ArrayList<CoordBean>();
    	if(findImg(Common.quxiao_1Img, 500, list)){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}else if(findImg(Common.qdImg, 500, list)){
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    	}
    }
    
    private void jian_zhanlipinbaoxiang(String leftImg,String rightImg,
    		String cail1,String cail2,String cail3,String cail4,String cail5){
    	int[] b = findzhanlipinbaoxiang();
		robot.delay(200);
		if(b.length == 3 && b[1] != -1){
			log.info("找到宝箱坐标为：" + b[1] + "," + b[2]);
			press.keyPress(press.F4);
			log.info("按下F4");
			robot.delay(1000);
			List<CoordBean> list = new ArrayList<CoordBean>();
			if(findImg(Common.shiquImg, 1000, list)){
				if(list.size() == 1){
					robot.delay(200);
					mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    				robot.delay(200);
    				reset_zb(leftImg,rightImg);
    				for(int i=0,j=2;i<j;i++){
    					aotuFenJie(cail1,cail2,cail3,cail4,cail5);	
    				}
				}
			}
		}
    }
    
    
    /**
     * 鼠标点击之后，并移开
     * @param x
     * @param y
     * @param flag
     */
    private void mouseClick(int x,int y,boolean flag){
    	mouse.mouseClick(x, y, flag);
    	List<CoordBean> list = getRW_XY("top");
    	if(null != list && list.size() > 0){
    		x = list.get(0).getX();
        	y = list.get(0).getY();
        	mouse.mouseMoveTo(x, y);	
    	}else{
    		mouse.mouseMoveTo(x, y - 180);
    	}
    }
    /**
     * 自动分解 
     */
    private void aotuFenJie(String cail1,String cail2,String cail3,String cail4,String cail5){
    	//y-120;
    	//打开背包
    	press.keyPress(press.I);
    	robot.delay(500);
    	//查找宝箱
    	int x = 0;
    	int y = 0;
    	List<CoordBean> list = new ArrayList<>();
    	int i = 0;
    	do{
    		if(findImg(Common.baoxiang,1000,list)){
        		x = list.get(0).getX();
        		y = list.get(0).getY();
        		//右键点击
        		mouseClick(x, y, false);
        		i++;
        	}
    	}while(list.size() > 0 && i < 10);
    	if(i == 0 && addCaiLiao(cail1,cail2,cail3,cail4,cail5) == 0){
    		robot.delay(200);
    		press.keyPress(press.ESC);
    		robot.delay(200);
    		return;
    	}
    	//点击神性
    	robot.delay(500);
    	if(findImg(Common.shenxing, 1000, list)){
    		x = list.get(0).getX();
    		y = list.get(0).getY();
    		mouse.mouseClick(x, y, true);
    		//点击神仆
    		robot.delay(500);
    		mouse.mouseClick(x, (y - 120), true);
    	}
    	//点击分解圣物按钮
    	robot.delay(500);
    	if(findImg(Common.fenjieshengwu, 1000, list)){
    		x = list.get(0).getX();
    		y = list.get(0).getY();
    		mouse.mouseClick(x, y, true);
    	}
    	robot.delay(500);
    	if(findImg(Common.kaishifenjie_huise, 1000, list)){
    		x = list.get(0).getX();
    		y = list.get(0).getY();
    		//添加材料
    		addCaiLiao(cail1,cail2,cail3,cail4,cail5);
    		//执行分解
    		mouse.mouseClick(x, y, true);
    		robot.delay(5000);
    		press.keyPress(press.ESC);
    	}
    	robot.delay(500);
    	closeAll();
    }
    
    /**
     * 添加材料
     */
    private int addCaiLiao(String cail1,String cail2,String cail3,String cail4,String cail5){
    	addLog("添加材料开始");
    	List<CoordBean> list = new ArrayList<>();
    	int i = 0;
    	do{
        	i = addCaiLiao(cail1);
        	i += addCaiLiao(cail2);
        	i += addCaiLiao(cail3);
        	i += addCaiLiao(cail4);
        	i += addCaiLiao(cail5);
    	}while(list.size() > 0 && i < 6);
    	addLog("添加材料结束 i=" + i);
    	return i;
    }
    
    /**
     * 返回添加的材料个数
     * @param cail
     * @return
     */
    private int addCaiLiao(String cail){
    	robot.delay(500);
    	int startX = 877;
    	int startY = 568;
    	int x = 0;
    	int y = 0;
    	int ret = 0;
    	List<CoordBean> list = new ArrayList<>();
    	if(findImg(cail,500,startX,startY,list)){
    		addLog(cail +  list.size());
    		for(CoordBean item : list){
    			x = item.getX();
        		y = item.getY();
        		if(ret <= 5){
        			//右键点击
            		mouseClick(x, y, false);
            		ret = ret + 1;
        		}
    		}
    	}
    	return ret;
    }
    
    /**
     * @param leftImg Common.juyanlingzhu_zsImg
     * @param rightImg Common.juyanlingzhu_ysImg
     */
    private void reset_zb(String leftImg,String rightImg){
    	log.info("重新回到坐标 start..");
    	List<CoordBean> list = new ArrayList<CoordBean>();
    	robot.delay(2000);
    	if(findImg(rightImg, 3000, list)){
    		log.info("rightImg ..");
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
			robot.delay(200);
    		return;
    	}
    	if(findImg(leftImg, 3000, list)){
    		log.info("leftImg ..");
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
			robot.delay(200);
    		return;
    	}
    	log.info("重新回到坐标 end..");
    }
    
    //打怪
    private void daguai(){
    	addLog("打怪 開始...");
		do{
			List<CoordBean> list = find_jzt_guaiwu();
			if(null != list && list.size() > 0){//找怪
				addLog("找到怪物..");
				mouse.mouseClick(list.get(0).getX(), list.get(0).getY() + 25,true);
				robot.delay(1000);
				if(findImg(Common.jzt_close, 500, list)){//如果左键点击之后,弹出窗口,则关闭窗口.
					mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5,true);
				}
				robot.delay(200);
				list = find_jzt_guaiwu();
				if(null == list || list.size() == 0){
					flag_gw = false;
        			flag_dx = true;
					return;
				}
				robot.delay(200);
				mouse.mouseMoveTo(list.get(0).getX() + 5, list.get(0).getY() + 5);
				robot.press(press.F1);
				robot.delay(200);
				robot.press(press.F1);
				robot.delay(200);
				for(int i=0,j=20;i<j;i++){
					mouse.mouseClick(false);//点击10次鼠标右键技能
					robot.delay(700);
				}
				robot.delay(200);
				press.keyPress(press.F3);//然后按F3技能
				robot.delay(1000);
				for(int i=0,j=27;i<j;i++){//启动大风车技能,快速捡东西
					press.keyPress(press.F4);
					robot.delay(300);
				}
    		}else{
    			addLog("没有找到怪物..");
    			boolean f = moveCenter();
    			if(f){
    				robot.delay(500);
    				list = find_jzt_guaiwu();
    				if(null == list || list.size() == 0){
    					addLog("没有怪物了");
    					flag_gw = false;
    				}
    			}else{
    				addLog("移动到中间失败..");
    				flag_gw = false;
    			}
    		}
		}while(flag_gw);
		addLog("打怪 結束...");
    }
    
    //撿東西
    private void pickup(){
    	addLog("撿東西 開始...");
		do{
			if(find_cailiao()){//找東西
				press.keyPress(press.F4);
        		robot.delay(500);
    		}else{
    			flag_dx = false;
    			flag_gw = true;
    		}
		}while(flag_dx);
		addLog("撿東西 結束...");
    }
    
    private void move1(){
    	if(!flag_gw){//没有怪物了
    		List<CoordBean> list = new ArrayList<CoordBean>();
            if(findImg(Common.zhuziImg, 500, list)){
            	mouse.mouseClick(list.get(0).getX(),list.get(0).getY(),true);
            }
            robot.delay(1000);
            if(findImg(Common.zhuziImg, 500, list)){
            	mouse.mouseClick(list.get(0).getX(),list.get(0).getY() + 310,true);	
            }
            robot.delay(1000);
            list = findPic(Common.jzt_1_01);
            if(list.size() > 0){
            	mouse.mouseClick(list.get(0).getX(),list.get(0).getY(),true);
            }
            robot.delay(1000);
    		boolean f = true;
    		list = find_jzt_guaiwu();
			if(null == list || list.size() == 0){//没有找到怪物
        		do{
        			f = moveBottom();
        			f = moveLeft();
        			list = find_jzt_guaiwu();
        			if(null != list && list.size() > 0){//找怪
        				f = false;
        			}
        		}while(f);
        		addLog("金字塔1开始第二个图打怪");
    			flag_gw = true;
        		daguai();
    		}else{
    			addLog("金字塔1开始第二个图打怪");
    			flag_gw = true;
        		daguai();
    		}
    		
    		do{
    			f = moveTop();
    			list = find_jzt_guaiwu();
    			if(null != list && list.size() > 0){//找怪
    				f = false;
    			}
    		}while(f);
    		
    		list = find_jzt_guaiwu();
			if(null == list || list.size() == 0){//没有找到怪物
				do{
					f = moveTop();
					f = moveLeft();
					list = find_jzt_guaiwu();
	    			if(null != list && list.size() > 0){//找怪
	    				f = false;
	    			}
				}while(f);
				addLog("金字塔1开始第三个图打怪");
	    		flag_gw = true;
	    		daguai();
			}else{
	    		addLog("金字塔1开始第三个图打怪");
	    		flag_gw = true;
	    		daguai();
			}
    	}
    }
    
    private void move2(){
    	if(!flag_gw){//没有怪物了
    		List<CoordBean> list = new ArrayList<CoordBean>();
    		addLog("没有怪物，找中间柱子.");
            if(findImg(Common.zhuziImg, 500, list)){
            	addLog("1找到中间柱子.");
            	mouse.mouseClick(list.get(0).getX(),list.get(0).getY(),true);
            }
            addLog("找中间柱子.");
            robot.delay(1000);
            if(findImg(Common.zhuziImg, 500, list)){
            	addLog("2找到中间柱子.");
            	mouse.mouseClick(list.get(0).getX(),list.get(0).getY() + 310,true);	
            }
            robot.delay(1000);
    		boolean f = true;
    		do{
    			f = moveLeft();
    			list = find_jzt_guaiwu();
    			if(null != list && list.size() > 0){//找怪
    				f = false;
    			}
    		}while(f);
    		list = find_jzt_guaiwu();
			if(null == list || list.size() == 0){//没有找到怪物
        		do{
        			f = moveBottom();
        			f = moveLeft();
        			list = find_jzt_guaiwu();
        			if(null != list && list.size() > 0){//找怪
        				f = false;
        			}
        		}while(f);
        		addLog("金字塔2开始第二个图打怪");
    			flag_gw = true;
        		daguai();
    		}else{
    			addLog("金字塔2开始第二个图打怪");
    			flag_gw = true;
        		daguai();
    		}
    		
    		do{
    			f = moveRight();
    			list = find_jzt_guaiwu();
    			if(null != list && list.size() > 0){//找怪
    				f = false;
    			}
    		}while(f);
    		
    		list = find_jzt_guaiwu();
			if(null == list || list.size() == 0){//没有找到怪物
				do{
					f = moveTop();
					list = find_jzt_guaiwu();
	    			if(null != list && list.size() > 0){//找怪
	    				f = false;
	    			}
				}while(f);
				addLog("金字塔2开始第三个图打怪");
	    		flag_gw = true;
	    		daguai();
			}else{
	    		addLog("金字塔2开始第三个图打怪");
	    		flag_gw = true;
	    		daguai();
			}
    	}
    }
    
    private void move3(){
    	if(!flag_gw){//没有怪物了
    		boolean f = true;
    		do{
    			f = moveTop();
    			List<CoordBean> list = find_jzt_guaiwu();
    			if(null != list && list.size() > 0){//找怪
    				f = false;
    			}
    		}while(f);
    		
    		List<CoordBean> list = find_jzt_guaiwu();
			if(null == list || list.size() == 0){//没有找到怪物
        		do{
        			f = moveRight();
        			f = moveTop();
        			list = find_jzt_guaiwu();
        			if(null != list && list.size() > 0){//找怪
        				f = false;
        			}
        		}while(f);
        		addLog("金字塔3开始第二个图打怪");
    			flag_gw = true;
        		daguai();
    		}else{
    			addLog("金字塔3开始第二个图打怪");
    			flag_gw = true;
        		daguai();
    		}
    		
    		do{
    			f = moveBottom();
    			if(!f){
    				addLog("金字塔3第二个图往下移动不了，往右移动");
    				f = moveRight();
    				List<CoordBean> list2 = new ArrayList<CoordBean>();
    				if(findImg(Common.tu3jinkou,500,list2)){
    					mouse.mouseClick(list2.get(0).getX(), list2.get(0).getY(), true);
    					robot.delay(2000);
    				}
    			}
    			list = find_jzt_guaiwu();
    			if(null != list && list.size() > 0){//找怪
    				f = false;
    			}
    		}while(f);
    		
    		addLog("金字塔3开始第三个图打怪");
    		flag_gw = true;
    		daguai();
    	}
    }
    
    private void move4(){
    	if(!flag_gw){//没有怪物了
    		boolean f = true;
    		do{
    			f = moveRight();
    			List<CoordBean> list = find_jzt_guaiwu();
    			if(null != list && list.size() > 0){//找怪
    				f = false;
    			}
    		}while(f);
    		
    		List<CoordBean> list = find_jzt_guaiwu();
    		if(null == list || list.size() == 0){//没有找到怪物
    			//往右移动不了了，往下移
        		do{
        			f = moveBottom();
        			list = find_jzt_guaiwu();
        			if(null != list && list.size() > 0){//找怪
        				f = false;
        			}
        		}while(f);
        		addLog("金字塔4开始第二个图打怪");
				flag_gw = true;
	    		daguai();
    		}else{
    			addLog("金字塔4开始第二个图打怪");
				flag_gw = true;
	    		daguai();
    		}
    		
    		do{
    			f = moveBottom();
    			f = moveLeft();
    			list = find_jzt_guaiwu();
    			List<CoordBean> list2 = new ArrayList<CoordBean>();
    			if(findImg(Common.tu3jinkou_4,500,list2)){//图3入口
    				mouse.mouseClick(list2.get(0).getX(), list2.get(0).getY(), true);
    				robot.delay(1500);
    				f = false;
    			}
    			if(null != list && list.size() > 0){//找怪
    				f = false;
    			}
    		}while(f);
    		
    		list = find_jzt_guaiwu();
			if(null == list || list.size() == 0){//没有找到怪物
				do{
					f = moveBottom();
					f = moveLeft();
	    			list = find_jzt_guaiwu();
	    			if(null != list && list.size() > 0){//找怪
	    				f = false;
	    			}
	    		}while(f);
				addLog("金字塔4开始第三个图打怪");
	    		flag_gw = true;
	    		daguai();
			}else{
				addLog("金字塔4开始第三个图打怪");
	    		flag_gw = true;
	    		daguai();
			}
    	}
    }
    
    private List<CoordBean> find_jzt_guaiwu(){
    	List<CoordBean> list = new ArrayList<CoordBean>();
    	if(findImg(Common.jzt_close, 500, list)){//如果左键点击之后,弹出窗口,则关闭窗口.
    		mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5,true);
    		robot.delay(100);
    	}
    	List<CoordBean> list_qd = new ArrayList<CoordBean>(); 
    	if(findImg(Common.qdImg, 200, list_qd)){
    		if(findImg(Common.no_guaiwu,200,list)){
    			mouse.mouseClick(list_qd.get(0).getX(), list_qd.get(0).getY(), true);
        		robot.delay(200);
        		return null;
    		}else{
    			mouse.mouseClick(list_qd.get(0).getX(), list_qd.get(0).getY(), true);
        		robot.delay(200);
    		}
    	}
    	list = findPic(Common.jzt_pucong);
    	if(list.size() > 0){
    		return list;
    	}
    	robot.delay(100);
    	list = findPic(Common.jzt_guibian);
    	if(list.size() > 0){
    		return list;
    	}
    	robot.delay(100);
    	list = findPic(Common.jzt_lingzhu);
    	if(list.size() > 0){
    		return list;
    	}
    	robot.delay(100);
    	list = findPic(Common.guaiwu_01);
    	if(list.size() > 0){
    		return list;
    	}
    	return null;
    }
    
    private boolean find_cailiao(){
    	List<CoordBean> list1 = findPic(Common.jzt_shenshouyigu);
    	if(list1.size() > 0){
    		return true;
    	}
    	robot.delay(100);
    	List<CoordBean> list2 = findPic(Common.jzt_shenyouzhilei);
    	if(list2.size() > 0){
    		return true;
    	}
    	robot.delay(100);
    	List<CoordBean> list3 = findPic(Common.jzt_yinghunjingquan);
    	if(list3.size() > 0){
    		return true;
    	}
    	robot.delay(100);
    	List<CoordBean> list4 = findPic(Common.jzt_shouguhuashi);
    	if(list4.size() > 0){
    		return true;
    	}
    	robot.delay(100);
       	List<CoordBean> list5 = findPic(Common.jzt_chongshenglingye);
    	if(list5.size() > 0){
    		return true;
    	}
    	robot.delay(100);
       	List<CoordBean> list6 = findPic(Common.jzt_fenghuashouji);
    	if(list6.size() > 0){
    		return true;
    	}
    	robot.delay(100);
       	List<CoordBean> list7 = findPic(Common.jzt_shengshicaipian);
    	if(list7.size() > 0){
    		return true;
    	}
    	robot.delay(100);
       	List<CoordBean> list8 = findPic(Common.jzt_jinghuazhilu);
    	if(list8.size() > 0){
    		return true;
    	}
    	robot.delay(100);
       	List<CoordBean> list9 = findPic(Common.jzt_qianguzhisha);
    	if(list9.size() > 0){
    		return true;
    	}
    	robot.delay(100);
    	return false;
    }

    private int[] findjuyanlingzhu(){
    	int[] a = findPic.findPic(0, 0, robot.screenWidth, robot.screenHeight, getRealPath(Common.juyanlingzhu_Img), "", 0.9, 0);
    	return a;
    }
    
    private int[] findzhanlipinbaoxiang(){
    	int[] a = findPic.findPic(0, 0, robot.screenWidth, robot.screenHeight, getRealPath(Common.zhanlipinbaoxiang_Img), "", 0.9, 0);
    	return a;
    }
    
    private List<CoordBean> findPic(String img,int maxDelay){
    	List<CoordBean> list = new ArrayList<CoordBean>();
    	int time = 0;
		while(time<maxDelay){
			list = findPic(img);
			if(list.size()>0){
				return list;
			}else{
				robot.delay(200);
				time+=200;
			}
		}
		return list;
    }
    
    private List<CoordBean> findPic(String img){
    	List<CoordBean> list = new ArrayList<CoordBean>();
    	int[] a = findPic.findPic(0, 0, robot.screenWidth, robot.screenHeight, getRealPath(img), "", 0.9, 0);
    	if(a[0] != 0){
    		addLog("未能找到IMG【"+img+"】..");
    		return list;
    	}else{
    		CoordBean item = new CoordBean();
    		item.setX(a[1]);
    		item.setY(a[2]);
    		list.add(item);
    		addLog("找到IMG【"+img+"】坐標【"+item.getX()+","+item.getY()+"】..");
    	}
    	return list;
    }
    
    
    
    /**
     * 获取应用路径
     * TODO <功能简述> <br/>
     * TODO <功能详细描述>
     * @return
     */
    private String getAppPath(){
        return textField.getText();
    }
    
    private void readStart() throws Exception{
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
    private void start_game(String[] user) throws Exception{
    	threadPool.execute(openGameTh);
    	robot.delay(1000);
        robot.setSourcePath(MYDemo.class);
        boolean showGame = robot.imageDelaySearch(0, 0, robot.screenWidth, robot.screenHeight, robot.getResourceImage(Common.startImg), Robot.SIM_ACCURATE, 10000);
        if(!showGame){
        	addLog("未能打开游戏..");
        	return;
        }
        int hwnd = window.findWindow(0, null, gameName);
        if(hwnd > 0){
            window.moveWindow(hwnd, 0, 0);
            window.setWindowActivate(hwnd); //激活窗口
            robot.setSourcePath(MYDemo.class);
            List<CoordBean> list = robot.imageSearch(0, 0, robot.screenWidth, robot.screenHeight, Common.startImg, Robot.SIM_ACCURATE);
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
                	robot.setSourcePath(MYDemo.class);
                    boolean loginError1 = robot.imageDelaySearch(0, 0, robot.screenWidth, robot.screenHeight, robot.getResourceImage(Common.error1Img), Robot.SIM_ACCURATE, 1000);
                    addLog("loginError1.." + loginError1);
                    if(loginError1){
                    	addLog("账号密码错误..");
                    	robot.setSourcePath(MYDemo.class);
                        list = robot.imageSearch(0, 0, robot.screenWidth, robot.screenHeight, Common.error1Img, Robot.SIM_ACCURATE);
                        if(null == list || list.size() == 0){
                            addLog("未能找到账号密码错误之后弹框的关闭按钮..");
                        }
                        mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);//移动之后，左键点击
                    }else{
                    	flag = false;
                    }
                }
            }while(flag);
            
            robot.setSourcePath(MYDemo.class);
            boolean loginSuccess = robot.imageDelaySearch(0, 0, robot.screenWidth, robot.screenHeight, robot.getResourceImage(Common.email1Img), Robot.SIM_ACCURATE, 15000);
            if(!loginSuccess){
                addLog("未知错误，登录失败..");
                return;
            }
            
            list.clear();
            if(findImg(Common.closeImg, 3000, list)){
            	mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 10, true);
            }
            robot.delay(500);
            list.clear();
            if(findImg(Common.quxiao_1Img, 1500, list)){
            	mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 10, true);
            }
            
            //签到
            singIn(hwnd);
            //小号签到
            login_xiaohao_sing(hwnd);
            //切换到主号然后退出
            addLog("切换到主号..");
            list.clear();
            if(findImg(Common.zhuhaoImg, 1500, list)){
            	mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);
            }else{
            	addLog("未能找到图片【"+Common.zhuhaoImg+"】..");
            }
            addLog("延迟1秒..");
            //退出
            exit();
        }else{
            addLog("未能找到游戏【"+gameName+"】...");
        }
    }
    
    private void activeGame(){
    	int hwnd = window.findWindow(0, null, gameName);
        if(hwnd > 0){
            window.setWindowActivate(hwnd); //激活窗口
        }
    }
    
    private boolean findLoingError(int time){
    	robot.setSourcePath(MYDemo.class);
        boolean loginError = robot.imageDelaySearch(0, 0, robot.screenWidth, robot.screenHeight, 
        		robot.getResourceImage(Common.errorImg), Robot.SIM_ACCURATE, time);
        return loginError;
    }
    
    /**
     * 找账号密码输入框
     * @param time
     * @return
     */
    private boolean findInputPass(int time){
    	//1分钟内循环找输入账号和密码的框
    	boolean flag = findImg(Common.inputUserImg, time, new ArrayList<CoordBean>());
    	return flag;
    }
    
    private void login(String[] user) throws Exception{
    	addLog("开始登录..." + user[0]);
    	robot.delay(5000);//休息5秒，等待打开游戏登录界面
        
        boolean flag = findInputPass(80000);
        addLog("是否找到输入框..." + flag);
        List<CoordBean> list = new ArrayList<CoordBean>();
        if(!flag){
        	if(findImg(Common.quxiaoImg, 1000, list)){//如果出现取消按钮
        		addLog("點擊取消按鈕...");
        		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	}else{
        		addLog("发生错误，未能进入到登录界面..." + user[0]);
            	press.keyPress(press.ENTER);
        	}
        	login(user);
        }
        robot.setSourcePath(MYDemo.class);
        list = robot.imageSearch(0, 0, robot.screenWidth, robot.screenHeight, Common.inputUserImg, Robot.SIM_ACCURATE);
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
    
    private void clearInput() throws Exception{
    	for(int i=0;i<35;i++){
    		press.keyPress(press.BACKSPACE);
    		robot.delay(100);
    	}
    }
    
    /**
     * 输入账号密码
     * @throws Exception
     */
    private void input_user(String[] user) throws Exception{
        input(user[0], press);//账号
        robot.delay(200);
        addLog("按下TAB");
        press.keyPress(press.TAB);
        robot.delay(200);
        input(user[1], press);//密码
        robot.delay(200);
        enter(user);
    }
    
    private void enter(String[] user) throws Exception{
        addLog("按下ENTER");
        press.keyPress(press.ENTER);
        List<CoordBean> list = new ArrayList<CoordBean>();
        boolean flag = findImg(Common.quxiaoImg, 3000, list);
    	if(flag){//如果出现取消按钮
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    		login(user);
    	}else{
    		//按下enter之后，如果还停留在登录页面,继续按ENTER
		    flag = findImg(Common.inputUserImg, 3000, list);
	        if(flag){
	        	mouse.mouseClick(list.get(0).getX() - 50, list.get(0).getY() + 12, true);
	        	robot.delay(200);
	        	enter(user);
	        }
    	}
    }
    
    private boolean findImg(String img,int m,List<CoordBean> list){
    	robot.setSourcePath(MYDemo.class);
    	if(null != list && list.size() > 0){
    		list.clear();
    	}
    	return findImg(img,m,0,0,list);
    }
    
    private boolean findImg(String img,int m,int startX,int startY,List<CoordBean> list){
    	robot.setSourcePath(MYDemo.class);
    	if(null != list && list.size() > 0){
    		list.clear();
    	}
    	return robot.imageDelaySearch(startX, startY, robot.screenWidth, robot.screenHeight, robot.getResourceImage(img), Robot.SIM_ACCURATE, m,list);
    }
    
    /**
     * 签到
     * TODO <功能简述> <br/>
     * TODO <功能详细描述>
     * @param hwnd
     * @throws InterruptedException
     */
    private void singIn(int hwnd) throws InterruptedException{
    	addLog("开始签到..");
    	if(0 == hwnd){
    		hwnd = window.findWindow(0, null, gameName);
    	}
    	window.setWindowActivate(hwnd); //激活窗口
        window.moveWindow(hwnd, 0, 0);
        
        robot.delay(3000);
        sigin_detail();
        press.keyPress(press.ESC);
        robot.delay(500);
        List<CoordBean> list = new ArrayList<CoordBean>();
        if(findImg(Common.huanbaoliangongImg, 1000, null)){//判断是否跳出退出对话框
        	addLog("找到环保练功按钮..");
        	if(findImg(Common.quxiao_1Img, 1000, list)){
        		addLog("找到取消按钮..");	
        		mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);
        		robot.delay(500);
        	}
        }
        if(findImg(Common.yihouzaishuoImg, 2000, list)){
        	addLog("找到“以后再说”按钮..");
    		mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() + 5, true);
    		robot.delay(500);
        }
        
        if(findImg(Common.close_2Img, 1000, list)){
        	mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);//移动之后，左键点击
            robot.delay(500);
        }
        if(findImg(Common.qdImg, 1000, list)){
        	mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);//移动之后，左键点击
            robot.delay(500);
        }
        if(findImg(Common.closeImg, 1000, list)){
        	mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);//移动之后，左键点击
            robot.delay(500);
        }
        if(!findImg(Common.shaiziImg, 1000, list)){
            addLog("未能找到图片【"+Common.shaiziImg+"】..");
            if(findImg(Common.close_1Img, 1000, list)){
            	mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);//移动之后，左键点击
                robot.delay(500);
            }
            list = findPic(Common.emailImg);
        	if(list.size() > 0){
            	int y = list.get(0).getY() + 137;
            	int x = list.get(0).getX() - 126;
        		mouse.mouseClick(x, y,true);	
            }
        }else{
        	mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);//移动之后，左键点击        	
        }
        sigin_detail();
        press.keyPress(press.ESC);
        robot.delay(1000);
        addLog("结束签到..");
    }
    
    private void sigin_detail(){
    	List<CoordBean> list = new ArrayList<CoordBean>();
        robot.delay(500);
        if(!findImg(Common.dashaziImg, 1000, list)){
        	addLog("未能找到图片【"+Common.dashaziImg+"】..");
            return;
        }
        mouse.mouseClick(list.get(0).getX(), list.get(0).getY() - 105, true);//移动之后，左键点击
        robot.delay(500);
        if(findImg(Common.qdImg, 1000, list)){
        	mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);//移动之后，左键点击
            robot.delay(1000);
        }
    }
    
    /**
     * 登录小号签到
     * @throws InterruptedException 
     */
    private void login_xiaohao_sing(int hwnd) throws InterruptedException{
    	addLog("开始小号签到..");
    	robot.delay(500);
    	press.keyPress(press.X);
    	robot.delay(500);
    	robot.setSourcePath(MYDemo.class);
    	List<CoordBean> list = robot.imageSearch(0, 0, robot.screenWidth, robot.screenHeight, Common.quanbudengluImg, Robot.SIM_ACCURATE);
        if(null == list || list.size() == 0){
            addLog("未能找到图片【"+Common.quanbudengluImg+"】..");
            return;
        }
        mouse.mouseClick(list.get(0).getX() + 5, list.get(0).getY() - 5, true);//移动之后，左键点击
        list.clear();
        
        sing_xiaohao(list, hwnd, Common.xiaohao_1Img);
        list.clear();
        sing_xiaohao(list, hwnd, Common.xiaohao_2Img);
        list.clear();
        sing_xiaohao(list, hwnd, Common.xiaohao_3Img);
        list.clear();
        sing_xiaohao(list, hwnd, Common.xiaohao_4Img);
        list.clear();
        sing_xiaohao(list, hwnd, Common.xiaohao_5Img);
        robot.delay(1000);
        addLog("结束小号签到..");
    }

    private void sing_xiaohao(List<CoordBean> list,int hwnd,String xiaohao) throws InterruptedException{
        boolean flag = robot.imageDelaySearch(0, 0, robot.screenWidth, robot.screenHeight, robot.getResourceImage(xiaohao), Robot.SIM_ACCURATE, 80000,list);
        if(!flag){
        	addLog("未能找到图片【"+xiaohao+"】..");
        }
        mouse.mouseClick(list.get(0).getX() + 10, list.get(0).getY() + 10, true);//移动之后，左键点击
        singIn(hwnd);
    }
    
    
    private void forceExit() throws Exception{
    	addLog("forceExit start...");
    	Runtime.getRuntime().exec("TASKKILL /IM soul.exe");
    	addLog("forceExit end...");
    }
    
    /**
     * 退出游戏
     * TODO <功能简述> <br/>
     * TODO <功能详细描述>
     * @throws Exception
     */
    private void exit() throws Exception{
        addLog("开始退出...");
        forceExit();
        robot.delay(2000);
        List<CoordBean> list = new ArrayList<CoordBean>();
        list = findPic(Common.quanbuhuanbao, 1000);
        if(list.size() > 0 || findImg(Common.huanbaoliangongImg, 2000, list)){
        	list.clear();
        	if(findImg(Common.qdImg, 2000, list)){
        		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	}else{
        		exit();
        	}
        }else{
        	if(findImg(Common.quxiao_1Img, 1000, list)){
        		mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
        	}
        	robot.delay(1000);
        	if(findImg(Common.huanbaoliangongImg, 2000, list)){
        		if(findImg(Common.qdImg, 2000, list)){
        			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);	
        		}
        	}else{
        		exit();
        	}
        }
        robot.delay(1000);
        if(findImg(Common.qdImg, 2000, list)){
			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);	
		}
        if(findImg(Common.jixuyouxiImg, 1000, list)){
        	addLog("等待退出...");
        	robot.delay(8000);
        }
    }
    
    private void input() throws Exception{
    	Thread openNotepadTh = new Thread(new Runnable() {
            public void run() {
                //打开记事本,此函数会阻塞当前线程，直到打开的关闭为止。故而须另开辟一个线程执行此函数
                String cmdExe = "notepad";
                SystemUtil.cmd(cmdExe);
            }
        });
        
        threadPool.execute(openNotepadTh);
        
        robot.delay(1000);
        
        int hwnd = window.findWindow(0, null, "记事本");
        if(hwnd > 0){
            window.setWindowActivate(hwnd); //激活窗口
            String inputstr = "abcd418S040445ATGH";
            input(inputstr, press);
        }
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    
    /**
     * 判斷是否字母 大写
     * TODO <功能简述> <br/>
     * TODO <功能详细描述>
     * @param fstrData
     * @return
     */
    public static boolean isLetterUpper(String fstrData) {
        char c = fstrData.charAt(0);
        if (c >= 'A' && c <= 'Z') {
            return true;
        } else {
            return false;
        }
    } 
    
    /**
     * 判斷是否字母 小写
     * TODO <功能简述> <br/>
     * TODO <功能详细描述>
     * @param fstrData
     * @return
     */
    public static boolean isLetterLower(String fstrData) {
        char c = fstrData.charAt(0);
        if (c >= 'a' && c <= 'z') {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 判斷是否字母
     * TODO <功能简述> <br/>
     * TODO <功能详细描述>
     * @param fstrData
     * @return
     */
    public static boolean isLetter(String fstrData) {
        if ( isLetterLower(fstrData)  || isLetterUpper(fstrData)) {
            return true;
        } else {
            return false;
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
    private void input(String input,Press press) throws Exception{
    	robot.delay(200);
    	/*int fhwnd = window.getForegroundFocus();		
		addLog("获取焦点窗口句柄： " + fhwnd);
		robot.delay(1500);
		boolean b = window.sendString(fhwnd, input);
		addLog("输入结果：" + b);*/
        /*for(char s : input.toCharArray()){
            String items = s + "";
            if(isNumeric(items)){
                addLog((int)s);
                press.keyPress((int)s);
            }else if(isLetterLower(items)){
                addLog(Integer.valueOf(items.toUpperCase().toCharArray()[0]));
                press.keyPress(Integer.valueOf(items.toUpperCase().toCharArray()[0]));
            }else if(isLetterUpper(items)){
                addLog("shift :" + Integer.valueOf(s));
                press.groupPress(press.SHIFT, Integer.valueOf(s));
            }
            robot.delay(200);
        }*/
    	robot.sendString(input);
        robot.delay(200);
    }
    
    private String getRealPath(String img){
    	return path + img_folder_name + File.separator + img;
    }
    
    
    /**
     * 日志纪录
     * @param str
     */
    public void addLog(Object str){
    	log.info(str);
        System.out.println(str);
        if(null != this.textArea){
            this.textArea.setText(str+"\n"+this.textArea.getText());    
        }
    }
    
    /**
     * start..
     */
    public static void main(String[] args) {
        MYDemo frame = new MYDemo();
        frame.init();
        frame.init_layout();
        frame.initJob(frame);
        frame.setVisible(true);
    }

    private void initJob(MYDemo mydemo){
        try {
            //1.创建Scheduler的工厂
            SchedulerFactory sf = new StdSchedulerFactory();
            //2.从工厂中获取调度器实例
            Scheduler scheduler = sf.getScheduler();

            //3.创建JobDetail
            JobDetail job = JobBuilder.newJob(DemoJob.class)
                    .withDescription("this is a ram job") //job的描述
                    .withIdentity("ramJob", "ramGroup") //job 的name和group
                    .build();
            job.getJobDataMap().put("mydemo", mydemo);  

            //任务运行的时间，SimpleSchedle类型触发器有效
            long time=  System.currentTimeMillis() + 10*1000L; //60秒后启动任务
            Date statTime = new Date(time);

            //4.创建Trigger
                //使用SimpleScheduleBuilder或者CronScheduleBuilder
            Trigger t = TriggerBuilder.newTrigger()
                        .withDescription("")
                        .withIdentity("ramTrigger", "ramTriggerGroup")
                        .startAt(statTime)  //60秒后启动任务
                        .withSchedule(CronScheduleBuilder.cronSchedule(MYConfig.getInstance().getConfig("quartz_con").toString())) //60秒执行一次 --0/60 * * * * ? --0 0 0,09,12,22 * * ? --"0 0 0,09,12 * * ?"
                        .build();

            //5.注册任务和定时器
            scheduler.scheduleJob(job, t);

            //6.启动 调度器
            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
            addLog(e.getMessage());
        }
    }
    
    public boolean moveTop(){
    	List<CoordBean> list = getRW_XY("top");
       	if(list.size() == 0){
    		return false;
    	}
    	int x = list.get(0).getX();
    	int y = list.get(0).getY();
    	mouse.mouseClick(x + 25, y, true);
    	boolean f = isMove();
    	robot.delay(1000);
    	return f;
    }
    
    public boolean moveCenter(){
    	addLog("开始移动到中间..");
    	List<CoordBean> list = new ArrayList<>();
    	boolean f = findImg(Common.center, 500, list);
    	if(f){//找到中间图片
    		mouse.mouseClick(list.get(0).getX(), list.get(0).getY() + 75, true);
    	}
    	f = isMove();
    	if(!f){
    		f = findImg(Common.zhuziImg,500,list);
    		if(f){
    			mouse.mouseClick(list.get(0).getX(), list.get(0).getY(), true);
    			robot.delay(1000);
    			moveCenter();
    		}
    	}
    	robot.delay(1000);
    	addLog("结束移动到中间..");
    	return f;
    }
    
    public boolean moveBottom(){
    	List<CoordBean> list = getRW_XY("bottom");
    	if(list.size() == 0){
    		return false;
    	}
    	int x = list.get(0).getX();
    	int y = list.get(0).getY();
    	int ry = y + m_var;
    	if(ry <= 0 ){
    		return false;
    	}
    	mouse.mouseClick(x + 25, ry, true);
    	boolean f = isMove();
    	robot.delay(1000);
    	return f;
    }
    
    public boolean moveLeft(){
    	List<CoordBean> list = getRW_XY("left");
       	if(list.size() == 0){
    		return false;
    	}
    	int x = list.get(0).getX();
    	int y = list.get(0).getY() + 28;
    	int rx = x - m_var;
    	if(rx <= 0 ){
    		return false;
    	}
    	mouse.mouseClick(rx, y, true);
    	boolean f = isMove();
    	robot.delay(1000);
    	return f;
    }
    
    public boolean moveRight(){
    	List<CoordBean> list = getRW_XY("right");
       	if(list.size() == 0){
    		return false;
    	}
    	int x = list.get(0).getX();
    	int y = list.get(0).getY() + 28;
    	int rx = x + m_var;
    	if(rx <= 0 ){
    		return false;
    	}
    	mouse.mouseClick(rx, y, true);
    	boolean f = isMove();
    	robot.delay(1000);
    	return f;
    }
    
    /**
     * 判断是否移动成功
     * @param x
     * @param y
     * @return
     * 宽 70 高16 邮件图标往上33
     */
    private boolean isMove(){
    	List<CoordBean> list = findPic(Common.emailImg);
    	if(list.size() > 0){
    		int yEnd = list.get(0).getY() - 33;
    		int xEnd = list.get(0).getX() + 8;
    		int xStart = xEnd - 70;
    		int yStart = yEnd - 18;
    		int time = 2;
//    		file.screenImage(xStart,yStart,xEnd,yEnd, "c:\\logs\\abc.png");
        	boolean a = findPic.isDisplayDead(xStart,yStart,xEnd,yEnd,time);
        	addLog(xStart + "," + yStart + "," + xEnd + "," + yEnd + " r > " + a);
        	return !a;
    	}
    	return true;
    }
    
    /**
     * 获取人物坐标
     * @return
     */
    private List<CoordBean> getRW_XY(String type){
    	List<CoordBean> list = findPic(Common.renwuImg);
    	if(null == list || list.size() == 0){
    		CoordBean item = new CoordBean();
    		item.setX(630);
    		item.setY(306);
    		list.add(item);
    	}
    	for(CoordBean item : list){
    		if(type.equals("top")){
        		
        	}else if(type.equals("bottom")){
        		item.setY(item.getY() + rw_hight);
        	}else if(type.equals("left")){
        		item.setY(item.getY() + rw_hight);
        	}else if(type.equals("right")){
        		item.setY(item.getY() + rw_hight);
        	}
    	}
    	return list;
    }
}
