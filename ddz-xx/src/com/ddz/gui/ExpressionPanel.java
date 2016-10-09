package com.ddz.gui;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.ddz.util.ImageUtil;


public class ExpressionPanel extends JPanel {

	public ExpressionPanel() {
		setSize(239, 161);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				ExpressionPanel.this.setVisible(false);
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(ImageUtil.page, 0, 0, this);
	}
}
