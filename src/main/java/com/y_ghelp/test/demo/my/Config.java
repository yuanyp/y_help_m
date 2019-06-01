package com.y_ghelp.test.demo.my;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;

import com.xnx3.microsoft.Com;
import com.y_ghelp.test.demo.config.MYConfig;

public class Config {
	
	private static String path;
	
	public static void setScreenWidth(int screenWidth) {
		Config.screenWidth = screenWidth;
	}

	public static void setScreenHeight(int screenHeight) {
		Config.screenHeight = screenHeight;
	}

	private static int screenWidth;
	private static int screenHeight;
	
	public final static String gameName = (String) MYConfig.getInstance().getConfig("gameName");
	
	public final static String defaultGamePath = (String) MYConfig.getInstance().getConfig("defaultGamePath");
	
	public final static String home = "c:\\temp_img";
	
	private static String img_folder_name = "game_img";
	
    public static String getImg_folder_name() {
		return img_folder_name;
	}

	public static void setImg_folder_name(String img_folder_name) {
		Config.img_folder_name = img_folder_name;
	}

	public static void setDic(Com com,int x, String fileName){
    	com.setDict(x, fileName);
    }
    
    public static void initConfig(int screenWidth,int screenHeight) {
    	Config.screenWidth = screenWidth;
    	Config.screenHeight = screenHeight;
    	path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        if(StringUtils.isNotBlank(path)){
            path = path.replace("/", "\\");
            path = path.substring(1, path.length());
        }
        PropertyConfigurator.configure(path + "log4j.properties");
    }

	public static String getPath() {
		return path;
	}

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}
	
}
