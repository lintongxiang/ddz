package com.ddz.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.ddz.config.Config;
import com.ddz.entity.User;
import com.ddz.msg.ClientLoginMsg;
import com.ddz.msg.ClientRegisterMsg;
import com.ddz.net.MyClient;
import com.ddz.util.ImageUtil;
import com.ddz.util.Salt;
import com.ddz.util.common;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterFrame extends JFrame implements IRegisterFrame {
	private static MyClient myclient;// 让视图拥有1个网络类对象
	private JPasswordField password_1;
	private JPasswordField password_2;
	private JTextField textField_1;
	private JRadioButton radio_1;
	private JRadioButton radio_2;
	private JLabel label_1;
	private JLabel label_2;
	private LoginFrame loginFrame;

	public RegisterFrame(LoginFrame loginFrame) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new LoginFrame().setVisible(true);
			}
		});
		this.loginFrame = loginFrame;
		myclient = MyClient.getInstanse();// 实例化出网络对象
		myclient.setRegisterFrame(this);
		setSize(408, 325);
		setLocationRelativeTo(null);
		setTitle("注册");
		getContentPane().setLayout(null);
		setResizable(false);

		Font font = new Font("TimesRoman", 1, 14);

		JLabel label1 = new JLabel("账号:");
		label1.setFont(font);
		label1.setForeground(Color.LIGHT_GRAY);
		label1.setBounds(50, 50, 40, 20);
		getContentPane().add(label1);

		textField_1 = new JTextField();
		textField_1.setBounds(130, 50, 140, 20);
		getContentPane().add(textField_1);
		// textField_1.setColumns(10);

		JLabel label2 = new JLabel("密码:");
		label2.setFont(font);
		label2.setForeground(Color.LIGHT_GRAY);
		label2.setBounds(50, 100, 40, 20);
		getContentPane().add(label2);

		password_1 = new JPasswordField();
		password_1.setBounds(130, 100, 140, 20);
		getContentPane().add(password_1);
		// password_1.setColumns(10);

		JLabel label3 = new JLabel("确认密码:");
		label3.setFont(font);
		label3.setForeground(Color.LIGHT_GRAY);
		label3.setBounds(50, 150, 70, 20);
		getContentPane().add(label3);

		password_2 = new JPasswordField();
		password_2.setBounds(130, 150, 140, 20);
		getContentPane().add(password_2);
		// password_2.setColumns(10);

		label_2 = new JLabel("label_2");
		label_2.setIcon(new ImageIcon("image/farmers.jpg"));
		label_2.setBounds(290, 60, 100, 115);
		getContentPane().add(label_2);

		radio_1 = new JRadioButton("男");
		radio_1.setFont(font);
		radio_1.setForeground(Color.LIGHT_GRAY);
		radio_1.setBounds(130, 190, 60, 20);
		radio_1.setFont(font);
		radio_1.setOpaque(false);
		radio_1.setSelected(true);
		getContentPane().add(radio_1);
		radio_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				label_2.setIcon(new ImageIcon("image/farmers.jpg"));
			}
		});

		radio_2 = new JRadioButton("女");
		radio_2.setFont(font);
		radio_2.setForeground(Color.LIGHT_GRAY);
		radio_2.setBounds(230, 190, 60, 20);
		radio_2.setFont(font);
		radio_2.setOpaque(false);
		getContentPane().add(radio_2);
		radio_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				label_2.setIcon(new ImageIcon("image/girls.jpg"));
			}
		});

		ButtonGroup group = new ButtonGroup();
		group.add(radio_1);
		group.add(radio_2);

		JButton btnNewButton = new JButton("注册");
		btnNewButton.setBounds(150, 230, 80, 40);
		ImageIcon icon = new ImageIcon("image/register.jpg");
		btnNewButton.setIcon(icon);
		getContentPane().add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Return");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				new LoginFrame().setVisible(true);
				RegisterFrame.this.dispose();
			}
		});
		btnNewButton_1.setBounds(350, 275, 50, 25);
		btnNewButton_1.setIcon(ImageUtil.Return);
		getContentPane().add(btnNewButton_1);

		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 1.连接服务器
				// if (myclient.isConnected()) {
				// return;
				// }
				// 2.如果连接成功
				if (myclient.connect("127.0.0.1", 1245)) {
					String username = textField_1.getText();
					String password1 = password_1.getText();
					String password2 = password_2.getText();
					Boolean bl1 = radio_1.isSelected();
					Boolean bl2 = radio_2.isSelected();

					if (common.empty(username) || common.empty(password1)
							|| common.empty(password2)) {
						JOptionPane.showMessageDialog(null, "不能为空！");
					} else if (!password1.equals(password2)) {
						JOptionPane.showMessageDialog(null, "两次输入密码不一致！请重新输入！");
						password_1.setText("");
						password_2.setText("");
					} else {
						int sex = (bl1 == true ? 1 : 0);
						User user = new User();
						user.setUsername(username);
						user.setPassword(password2);
						user.setSex(sex);
						user.setSalt(Salt.createSalt());
						// 数据传到服务器

						ClientRegisterMsg msg = new ClientRegisterMsg(user);
						myclient.sendMsg(msg);

					}
				} else {
					JOptionPane.showMessageDialog(null, "服务器连接失败");
				}

			}
		});
		// 设置注册快捷键
		btnNewButton.registerKeyboardAction(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (myclient.connect("127.0.0.1", 1245)) {
							String username = textField_1.getText();
							String password1 = password_1.getText();
							String password2 = password_2.getText();
							Boolean bl1 = radio_1.isSelected();
							Boolean bl2 = radio_2.isSelected();

							if (common.empty(username)
									|| common.empty(password1)
									|| common.empty(password2)) {
								JOptionPane.showMessageDialog(null, "不能为空！");
							} else if (!password1.equals(password2)) {
								JOptionPane.showMessageDialog(null,
										"两次输入密码不一致！请重新输入！");
								password_1.setText("");
								password_2.setText("");
							} else {
								int sex = (bl1 == true ? 1 : 0);
								User user = new User();
								user.setUsername(username);
								user.setPassword(password2);
								user.setSex(sex);
								user.setSalt(Salt.createSalt());
								// 数据传到服务器

								ClientRegisterMsg msg = new ClientRegisterMsg(
										user);
								myclient.sendMsg(msg);

							}
						} else {
							JOptionPane.showMessageDialog(null, "服务器连接失败");
						}
					}

				}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		label_1 = new JLabel("label_1");
		label_1.setIcon(new ImageIcon("image/Rebackground.jpg"));
		label_1.setBounds(0, 0, 400, 300);
		getContentPane().add(label_1);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new RegisterFrame(new LoginFrame()).setVisible(true);
	}

	@Override
	public void informSuc(String username) {
		JOptionPane.showMessageDialog(null, username + "注册成功");
		this.setVisible(false);// 注册界面消失
		new LoginFrame().setVisible(true);
	}

	@Override
	public void informFail(String error) {
		JOptionPane.showMessageDialog(null, error);
	}
}
