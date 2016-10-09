package com.ddz.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.ddz.config.Config;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConfigFrame extends JFrame{
	public  JTextField hostField;
	private JTextField portField;
	public ConfigFrame() {
		addWindowListener(new WindowAdapter() {
	
			@Override
			public void windowClosing(WindowEvent e) {
				LoginFrame.doConnect();
			}
		});
		
		setLocationRelativeTo(null);
		setResizable(false);
		setSize(new Dimension(231, 176));
		getContentPane().setLayout(null);
		
		JLabel label = new JLabel("主机:");
		label.setBounds(10, 23, 36, 15);
		getContentPane().add(label);
		
		JLabel label_1 = new JLabel("端口:");
		label_1.setBounds(10, 60, 36, 15);
		getContentPane().add(label_1);
		
		hostField = new JTextField(Config.getClientHost());
		hostField.setBounds(54, 20, 99, 21);
		getContentPane().add(hostField);
		hostField.setColumns(10);
		
		portField = new JTextField(Config.getClientPort()+"");
		portField.setBounds(56, 57, 66, 21);
		getContentPane().add(portField);
		portField.setColumns(10);
		
		JButton btnNewButton = new JButton("确定");
		btnNewButton.setFont(new Font("宋体", Font.BOLD, 12));
		btnNewButton.setForeground(Color.BLUE);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginFrame.doConnect();
				
			}
		});
		btnNewButton.setBounds(54, 99, 68, 23);
		getContentPane().add(btnNewButton);
		JLabel Label_1 = new JLabel("Label_1");
		Label_1.setIcon(new ImageIcon("image/hz.jpeg"));
		Label_1.setBounds(-80, 0, 311, 176);
		getContentPane().add(Label_1);
	}
	public static void main(String[] args) {
		new ConfigFrame().setVisible(true);
	}
	public String getHost(){
		return hostField.getText();
	}
	public int getPort(){
		return Integer.parseInt(portField.getText());
	}
}
