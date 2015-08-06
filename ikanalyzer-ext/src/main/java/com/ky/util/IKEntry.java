package com.ky.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import com.sun.xml.internal.xsom.impl.scd.Iterators.Map;

public class IKEntry {
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
	
	public static HashMap<Integer, String> typeName = null;
	
	public static final Set<String> PROV = new HashSet<String>(Arrays.asList("京", "津", "冀", "晋", "蒙", "辽", "吉", "黑", 
			"沪", "苏", "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘", "粤", "桂", "琼", "渝", "川", "贵", "云", 
			"藏", "陕", "秦", "甘", "青", "宁", "新", "港", "澳", "台"));
			
	private int type;
	private String text;
	
	public IKEntry(){ 
		init();
	}
	
	public IKEntry(int type,String text){
		this.type = type;
		this.text = text;
		init();
	}
	
	private static int[] typeArr = new int[]{TYPE_OTHER,TYPE_ENGLISH,TYPE_ARABIC,TYPE_LETTER,
			TYPE_CNWORD,TYPE_CNCHAR,TYPE_OTHER_CJK,TYPE_CNUM,TYPE_COUNT,TYPE_CQUAN,
			TYPE_PHONE_NUM,TYPE_QQ_NUM,TYPE_EMAIL,TYPE_ID_NUM,TYPE_PLATE_NUM};
	private static String[] typeNames = new String[]{"OTHER","ENGLISH","ARABIC","LETTER",
		"CNWORD","CNCHAR","OTHER_CJK","CNUM","COUNT","CQUAN",
		"PHONE_NUM","QQ_NUM","EMAIL","ID_NUM","PLATE_NUM"};
	
	private static void init() {
		if(typeName == null) {
			typeName = new HashMap<Integer, String>();
			for(int i = 0; i < typeArr.length; i++) {
				typeName.put(typeArr[i], typeNames[i]);
			}
		}
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
	
	public String getTypeName() {
		return typeName.get(this.type);
	}
}
