package com.ky.util;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class IKEntry {
	enum WORDTPYE{
		PHONE_NUM,QQ_NUM,EMAIL,ID_NUM,PLATE_NUM,ENG_WORD,CN_WORD,OTHER
	};
	//其他
	public static final int TYPE_OTHER = 0;
	//英文
	public static final int TYPE_ENGLISH = 1;
	//数字
	public static final int TYPE_ARABIC = 2;
	//英文数字混合
	public static final int TYPE_LETTER = 3;
	//中文词元
	public static final int TYPE_CNWORD = 4;
	//中文单字
	public static final int TYPE_CNCHAR = 64;
	//日韩文字
	public static final int TYPE_OTHER_CJK = 8;
	//中文数词
	public static final int TYPE_CNUM = 16;
	//中文量词
	public static final int TYPE_COUNT = 32;
	//中文数量词
	public static final int TYPE_CQUAN = 48;
	//手机号码
	public static final int TYPE_PHONE_NUM = 5;
	//QQ号码
	public static final int TYPE_QQ_NUM = 6;
	//E-mail
	public static final int TYPE_EMAIL = 7;
	//身份证号码
	public static final int TYPE_ID_NUM = 9;
	//车牌号码
	public static final int TYPE_PLATE_NUM = 10;
	
	public static final Set<String> PROV = new HashSet<String>(Arrays.asList("京", "津", "冀", "晋", "蒙", "辽", "吉", "黑", 
			"沪", "苏", "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘", "粤", "桂", "琼", "渝", "川", "贵", "云", 
			"藏", "陕", "秦", "甘", "青", "宁", "新", "港", "澳", "台"));
			
	int type;
	String text;
	
	public IKEntry(){}
	
	public IKEntry(int type,String text){
		this.type = type;
		this.text = text;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public boolean isProvShort() {
		return PROV.contains(text);
	}
}
