package com.xnx3.microsoft;

import java.io.File;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.xnx3.Lang;
import com.xnx3.Log;
import com.xnx3.UI;
import com.xnx3.bean.ActiveBean;
import com.xnx3.file.FileUtil;
import com.xnx3.net.HttpUtil;
import com.xnx3.robot.Robot;

/**
 * 辅助时必须先创建此类，获取此类的 {@link Com#getActiveXComponent()} 对象，所有的都对此对象操作(绑定后为后台操作，不绑定直接获取的话是前台操作)
 * <br/><b><u>此支持Windows xp、win7系统，Win7以上及Llinux、OS X等系统请使用 {@link Robot}</u></b>
 * <br/><b/>需</b>
 * <br/><i>xnx3-dll.jar</i>
 * <br/><i>jacob.jar</i>
 * <li>须使用Jre1.7作为运行环境，下载地址： <a href="http://www.xnx3.com/doc/jre1.7.html">http://www.xnx3.com/doc/jre1.7.html</a>
 * <br/>
 * C:\\dm.dll
 * 
 * @author 管雷鸣
 */
public class Com {
	/******文件长度，大小*******/
	private final static int msvcr100Length=773968;
	private final static int jacobX64Length=204800;
	private final static int jacobX86Length=167424;
	private final static int dmLength=830464;
	private final static int plug365Length=95232;
	private final static int JIntellitypeLength=30208;
	private final static int JIntellitype64Length=468704;
	
	private Log log;
	private boolean createSuccess=false;	//创建Com成功 调用getCreateSuccess()返回结果，证明创建成功
	private ActiveBean activeBean;	//各个插件的集合
	
	/**
	 * 是否已经初始化DLL过了，项目创建时实例化过一次后变为true，以后再有实例不会再初始化dll
	 */
	public static boolean initDll=false; 
	
	/**
	 * 绑定模式:dx模式,采用模拟dx后台鼠标模式,这种方式会锁定鼠标输入.有些窗口在此模式下绑定时，需要先激活窗口再绑定(或者绑定以后激活)，否则可能会出现绑定后鼠标无效的情况.此模式等同于BindWindowEx中的mouse为以下组合
	 */
	public static String DX="dx";	
	
	/**
	 * 绑定模式：正常模式,平常我们用的前台截屏模式
	 */
	public static String NORMAL="normal";
	
	/**
	 * 绑定模式：gdi模式,用于窗口采用GDI方式刷新时. 此模式占用CPU较大.
	 */
	public static String GDI="gdi";
	
	/**
	 * 绑定模式：: gdi2模式,此模式兼容性较强,但是速度比gdi模式要慢许多,如果gdi模式发现后台不刷新时,可以考虑用gdi2模式.
	 */
	public static String GDI2="gdi2";
	
	
	/**
	 * 绑定模式：Windows模式,采取模拟windows消息方式 同按键的后台插件.
	 */
	public static String WINDOWS="windows";
	
	/**
	 * 绑定模式：Windows3模式，采取模拟windows消息方式,可以支持有多个子窗口的窗口后台.
	 */
	public static String WINDOWS3="windows3";
	
	
	
