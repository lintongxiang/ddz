package com.ddz.util;

import java.util.Random;

public class Salt {
	public static String createSalt(){
		char[] ch = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
		Random ran = new Random();
		int len = ch.length;
		int index,size;
		size = 8;
		String salt = "";
		for (int i = 0; i < size; i++) {
			index = ran.nextInt(len);
			salt += ch[index];
		}
		return salt;
		
	}
	
}
