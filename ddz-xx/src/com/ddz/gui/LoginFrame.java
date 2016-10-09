package com.ddz.gui;

import java.awt.Color;
import java.awt.Font;

import javax.crypto.Cipher;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.ddz.config.Config;
import com.ddz.entity.Player;
import com.ddz.entity.Room;
import com.ddz.entity.User;
import com.ddz.msg.ClientLoginMsg;
import com.ddz.net.MyClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class LoginFrame extends JFrame implements ILoginFrame {
	private static MyClient myclient;// 让视图拥有1个网络类对象
	private ClientHallFrame hall;// 游戏大厅
	private JTextField textField;
	private JPasswordField passwordField;
	private static String host;
	JCheckBox checkBox;
	private static int port;
	private static ConfigFrame cf;

	public ClientHallFrame getHall() {
		return hall;
	}

	public void setHall(ClientHallFrame hall) {
		this.hall = hall;
	}

	public LoginFrame() {
		setResizable(false);
		/*
		 * myclient =MyClient.getInstanse();// 实例化出网络对象
		 * myclient.setLoginframe(this); myclient.connect(host,port );
		 */
		myclient = MyClient.getInstanse();// 实例化出网络对象
		myclient.setLoginframe(this);
		if (cf == null)
			cf = new ConfigFrame();

		doConnect();
		setSize(440, 360);
		setLocationRelativeTo(null);
		setTitle("登录");

		getContentPane().setLayout(null);

		// Image image=new ImageIcon("image/LoginBackground.jpg").getImage();
		Font font = new Font("TimesRoman", 1, 14);

		JLabel label = new JLabel("账号");
		label.setFont(font);
		label.setForeground(Color.blue);
		label.setBounds(140, 160, 40, 20);
		getContentPane().add(label);

		JLabel labe2 = new JLabel("密码");
		labe2.setFont(font);
		labe2.setForeground(Color.blue);
		labe2.setBounds(140, 195, 40, 20);
		getContentPane().add(labe2);

		textField = new JTextField();
		textField.setText(Config.getUsername());
		textField.setBounds(180, 160, 120, 20);
		getContentPane().add(textField);
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setText(Config.getPassword());
		passwordField.setBounds(180, 195, 120, 20);
		getContentPane().add(passwordField);

		checkBox = new JCheckBox("记住密码");
		checkBox.setSelected(true);

		checkBox.setForeground(Color.white);
		checkBox.setBounds(275, 225, 105, 25);
		checkBox.setFont(font);
		checkBox.setOpaque(false);
		getContentPane().add(checkBox);

		JButton btnNewButton = new JButton("");
		// 设置登录快捷键
		btnNewButton.registerKeyboardAction(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (checkBox.isSelected())
							Config.excuteDefaultUser(textField.getText(),
									passwordField.getText());
						else
							Config.excuteDefaultUser("", "");

						myclient.connect(Config.getClientHost(),
								Config.getClientPort());
						if (myclient.isConnected()) {
							// 获得用户名和密码
							String username = textField.getText();
							String password = passwordField.getText();

							// 发送登录报文
							ClientLoginMsg msg = new ClientLoginMsg(username,
									password);
							// 将消息发送给服务器
							myclient.sendMsg(msg);
						} else {
							JOptionPane.showMessageDialog(null, "服务器连接失败");
						}

					}
				}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 1.连接服务器
				// if (myclient.isConnected()) {
				// return;
				// }
				// 2.如果连接成功
				if (checkBox.isSelected())
					Config.excuteDefaultUser(textField.getText(),
							passwordField.getText());
				else
					Config.excuteDefaultUser("", "");

				myclient.connect(Config.getClientHost(), Config.getClientPort());
				if (myclient.isConnected()) {
					// 获得用户名和密码
					String username = textField.getText();
					String password = passwordField.getText();

					// 发送登录报文
					ClientLoginMsg msg = new ClientLoginMsg(username, password);
					// 将消息发送给服务器
					myclient.sendMsg(msg);
				} else {
					JOptionPane.showMessageDialog(null, "服务器连接失败");
				}
			}
		});

		btnNewButton.setBounds(120, 260, 80, 40);
		ImageIcon icon_3 = new ImageIcon("image/Login.jpg");
		btnNewButton.setIcon(icon_3);
		getContentPane().add(btnNewButton);

		JButton btnNewButton_1 = new JButton("");

		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LoginFrame.this.setVisible(false);
				new RegisterFrame(LoginFrame.this).setVisible(true);
			}
		});

		ImageIcon icon_4 = new ImageIcon("image/register.jpg");
		btnNewButton_1.setIcon(icon_4);
		btnNewButton_1.setBounds(240, 260, 80, 40);
		getContentPane().add(btnNewButton_1);

		JButton setBtn = new JButton("");
		setBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				cf.setVisible(true);

			}
		});
		setBtn.setBounds(360, 270, 30, 30);
		ImageIcon sIcon = new ImageIcon("image/setting.jpg");
		setBtn.setIcon(sIcon);
		getContentPane().add(setBtn);

		JLabel Label_1 = new JLabel("Label_1");
		Label_1.setIcon(new ImageIcon("image/LoginBackground.jpg"));
		Label_1.setBounds(0, 0, 432, 330);
		getContentPane().add(Label_1);

	}

	public static void doConnect() {
		if (cf == null)
			cf = new ConfigFrame();
		cf.setVisible(false);
		host = cf.getHost();
		port = cf.getPort();
		Config.excuteClientConfig(host, port);
		if (myclient.isConnected())
			myclient.disconnect();
		myclient.connect(host, port);

	}

	public static void main(String[] args) {
		new LoginFrame().setVisible(true);
	}

	@Override
	public void informSuc(User user, Player player, List<Room> rooms) {
		System.out.println(host + "  " + port);
		JOptionPane.showMessageDialog(null, user.getUsername() + "登录成功");
		this.setVisible(false);// 登录界面消失
		if (hall == null) {
			hall = new ClientHallFrame(user, player, rooms);// 创建游戏大厅
		}
		hall.setTitle(user.getUsername());
		hall.setVisible(true);
	}

	@Override
	public void informFail(String error) {
		JOptionPane.showMessageDialog(null, error);

	}
}
