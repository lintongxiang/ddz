package com.ddz.util;

import java.awt.Point;


public class common {

	/**
	 * @param args
	 */
	public static boolean empty(String str){
		if(str==null||"".equals(str))
			return true;
		return false;
	}
	
	public static boolean inRec(Point lt, int w, int h, Point p) {
		if (lt == null || p == null) {
			return false;
		}
		int x=(int) lt.getX();
		int y=(int) lt.getY();
		if(x+w>=p.getX()&&y+h>=p.getY()&&p.getX()>=x&&p.getY()>=y){
			return true;
		}
		return false;
	}
}
