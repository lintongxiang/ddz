package com.ddz.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

public class LogUtil {
	//按照固定格式显示时间
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//日志打印  控件对象  打印的信息
	public static void log(JTextArea textArea,String msg){
		if(textArea==null){
			return;
		}
		msg =msg+"  "+sdf.format(new Date());
		textArea.setText(textArea.getText()+"\n"+msg);
	}
}