	private int hwnd;
	private String display="";	//绑定，屏幕方式
	private String mouse="";	//绑定，鼠标模拟方式
	private String key="";		//绑定，案件方式
	/**
	 * public 字符串: 公共属性 dx模式共有  注意以下列表中,前面打五角星的表示需要管理员权限 

取值可以是以下任意组合. 组合采用"|"符号进行连接 这个值可以为空
1. ★ "dx.public.active.api" 此模式表示通过封锁系统API来锁定窗口激活状态.  注意，部分窗口在此模式下会耗费大量资源慎用. 
2. ★ "dx.public.active.message" 此模式表示通过封锁系统消息来锁定窗口激活状态.  注意，部分窗口在此模式下会耗费大量资源 慎用. 另外如果要让此模式生效，必须在绑定前，让绑定窗口处于激活状态,否则此模式将失效. 比如dm.SetWindowState hwnd,1 然后再绑定.
3.    "dx.public.disable.window.position" 此模式将锁定绑定窗口位置.不可与"dx.public.fake.window.min"共用.
4.    "dx.public.disable.window.size" 此模式将锁定绑定窗口,禁止改变大小. 不可与"dx.public.fake.window.min"共用.
5.    "dx.public.disable.window.minmax" 此模式将禁止窗口最大化和最小化,但是付出的代价是窗口同时也会被置顶. 不可与"dx.public.fake.window.min"共用.
6.    "dx.public.fake.window.min" 此模式将允许目标窗口在最小化状态时，仍然能够像非最小化一样操作.. 另注意，此模式会导致任务栏顺序重排，所以如果是多开模式下，会看起来比较混乱，建议单开使用，多开不建议使用. <收费功能，具体详情点击查看>
7.    "dx.public.hide.dll" 此模式将会隐藏目标进程的大漠插件，避免被检测..另外使用此模式前，请仔细做过测试，此模式可能会造成目标进程不稳定，出现崩溃。<收费功能，具体详情点击查看>
8. ★ "dx.public.active.api2" 此模式表示通过封锁系统API来锁定窗口激活状态. 部分窗口遮挡无法后台,需要这个属性. <收费功能，具体详情点击查看>
9. ★ "dx.public.input.ime" 此模式是配合SendStringIme使用. 具体可以查看SendStringIme接口. <收费功能，具体详情点击查看>
10 ★ "dx.public.graphic.protect" 此模式可以保护dx图色不被恶意检测.同时对dx.keypad.api和dx.mouse.api也有保护效果. <收费功能，具体详情点击查看>
11 ★ "dx.public.disable.window.show" 禁止目标窗口显示,这个一般用来配合dx.public.fake.window.min来使用. <收费功能，具体详情点击查看>
12 ★ "dx.public.anti.api" 此模式可以突破部分窗口对后台的保护. <收费功能，具体详情点击查看>
13 ★ "dx.public.memory" 此模式可以让内存读写函数突破保护.只要绑定成功即可操作内存函数. <收费功能，具体详情点击查看>
14 ★ "dx.public.km.protect" 此模式可以保护dx键鼠不被恶意检测.最好配合dx.public.anti.api一起使用. 此属性可能会导致部分后台功能失效. <收费功能，具体详情点击查看>
15    "dx.public.prevent.block"  绑定模式1 3 5 7 101 103下，可能会导致部分窗口卡死. 这个属性可以避免卡死. <收费功能，具体详情点击查看>
16    "dx.public.ori.proc"  此属性只能用在模式0 1 2 3和101下. 有些窗口在不同的界面下(比如登录界面和登录进以后的界面)，键鼠的控制效果不相同. 那可以用这个属性来尝试让保持一致. 注意的是，这个属性不可以滥用，确保测试无问题才可以使用. 否则可能会导致后台失效. <收费功能，具体详情点击查看>
	 */
	private String pub="";		//绑定，案件方式
	private int mode=0;		//绑定模式
	
	
	
	/**
	 * @see Com
	 */
	public Com() {
		versionCheck();
		initCheckJreVersion();
		initCopyDll();
		
		ComThread.InitSTA();
		
		display=DX;
		mouse=DX;
		key=DX;
		mode=0;
		
		log=new Log();
		
		//注册，创建dm.dmsoft对象
		initRegisterDll();
	}
	
	/**
	 * 版本检测
	 */
	private void versionCheck(){
		new Thread(new Runnable() {
			public void run() {
				HttpUtil http=new HttpUtil();
				String content=http.get("http://www.xnx3.com/down/java/j2se/version.html").getContent();
				if(content==null||content.length()==0){
					System.out.println("连接服务器进行版本检测失败");
				}else{
					float version=Lang.stringToFloat(content, 1.0f);
					if(version-Lang.version>0.0005){
						System.out.println("发现新版本："+version+"，请及时更新最新版本");
					}
				}
			}
		}).start();
	}
	
	/**
	 * 返回创建Com()的结果，如果自检过程中发现异常，创建Com失败，则调用此会返回false
	 * @return 如果为true，则创建成功！
	 */
	public boolean isCreateSuccess() {
		return createSuccess;
	}

