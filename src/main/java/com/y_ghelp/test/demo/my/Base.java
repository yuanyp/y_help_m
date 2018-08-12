package com.y_ghelp.test.demo.my;

import com.xnx3.microsoft.Color;
import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.File;
import com.xnx3.microsoft.FindPic;
import com.xnx3.microsoft.Mouse;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.Window;
import com.xnx3.robot.Robot;

public class Base{
	Com com;
    Window window;
    Mouse mouse;
    Press press;
    Color color;
    Robot robot;
    FindPic findPic;
    com.xnx3.microsoft.File file;
    
	public Base(Com com, Window window, Mouse mouse, Press press, Color color, Robot robot, FindPic findPic,
			File file) {
		super();
		this.com = com;
		this.window = window;
		this.mouse = mouse;
		this.press = press;
		this.color = color;
		this.robot = robot;
		this.findPic = findPic;
		this.file = file;
	}
	
	public Com getCom() {
		return com;
	}
	public void setCom(Com com) {
		this.com = com;
	}
	public Window getWindow() {
		return window;
	}
	public void setWindow(Window window) {
		this.window = window;
	}
	public Mouse getMouse() {
		return mouse;
	}
	public void setMouse(Mouse mouse) {
		this.mouse = mouse;
	}
	public Press getPress() {
		return press;
	}
	public void setPress(Press press) {
		this.press = press;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Robot getRobot() {
		return robot;
	}
	public void setRobot(Robot robot) {
		this.robot = robot;
	}
	public FindPic getFindPic() {
		return findPic;
	}
	public void setFindPic(FindPic findPic) {
		this.findPic = findPic;
	}
	public com.xnx3.microsoft.File getFile() {
		return file;
	}
	public void setFile(com.xnx3.microsoft.File file) {
		this.file = file;
	}
    
    
}
