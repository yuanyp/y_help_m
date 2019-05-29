package com.y_ghelp.test.demo;

import com.xnx3.bean.ActiveBean;
import com.xnx3.microsoft.DmSoft;
import com.xnx3.microsoft.Press;
import com.xnx3.microsoft.Sleep;

public class NotepadDemo {
	
	public static void main(String[] args) {
		DmSoft dm = new DmSoft();
		ActiveBean activeBean = new ActiveBean();
		activeBean.setDm(dm.getDM());
		Press press = new Press(activeBean);
		Sleep sleep = new Sleep();
		int hwnd = dm.FindWindow("", "记事本");
		if(hwnd > 0) {
			System.out.println("找到了记事本窗口");
			int hwndEdit = dm.FindWindowEx(hwnd, "Edit", "");
			if(hwndEdit > 0) {
				System.out.println("找到记事本编辑窗口，开始绑定");
				int a = dm.BindWindow(hwndEdit, "normal", "windows", "windows", 0);
				if(a == 1) {
					System.out.println("绑定成功");
					sleep.sleep(200);
					dm.KeyPress(press.A);
					dm.KeyPress(press.B);
					dm.SendString(hwndEdit, "后台输入测试");	
				}
			}
		}else {
			System.out.println("没有打开记事本");
		}
	}

}