	/**
	 * 需要是jre1.7版本，检测版本
	 */
	private void initCheckJreVersion(){
		String version=System.getProperty("java.version");
		if(version.indexOf("1.7")>-1){
			//通过
			this.createSuccess=true;
		}else{
			this.createSuccess=false;
			if(UI.showConfirmDialog("运行请使用 JRE 1.7 版本！<br/>当前版本："+version+"<br/>当前JRE路径："+Lang.getCurrentJrePath()+"<br/>JRE1.7下载地址：http://www.xnx3.com/doc/jre1.7.html<br/>只有开发者可见此提示<br/><br/>是否下载？")==Lang.CONFIRM_YES){
				SystemUtil.openUrl("http://www.xnx3.com/doc/jre1.7.html");
			}
			System.err.println("运行请使用 JRE 1.7 版本！下载地址： http://www.xnx3.com/doc/jre1.7.html");
		}
	}
	
	public Com(boolean a) {
		initCopyDll();
	}
	
	public static void main(String[] args) {
		Com com = new Com(true);
		com.initCopyDll();
	}
	
	/**
	 * Dll文件初始化，将对应的dll复制到相应的文件夹内
	 * <li>1.检测C:\WINDOWS\system32\msvcr100.dll 是否存在
	 * <li>2.检测jacob-1.15-M3-x64.dll、jacob-1.15-M3-x86.dll是否放到了Jre/bin/下
	 * <li>3.检测JIntellitype.dll、JIntellitype64.dll是否放到了Jre/bin/下
	 */
	private void initCopyDll(){
		if(Com.initDll){
			return;
		}
		
		if(this.getClass().getResourceAsStream("dll/msvcr100.dll")==null){
			UI.showMessageDialog("请导入 xnx3_dll.jar");
			return;
		}
		
		try {
			//1.
			String msvcr100="C:\\WINDOWS\\system32\\msvcr100.dll";
			if(!FileUtil.exists(msvcr100)){
				this.createSuccess=false;
				FileUtil.inputStreamToFile(this.getClass().getResourceAsStream("dll/msvcr100.dll"), msvcr100);
				if(FileUtil.exists(msvcr100)){
					UI.showMessageDialog("系统缺少msvcr100.dll文件，已自动修复！");
				}else{
					UI.showMessageDialog("自动修复msvcr100.dll文件失败！文件无法写入，请检查C盘是否有写入文件权限，或手动将dll文件加入");
				}
				this.createSuccess=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			//2.
			String jacobX64=Lang.getCurrentJrePath()+"\\bin\\jacob-1.18-M2-x64.dll";
			String jacobX86=Lang.getCurrentJrePath()+"\\bin\\jacob-1.18-M2-x86.dll";
			if(!FileUtil.exists(jacobX64)||!FileUtil.exists(jacobX86)){
				this.createSuccess=false;
				FileUtil.inputStreamToFile(this.getClass().getResourceAsStream("dll/jacob-1.18-M2-x64.dll"), jacobX64);
				FileUtil.inputStreamToFile(this.getClass().getResourceAsStream("dll/jacob-1.18-M2-x86.dll"), jacobX86);
				UI.showMessageDialog("系统缺少jacob-1.18-M2-x64.dll、jacob-1.18-M2-x86.dll文件，已自动修复！");
				this.createSuccess=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			//3.
			String JIntellitype=Lang.getCurrentJrePath()+"\\bin\\JIntellitype.dll";
			String JIntellitype64=Lang.getCurrentJrePath()+"\\bin\\JIntellitype64.dll";
			if(!FileUtil.exists(JIntellitype)||!FileUtil.exists(JIntellitype64)){
				this.createSuccess=false;
				FileUtil.inputStreamToFile(this.getClass().getResourceAsStream("dll/JIntellitype.dll"), JIntellitype);
				FileUtil.inputStreamToFile(this.getClass().getResourceAsStream("dll/JIntellitype64.dll"), JIntellitype64);
				UI.showMessageDialog("系统缺少JIntellitype.dll、JIntellitype64.dll文件，已自动修复！");
				this.createSuccess=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建dm.dmsoft对象
	 * <li>检测dm.dll是否已注册,若是没有注册，则自动注册
	 * <li>若是已注册，则创建dm.dmsoft对象
	 */
	private void initRegisterDll(){
		if(Com.initDll){
			return;
		}
		
		Com.initDll=true;
		activeBean=new ActiveBean();
		String currentDir=Lang.getCurrentJrePath();	//当前项目Jre路径
		
		try {
			ActiveXComponent activeDm=new ActiveXComponent("dm.dmsoft");
			activeBean.setDm(activeDm);		//创建大漠对象
			activeBean.setPlugin365(new ActiveXComponent("Plugin365ID"));	//创建365对象
			this.createSuccess=true;
		} catch (Exception e) {
			e.printStackTrace();
			//检测dm.dll是否放到了C:下
			String dm="C:\\dm.dll";
			if(!FileUtil.exists(dm)){
				this.createSuccess=false;
				FileUtil.inputStreamToFile(this.getClass().getResourceAsStream("dll/dm.dll"), dm);
				SystemUtil.registerDll(dm);
				log.debug(this, "initRegisterDll()", "注册dm.dll完毕");
			}
			
			//检测Plug365New.dll是否放到了C:下
			String plug365="C:\\Plug365New.dll";
			if(!FileUtil.exists(plug365)){
				this.createSuccess=false;
				FileUtil.inputStreamToFile(this.getClass().getResourceAsStream("dll/Plug365New.dll"), plug365);
				SystemUtil.registerDll(plug365);
				log.debug(this, "initRegisterDll()", "注册Plug365New.dll完毕");
			}
			
			try {
				activeBean.setDm(new ActiveXComponent("dm.dmsoft"));		//创建大漠对象
				activeBean.setPlugin365(new ActiveXComponent("Plugin365ID"));	//创建365对象
				log.debug(this, "initDll", "检测到dll未注册，已自动注册完毕");
				this.createSuccess=true;
			} catch (Exception e2) {
				this.createSuccess=false;
				
				e2.printStackTrace();
				if(e2.getMessage().equals("Can't co-create object")){
					System.out.println("检测到dll插件未注册，进行自动注册时出错！报此异常，极大可能是运行的Jar导致的，请下载我们测试好的Jar1.7安装包进行开发调试");
					System.out.println("http://www.xnx3.com/doc/jre1.7.html");
					System.out.println("(如果弹出提示注册...dll文件失败，则你的操作系统注册不了，有极少数的xp、win7的用户电脑注册不了的，可以换台电脑测试)");
				}
				
			}
		}
		
	}
	

	

	/**
	 * 获取绑定成功后的ActiveXComponent对象,以后的操作都是对该对象的操作
	 * @return ActiveXComponent 要操作的目标对象
	 */
	public ActiveBean getActiveXComponent() {
		return activeBean;
	}
	
	/**
	 * 设置点阵字库
	 * @param num 字库的序号,取值为0-9,目前最多支持10个字库，序号设置不可以重复
	 * @param fileName 字库文件名，如 mapPositionDict.txt ,此文件须位于设置的 {@link Com#setResourcePath(String)} 资源路径里，也就是资源路径必须于此函数之前先设置好
	 * @return boolean true:成功
	 */
	public boolean setDict(int num,String fileName){
		boolean xnx3_result=false;
		
		Variant[] var=new Variant[2];
		try {
			var[0]=new Variant(num);
			var[1]=new Variant(fileName);
			if(activeBean.getDm().invoke("SetDict",var).getInt()==1){
				xnx3_result=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(this, "setDict", "异常:"+e.getMessage());
		}finally{
			var=null;
		}
		return xnx3_result;
	}
	

	/**
	 * 窗口绑定， 绑定后可任意操作该窗口，每个程序都不一样，如果鼠标、键盘、截图、取色等不好使，需要多试试几种组合，以便找到其最佳组合
	 * @param hwnd 要绑定的窗口句柄  获取方式位于： {@link Window#findWindow(int, String, String)} {@link Window#getMousePointWindowHwnd()}
	 * @param display 屏幕颜色获取方式 取值有以下几种
	 * 		<ul>
	 * 			<li>{@link Com#NORMAL} 正常模式,平常我们用的前台截屏模式 
	 * 			<li>{@link Com#GDI} gdi模式,用于窗口采用GDI方式刷新时. 此模式占用CPU较大.
	 * 			<li>{@link Com#GDI2} gdi2模式,此模式兼容性较强,但是速度比gdi模式要慢许多,如果gdi模式发现后台不刷新时,可以考虑用gdi2模式
	 * 			<li>{@link Com#DX} dx图像模式 dx.graphic.2d|dx.graphic.3d .<b>注意此模式需要管理员权限</b>
	 * 		</ul>
	 * @param mouse 鼠标仿真模式 取值有以下几种 
	 * 		<ul>
	 * 			<li>{@link Com#NORMAL} 正常模式,平常我们用的前台鼠标模式
	 * 			<li>{@link Com#WINDOWS} Windows模式,采取模拟windows消息方式 同按键自带后台插件
	 * 			<li>{@link Com#WINDOWS3} Windows3模式，采取模拟windows消息方式,可以支持有多个子窗口的窗口后台
	 * 			<li>{@link Com#DX} dx模式,采用模拟dx后台鼠标模式,这种方式会锁定鼠标输入.有些窗口在此模式下绑定时，需要先激活窗口再绑定(或者绑定以后激活)，否则可能会出现绑定后鼠标无效的情况.<b>注意此模式需要管理员权限</b>
	 * 		</ul>
	 * @param key 键盘仿真模式 取值有以下几种
	 * 		<ul> 
	 * 			<li>{@link Com#NORMAL} 正常模式,平常我们用的前台键盘模式 
	 * 			<li>{@link Com#WINDOWS} Windows模式,采取模拟windows消息方式 同按键精灵的后台插件
	 * 			<li>{@link Com#DX} dx模式,采用模拟dx后台键盘模式。有些窗口在此模式下绑定时，需要先激活窗口再绑定(或者绑定以后激活)，否则可能会出现绑定后键盘无效的情况.<b>注意此模式需要管理员权限</b>
	 * 		</ul>
	 * @param mode 窗口绑定的模式 
	 * 				<ul>
	 * 					<li>0 : 推荐模式此模式比较通用，而且后台效果是最好的.
	 *					<li>2 : 同模式0,此模式为老的模式0,尽量不要用此模式，除非有兼容性问题
	 *					<li>4 : 同模式0,如果模式0有崩溃问题，可以尝试此模式.
	 *				</ul>
	 * @return boolean 窗口绑定结果 
	 * 				<ul>
	 * 					<li>true:绑定成功
	 * 					<li>false:绑定失败
	 * 				</ul>
	 */
	public boolean bind(int hwnd,String display,String mouse,String key,int mode){
		this.display=display;
		this.mouse=mouse;
		this.key=key;
		this.mode=mode;
		
		return bind(hwnd);
	}
	
	public boolean bindEx(int hwnd,String display,String mouse,String key,String pub,int mode){
		this.display=display;
		this.mouse=mouse;
		this.key=key;
		this.mode=mode;
		this.pub = pub;
		return bindEx(hwnd);
	}

	public boolean bindEx(int hwnd){
		this.hwnd=hwnd;
		
		boolean xnx3_result=false;	
		
		for (int i = 0; i < 30; i++) {
			try {
				//激活窗口
				new Window(activeBean).setWindowState(hwnd, 1);
				
				//进行dm窗口绑定
				Variant[] var=new Variant[5];
				var[0]=new Variant(hwnd);
				var[1]=new Variant(display);
				var[2]=new Variant(mouse);
				var[3]=new Variant(key);
				var[4]=new Variant(pub);
				var[5]=new Variant(mode);
				int bindWindow=activeBean.getDm().invoke("BindWindowEx",var).getInt();
				var=null;
				
				if(bindWindow==0){
					activeBean.getDm().invoke("ForceUnBindWindow",hwnd);
					xnx3_result=false;
				}else{	//绑定成功
					
					xnx3_result=true;
					
					i=30;
				}
			} catch (Exception e) {
				xnx3_result=false;
				e.printStackTrace();
				log.debug(this, "bind", "异常:"+e.getMessage());
			}
		}
		
		return xnx3_result;
	} 
	
	/**
	 * 窗口绑定，绑定后可任意操作该窗口
	 * 		<li>同 {@link Com#bind(int, String, String, String, int)} 后面默认传入的都是DX模式
	 * @param hwnd 要绑定的窗口句柄  获取方式位于： {@link Window#findWindow(int, String, String)} {@link Window#getMousePointWindowHwnd()}
	 * @return boolean <li>true:绑定成功
	 * 					<li>false:绑定失败
	 */
	public boolean bind(int hwnd){
		this.hwnd=hwnd;
		
		boolean xnx3_result=false;	
		
		for (int i = 0; i < 30; i++) {
			try {
				//激活窗口
				new Window(activeBean).setWindowState(hwnd, 1);
				
				//进行dm窗口绑定
				Variant[] var=new Variant[5];
				var[0]=new Variant(hwnd);
				var[1]=new Variant(display);
				var[2]=new Variant(mouse);
				var[3]=new Variant(key);
				var[4]=new Variant(mode);
				int bindWindow=activeBean.getDm().invoke("BindWindow",var).getInt();
				var=null;
				
				if(bindWindow==0){
					activeBean.getDm().invoke("ForceUnBindWindow",hwnd);
					xnx3_result=false;
				}else{	//绑定成功
					
					xnx3_result=true;
					
					i=30;
				}
			} catch (Exception e) {
				xnx3_result=false;
				e.printStackTrace();
				log.debug(this, "bind", "异常:"+e.getMessage());
			}
		}
		
		return xnx3_result;
	} 
	
	/**
	 * 设置资源文件的所在路径，例如找图、点阵字库等文件所放的图片路径，统一放到此文件夹内
	 * @param resourcePath 资源路径文件夹，如： F:/waigua/resource
	 * @return boolean
	 */
	public boolean setResourcePath(String resourcePath){
		//设置当前资源路径
		int setPath=activeBean.getDm().invoke("SetPath", resourcePath).getInt();
		return setPath==1;
	}
	
	/**
	 * CPU优化，降低界面刷新的CPU占用
	 * @param num 取值范围0到100   取值为0 表示关闭CPU优化. 这个值越大表示降低CPU效果越好.
	 * @return boolean
	 */
	public boolean lowerCpu(int num){
		boolean xnx3_result=false;
		try {
			if(activeBean.getDm().invoke("DownCpu",num).getInt()==1){
				xnx3_result=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(this, "lowerCpu", "异常:"+e.getMessage());
		}
		return xnx3_result;
	}
	
	
	/**
	 * 解除绑定，释放内存
	 * <li>1.解除之前绑定的窗口，若是没有绑定操作的窗口，则自动跳过
	 * <li>2.调用 {@link Com#destroy()} 进行资源、内存释放。  
	 * @return <li>true：解除绑定成功，同时自动释放ActiveXComponent对象，释放资源
	 * 			<li>false:解除绑定失败，若是失败，不做任何处理。游戏运行时间太长一两天的有时候会遇到此种情况
	 */
	public boolean unbind(){
		boolean xnx3_result=false;
		
		//有绑定窗口了，解除绑定窗口
		if(hwnd>0){
			try {
				//解除绑定前将窗口设置为当前活动窗口
				activeBean.getDm().invoke("SetWindowState", hwnd,1);
				
				int getResult=activeBean.getDm().invoke("UnBindWindow").getInt();;
				if(getResult==0){	//解除绑定失败，使用强制接触绑定
					if(activeBean.getDm().invoke("ForceUnBindWindow",hwnd).getInt()==1){
						xnx3_result=true;
					}
				}else{
					xnx3_result=true;
				}
				
//				解除对窗口的绑定，进行资源释放
			} catch (Exception e) {
				e.printStackTrace();
				log.debug(this, "unbind", "异常:"+e.getMessage());
			}
			
		}
		
		if(xnx3_result){
			destroy();
		}
		
		return xnx3_result;
	}
	
	public String getDMVersion() {
		String ver = Dispatch.call(this.activeBean.getDm(), "Ver").getString();
		return ver;
	}
	
	
	/**
	 * 销毁，释放内存
	 * <li>若是之前调用过 {@link Com#bind(int)} 绑定了操作的目标窗口了，则需要使用 {@link Com#unbind()} 进行释放
	 */
	public void destroy(){
//		ComThread.RemoveObject(activeBean.getDm());
		ComThread.Release();
		activeBean.setDm(null);
		activeBean.setPlugin365(null);
	}
	
}