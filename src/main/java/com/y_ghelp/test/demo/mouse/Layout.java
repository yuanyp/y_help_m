package com.y_ghelp.test.demo.mouse;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JRadioButton;

import org.apache.commons.lang3.StringUtils;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Layout extends JFrame{
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_delay1;
    private JTextField textField_delay2;
    private JTextField textField_delay3;
    private JRadioButton rdo1_1;
    private JRadioButton rdo1_2;
    private JRadioButton rdo2_1;
    private JRadioButton rdo2_2;
    private JRadioButton rdo3_1;
    private JRadioButton rdo3_2;
    
    public void execute(){
        String strXY1 = textField_1.getText();
        String strXY2 = textField_2.getText();
        String strXY3 = textField_3.getText();
        //1
        if(StringUtils.isBlank(strXY1)){
            return;
        }
        String delay1 = textField_delay1.getText();
        if(StringUtils.isBlank(delay1)){
            delay1 = "500";
        }
        detail(getXY(strXY1),Integer.parseInt(delay1),rdo1_1.isSelected());
        
        //2
        if(StringUtils.isBlank(strXY2)){
            return;
        }
        String delay2 = textField_delay2.getText();
        if(StringUtils.isBlank(delay2)){
            delay2 = "500";
        }
        detail(getXY(strXY2),Integer.parseInt(delay2),rdo2_1.isSelected());
        
        //3
        if(StringUtils.isBlank(strXY3)){
            return;
        }
        String delay3 = textField_delay3.getText();
        if(StringUtils.isBlank(delay3)){
            delay3 = "500";
        }
        detail(getXY(strXY3),Integer.parseInt(delay3),rdo3_1.isSelected());
    }
    
    private void detail(List<Integer> xy,int delay,boolean mouse){
        MouseTool.robot.delay(delay);
        MouseTool.mouse.mouseClick(xy.get(0), xy.get(1), mouse);
    }
    
    private List<Integer> getXY(String strXY){
        if(StringUtils.isBlank(strXY)){
            return null;
        }
        List<Integer> ret = new ArrayList<Integer>();
        List<String> list = Arrays.asList(strXY.split(","));
        for(String item:list){
            ret.add(Integer.parseInt(item));
        }
        return ret;
    }
    
    public Layout() {
        setFont(new Font("Dialog", Font.PLAIN, 12));
        setForeground(Color.WHITE);
        setTitle("鼠标自动点击工具");
        
        JLabel lblNewLabel = new JLabel("1. 坐标");
        
        textField_1 = new JTextField();
        textField_1.setColumns(10);
        
        ButtonGroup group = new ButtonGroup();
        
        rdo1_1 = new JRadioButton("左键");
        rdo1_1.setSelected(true);
        
        rdo1_2 = new JRadioButton("右键");
        group.add(rdo1_1);
        group.add(rdo1_2);
        
        JLabel label = new JLabel("");
        
        JLabel label_1 = new JLabel("2. 坐标");
        
        textField_2 = new JTextField();
        textField_2.setColumns(10);
        
        ButtonGroup group2 = new ButtonGroup();
        rdo2_1 = new JRadioButton("左键");
        rdo2_1.setSelected(true);
        
        rdo2_2 = new JRadioButton("右键");
        group2.add(rdo2_1);
        group2.add(rdo2_2);
        
        JLabel label_2 = new JLabel("3. 坐标");
        
        textField_3 = new JTextField();
        textField_3.setColumns(10);
        
        ButtonGroup group3 = new ButtonGroup();
        rdo3_1 = new JRadioButton("左键");
        rdo3_1.setSelected(true);
        
        rdo3_2 = new JRadioButton("右键");
        group3.add(rdo3_1);
        group3.add(rdo3_2);
        
        textField_delay1 = new JTextField();
        textField_delay1.setText("500");
        textField_delay1.setColumns(10);
        
        JLabel label_3 = new JLabel("延迟");
        
        JLabel label_4 = new JLabel("延迟");
        
        textField_delay2 = new JTextField();
        textField_delay2.setText("500");
        textField_delay2.setColumns(10);
        
        JLabel label_5 = new JLabel("延迟");
        
        textField_delay3 = new JTextField();
        textField_delay3.setText("500");
        textField_delay3.setColumns(10);
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(41)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(label, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addGroup(groupLayout.createSequentialGroup()
                                    .addComponent(lblNewLabel)
                                    .addPreferredGap(ComponentPlacement.UNRELATED)
                                    .addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                    .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(label_2, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(textField_3, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(label_1, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))))
                            .addGap(18)
                            .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                .addGroup(groupLayout.createSequentialGroup()
                                    .addComponent(label_3, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(textField_delay1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGroup(groupLayout.createSequentialGroup()
                                    .addComponent(label_4, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(textField_delay2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGroup(groupLayout.createSequentialGroup()
                                    .addComponent(label_5, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(textField_delay3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                            .addGap(18)
                            .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                .addComponent(rdo3_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rdo1_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rdo2_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
                            .addGap(27)
                            .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                .addComponent(rdo1_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rdo2_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rdo3_2, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))))
                    .addGap(33))
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                    .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addGroup(groupLayout.createSequentialGroup()
                                    .addGap(3)
                                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(label_5)
                                        .addComponent(textField_delay3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                    .addComponent(rdo3_1)
                                    .addComponent(rdo3_2))))
                        .addGroup(groupLayout.createSequentialGroup()
                            .addGap(25)
                            .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                .addComponent(lblNewLabel)
                                .addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label_3)
                                .addComponent(rdo1_1)
                                .addComponent(rdo1_2)
                                .addComponent(textField_delay1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(18)
                            .addComponent(label)
                            .addPreferredGap(ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                            .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                    .addComponent(rdo2_1)
                                    .addComponent(rdo2_2))
                                .addGroup(Alignment.TRAILING, groupLayout.createParallelGroup(Alignment.LEADING)
                                    .addGroup(groupLayout.createSequentialGroup()
                                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                            .addComponent(label_1)
                                            .addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(24)
                                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                            .addComponent(label_2)
                                            .addComponent(textField_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(groupLayout.createSequentialGroup()
                                        .addGap(3)
                                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                            .addComponent(label_4)
                                            .addComponent(textField_delay2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))))
                    .addGap(230))
        );
        getContentPane().setLayout(groupLayout);
    }
}
