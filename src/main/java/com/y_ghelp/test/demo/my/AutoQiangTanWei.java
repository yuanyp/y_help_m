package com.y_ghelp.test.demo.my;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.xnx3.microsoft.Color;
import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.File;
import com.xnx3.microsoft.FindPic;
import com.xnx3.microsoft.FindStr;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.Sleep;
import com.xnx3.microsoft.Window;
import com.xnx3.robot.Robot;
import com.xnx3.robot.support.CoordBean;
import com.y_ghelp.test.demo.config.MYConfig;

import net.sf.json.JSONArray;

/**
 * 抢摊位
 * @author yuanyp
 *
 */
public class AutoQiangTanWei {

	public MYDemo myDemo;
	
	Robot robot;
    Com com;
    Window window;
    Mouse mouse;
    Press press;
    Color color;
    FindPic findPic;
    FindStr findStr;
    File file;
    private Util util;
    Sleep sleep = new Sleep();
    public String user;
    
    static Logger log = Logger.getLogger(AutoQiangTanWei.class);
	
	/**
	 * 在线的用户
	 */
	public Map<String,Integer> onLineUser = new HashMap<>();
	
	/**
	 * 当前正在操作的用户
	 */
	public String currentUser;
	
	public AutoQiangTanWei(MYDemo myDemo,Robot robot,Com com) {
		this.myDemo = myDemo;
		this.robot = robot;
		this.com = com;
		this.util = new Util(com);
		this.window = util.window;    //窗口操作类
		this.mouse = util.mouse;   //鼠标模拟操作类
		this.press = util.press;   //键盘模拟操作类
		this.color = util.color;   //颜色相关的取色、判断类
		this.findPic = util.findPic;
		this.findStr = util.findStr;
		this.file = util.file;
		
	}
	
	/**
	 * 设置需要操作的用户
	 * @param user
	 */
	public void setUser(String user) {
		this.user = user;
	}
	
	public void login() {
		try {
			log.info("开始登录账号..");
	        if(StringUtils.isBlank(user)){
	        	log.info("未能找到账号..");
	            return;
	        }
	        String[] users = user.split(";");
	        log.info("users .." + users.length);
            String[] userInfo = user.split(",");//下标：第0个账号，第1个密码，第2个是否含有小号
            if(userInfo.length == 3){
            	myDemo.start_game(userInfo, false);
            	if(loginSuccess()) {
            		int hwnd = window.findWindow(0, null, Config.gameName);
            		if(hwnd > 0) {
            			String title = window.getWindowTitle(hwnd);
            			window.setWindowTitle(hwnd, title + "_" + userInfo[0]);
            			onLineUser.put(userInfo[0], hwnd);
            		}
            	}
            }
		}catch (Exception e) {
			log.error("登录失败..", e);
		}
	}
	
	/**
	 * 设置当前工作的用户
	 * @param user
	 */
	public void setCurrentUser(String user) {
		log.info("设置活动的账号" + user);
		this.currentUser = user;
	}
	
	
	/**
	 * 绑定
	 */
	public boolean bind() {
		if(StringUtils.isBlank(currentUser)) {
			log.info("没有设置活动的账号");
			return false;
		}
		log.info("开始绑定.." + currentUser);
		int hwnd = onLineUser.get(currentUser);
		boolean bindRet = false;
		if(hwnd > 0) {
			log.info("hwnd.." + hwnd);
			window.setWindowActivate(hwnd);
			sleep.sleep(200);
			bindRet = com.bind(hwnd, "dx2", "windows3", "windows", 0);
			log.info("绑定结果.." + bindRet);
			sleep.sleep(200);
			return bindRet;
		}
		log.info("结束绑定.." + currentUser + "_绑定结果：" + bindRet);
		return false;
	}
	
	/**
	 * 主函数入口
	 */
	public void main() {
		//判断是否已经登录了账号
		int hwnd = window.findWindow(0, null, Config.gameName);
		if(hwnd > 0) {
			log.info("检测到已经有账号登录,设置在线用户..");
			setOnlineUsers();
		}
		if(onLineUser.size() == 0) {
			log.info("未检测到账号登录,开始登录用户..");
			login();
		}
		
		Set<String> users = onLineUser.keySet();
		for(String user : users) {
			setCurrentUser(user);
			if(bind()){
				if(goToLeiMing()){
					
				}
			}
			sleep.sleep(2000);
		}
	}
	
	/**
	 * 发送邮件提醒
	 */
	public void sendEmail(){
		
	}
	
	/**
	 * 抢摊位
	 * 422800-000000 当前地图, 00ff00-000000 摊位旗, ffff00-000000 结束
	 */
	public void qiangTanWei(){
		JSONArray jsonArray = (JSONArray)MYConfig.getInstance().getConfig("tanwei_xy");
		if(null != jsonArray && jsonArray.size() > 0){
			for(int i=0,j=jsonArray.size();i<j;i++){
				String xy = jsonArray.get(i).toString();
				if(StringUtils.isNotBlank(xy)){
					String[] xys = xy.split(",");
					List<CoordBean> emailImg = util.findPic(Common.emailImg, 1000);
					if(emailImg.size() > 0){
						int x = emailImg.get(0).getX();
						int y = emailImg.get(0).getY();
						mouse.mouseClick(x+2, y+52, true);
						sleep.sleep(500);
						List<CoordBean> dqdt = util.findStrE("当前地图", "422800-000000", 0.9, 0);//当前地图
						if(dqdt.size() > 0){
							int goX = Integer.parseInt(xys[0]);
							int goY = Integer.parseInt(xys[1]);
							mouse.mouseClick(goX,goY,true);
							sleep.sleep(200);
							xunlu();
							findTanWei();
						}
					}
				}
			}
		}
	}
	
	private void findTanWei(){
		//closeXiaoDiTu();
		List<CoordBean> tanwei = util.findStrE("摊位旗", "00ff00-000000", 0.9, 0);//摊位旗
		
	}
	private void xunlu(){
		
	}
	
	/**
	 * 去雷鸣
	 */
	public boolean goToLeiMing() {
		return true;
	}
	
	/**
	 * 检测是否已经有登录账号窗口,并设置在线用户；
	 */
	public void setOnlineUsers() {
		List<Integer> list = window.EnumWindow(0, Config.gameName, "", 1);
		for(Integer hwnd : list) {
			//获取账号
			String title = window.getWindowTitle(hwnd);
			if(StringUtils.isNotBlank(title) && title.contains("_")) {
				String user = title.split("_")[1];
				if(StringUtils.isNotBlank(user)) {
			    	if(loginSuccess()){
			    		onLineUser.put(user, hwnd);
			    	}
				}
			}
		}
	}
	
	/**
	 * 判断是否登录成功
	 * @return
	 */
	public boolean loginSuccess() {
		List<CoordBean> list = util.findPic(Common.emailImg);
    	if(list.size() > 0){
    		return true;
    	}
    	return false;
	}
}
