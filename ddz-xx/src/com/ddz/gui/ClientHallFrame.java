package com.ddz.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultCaret;

import com.ddz.entity.Player;
import com.ddz.entity.Room;
import com.ddz.entity.User;
import com.ddz.msg.ClientChatMsg;
import com.ddz.msg.ClientUnRegisterMsg;
import com.ddz.net.MyClient;
import com.ddz.util.ImageUtil;
import com.ddz.util.LogUtil;
import com.ddz.util.ShowUtil;

public class ClientHallFrame extends JFrame implements IHallFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyClient myClient;
	JPanel roomsPanel;
	private List<JButton> btnList;
	private JTextField textField;
	private JTextArea textArea;
	private JList list;
	private User user;
	private Player player;
	private List<Room> rooms;
	private GameHall gameHall;

	private PlayFrame playFrame;

	public ClientHallFrame(User user1, Player player, List<Room> rooms) {
		this.user = user1;
		this.setPlayer(player);
		myClient = MyClient.getInstanse();
		myClient.setHallFrame(this);// 让客户端拥有游戏大厅
		setSize(899, 700);
		setLocationRelativeTo(null);
		setTitle("游戏大厅");
		getContentPane().setLayout(null);

		this.rooms = rooms;
		// for (int i = 1; i <= 16; i++) {
		// Room r = new Room(i, "", "", "", Room.IDLE);
		// rooms.add(r);
		// }

		gameHall = new GameHall(rooms, user.getUsername(), this);
		gameHall.setForeground(Color.WHITE);
		gameHall.setBackground(Color.CYAN);
		JScrollPane sp = new JScrollPane(gameHall);
		sp.setBounds(0, 0, 698, 670);
		getContentPane().add(sp);
		// showRoomList(rooms);

		list = new JList();
		list.setForeground(Color.GREEN);
		list.setFont(new Font("新宋体", Font.BOLD, 14));
		list.setModel(new AbstractListModel() {
			String[] values = new String[] { "list1", "list2", "list3",
					"list4", "list5", "list4", "list5", "list4",

					"list5", "list4", "list5" };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setOpaque(false);
		JScrollPane jsp = new JScrollPane(list);
		jsp.setOpaque(false);
		jsp.getViewport().setOpaque(false);
		jsp.setOpaque(false);
		jsp.setBounds(708, 165, 175, 150);
		getContentPane().add(jsp);

		JLabel lblNewLabel = new JLabel("handle");

		lblNewLabel
				.setIcon(user.getSex() == 1 ? ImageUtil.boy : ImageUtil.girl);
		lblNewLabel.setBounds(708, 10, 100, 115);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("name");
		lblNewLabel_1.setBounds(729, 140, 79, 15);
		lblNewLabel_1.setText(user.getUsername());
		getContentPane().add(lblNewLabel_1);

		// 发送
		JButton btnNewButton = new JButton("");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String chatContent = textField.getText();
				ClientChatMsg msg = new ClientChatMsg(user.getUsername(),
						chatContent);
				myClient.sendMsg(msg);
				textField.setText("");
			}
		});
		// 设置发送快捷键
		btnNewButton.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String chatContent = textField.getText();
				ClientChatMsg msg = new ClientChatMsg(user.getUsername(),
						chatContent);
				myClient.sendMsg(msg);
				textField.setText("");

			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		btnNewButton.setBounds(858, 619, 25, 25);
		btnNewButton.setIcon(ImageUtil.send);
		getContentPane().add(btnNewButton);

		textField = new JTextField();
		textField.setForeground(Color.MAGENTA);
		textField.setOpaque(false);
		textField.setBounds(712, 619, 131, 25);
		getContentPane().add(textField);
		textField.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setForeground(Color.BLACK);
		scrollPane.setSize(new Dimension(131, 175));
		scrollPane.setBounds(708, 330, 175, 238);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		getContentPane().add(scrollPane);

		textArea = new JTextArea();
		textArea.setOpaque(false);
		textArea.setCaretColor(Color.GREEN);
		textArea.setDisabledTextColor(Color.BLUE);
		textArea.setForeground(Color.CYAN);
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setFont(new Font("仿宋_GB2312", Font.PLAIN, 14));
		textArea.setEnabled(false);
		textArea.setSize(new Dimension(131, 175));
		textArea.setRows(20);
		textArea.setColumns(5);
		textArea.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();

		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane.setViewportView(textArea);

		JLabel label = new JLabel("在此聊天：");
		label.setForeground(Color.RED);
		label.setFont(new Font("华文新魏", Font.PLAIN, 13));
		label.setBounds(713, 583, 109, 21);
		getContentPane().add(label);
		
		JLabel lblNewLabel_2 = new JLabel("RightBg");
		lblNewLabel_2.setIcon(ImageUtil.bg);
		lblNewLabel_2.setBounds(700, 0, 500, 1000);
		getContentPane().add(lblNewLabel_2);
		
	

		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				if(ClientHallFrame.this.player.getStatus()==Player.INROOM){
					JOptionPane.showMessageDialog(null, "在房间中，请先退房");
					return;
				}
				ClientUnRegisterMsg msg=new ClientUnRegisterMsg(ClientHallFrame.this.user);
				MyClient.getInstanse().sendMsg(msg);
				ClientHallFrame.this.dispose();
			}
		});
	}

	public void showRoomList(List<Room> rooms) {

		this.rooms = rooms;
		this.gameHall.setRoomList(this.rooms);
		this.gameHall.setPreferredSize(new Dimension(680,
				((this.rooms.size() - 1) / 4 + 1) * 170));
		this.gameHall.repaint();
	}

	@Override
	public void showUserList(final List<User> userList) {
		final String[] values = new String[userList.size()];
		list.setModel(new AbstractListModel() {
			// 对象段
			{
				for (int i = 0; i < values.length; i++) {
					values[i] = userList.get(i).getUsername() + "    分数"
							+ userList.get(i).getScore() + "";
				}
			}

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		ClientHallFrame.this.repaint();
	}

	@Override
	public void showChatMessage(String name, String chatContent) {
		String chatmsg = name + "说：" + chatContent;
		System.out.println(chatmsg);

		ShowUtil.log(textArea, chatmsg);

	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void enterRoom(Player player,Room r) {
		if(this.player.getUser().getUsername().equals(player.getUser().getUsername()))
			this.player=player;
		else
			return;
		for (Room room : rooms) {
			if(room.exist(user.getUsername())){
				room=r;
				break;
			}
		}
		//if (playFrame == null) {
		if(playFrame!=null)
			playFrame.dispose();
		playFrame=null;
		playFrame = new PlayFrame(r,this.player);
		playFrame.setVisible(true);
//		} else {
//			playFrame.setRoom(r);
//			playFrame.reFreshName();
//		}
	}

	@Override
	public void getOutRoom(Player player) {
		this.player=player;
		System.out.println(this.player.getUser());
		this.gameHall.setPlayer(this.player);
	}
}
