package com.y_ghelp.test.demo.my;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

/**
 * 
 * win8 || win10 需要手动注册；用管理员打开cmd 
 * regsvr32 C:\\dm.dll
 * regsvr32 C:\\Plug365New.dll
 *
 */
public class MYDemoLogin extends JFrame{
	
	static Logger log = Logger.getLogger(MYDemoLogin.class);
	
	List<String> listUsers = new ArrayList<String>();//判断账号是否已经处理过
	
	private JTextField textField;
	Thread playGameTh;
	
	public MYDemoLogin() {
		this.setBounds(100, 100, 450, 300);
		JButton btnNewButton = new JButton("一鍵登錄");
		btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		MYUtil.threadPool.execute(playGameTh);
				} catch (Exception e2) {
					e2.printStackTrace();
					MYUtil.addLog(e2.getMessage());
				}
            }
        });
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setText(MYUtil.defaultGamePath);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGap(33)
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(btnNewButton)
					.addGap(42))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(30)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton))
					.addContainerGap(209, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
		
	}
	
	public void initPlayGameTh(){
        playGameTh = new Thread(new Runnable() {
            public void run() {
                try {
                	MYUtil.readStart(listUsers);
                } catch (Exception e) {
                	MYUtil.addLog(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
	}
	
    /**
     * start..
     */
    public static void main(String[] args) {
        MYDemoLogin frame = new MYDemoLogin();
        frame.initPlayGameTh();
        frame.setVisible(true);
    }
}
