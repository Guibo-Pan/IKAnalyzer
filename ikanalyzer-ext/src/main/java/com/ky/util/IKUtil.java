/**
 * IKAnalyzer工具类 版本 1.0
 * IKAnalyzer tool release 1.0
 * 
 * 工具类用于获取字符串输入流，或文件流的分词结果及属性
 * 完全使用IK的原生接口，而不需要依赖Lucene的包
 * 可通过实现类似List<String> list = ik.getWords("abcde")
 * 以及List<String> list = ik.getWords(file)的方法进行调用
 * 
 * @author Guibo Pan
 * @since v1.0 2015/7
 */

package com.ky.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;


public class IKUtil {
	
	//default use smart
	public IKUtil(){
	}
	
	public List<String> getWords(String inputString) {
		Reader reader = new StringReader(inputString);
		return get(reader);
	}
	
	/**
	 * 单个文件分词
	 * @param infile 输入文件，要求格式为UTF-8格式
	 * @return 分词的结果
	 */
	public List<String> getWords(File infile) {
		//如果文件不存在
		if(!infile.exists()) {
			return null;
		}

		try {
			if(!infile.isDirectory()) {
				Reader reader = new InputStreamReader(new FileInputStream(infile),"UTF-8");
				return get(reader);
			} else {
				List<String> result = new LinkedList<String>();
				File[] files = infile.listFiles();
				for(File f:files) {
					result.addAll(get(new FileReader(f)));
				}
				return result;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param reader
	 * @return
	 */
	public List<String> get(Reader reader) {
		List<String> result = new LinkedList<String>();
		IKSegmenter ikSegmenter = new IKSegmenter(reader, true);
		try{
			Lexeme lexeme;
			while((lexeme = ikSegmenter.next()) != null) {
				result.add(lexeme.getLexemeText());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<IKEntry> getIK(String text) {
		List<IKEntry> result = null;
		try{
			result = getIK(new StringReader(text));
		} catch(IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public List<IKEntry> getIK(File infile) {
		List<IKEntry> result = null;
		try{
			if(infile.isDirectory()){
				File[] files = infile.listFiles();
				result = new LinkedList<IKEntry>();
				List<IKEntry> tmpList;
				for(File f:files){
					tmpList = getIK(new FileReader(f));
					result.addAll(tmpList);
				}
			}else {
				result = getIK(new FileReader(infile));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 通过Reader获取输入的词元与对应的类型
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public List<IKEntry> getIK(Reader reader) throws IOException {
		List<IKEntry> result = new LinkedList<IKEntry>();
		IKSegmenter ikSegmenter = new IKSegmenter(reader, true);
		Lexeme lexeme;
		while((lexeme = ikSegmenter.next()) != null) {
			String ltext = lexeme.getLexemeText();
			if(!ltext.equals(" ")) {
				int ltype = chooseType(ltext,lexeme.getLexemeType());
				//车牌号省简称与号码
				if(ltype == IKEntry.TYPE_PLATE_NUM && result.get(result.size()-1).isProvShort()){
					ltext = result.remove(result.size()-1).getText()+ltext;
				}
				result.add(new IKEntry(ltype,ltext));
			}
		}
		return result;
	}
	
	/**
	 * 通过正则匹配词元类型
	 * @param text
	 * @param type
	 * @return
	 */
	private int chooseType(String text,int type) {
		if(type == Lexeme.TYPE_ARABIC) {
			//数字
			//可为手机号码，电话号码，QQ号码，或者身份证号码
			
			//判断是否手机号码，电话号码
			Pattern pattern = Pattern.compile("(^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$)|(^((\\d{3,4})|\\d{3,4}-|\\s)?\\d{7,8}$)");
			//Matcher matcher = pattern.matcher("+86-13824418583");

			if(pattern.matcher(text).find()){
				//匹配为手机号码
				return IKEntry.TYPE_PHONE_NUM;
			}
			
			//判断是否为QQ号码
			pattern = Pattern.compile("^[1-9]\\d{4,9}$");
			if(pattern.matcher(text).find()){
				//匹配为QQ号码
				return IKEntry.TYPE_QQ_NUM;
			}
			
			//判断是否为身份证号码，无X,18位或15位
			pattern = Pattern.compile("^([1-9]\\d{9}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$)|(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)");

			if(pattern.matcher(text).find()){
				//匹配为身份证号码
				return IKEntry.TYPE_ID_NUM;
			}
			
			//匹配不到,类型为数字
			return IKEntry.TYPE_ARABIC;

		} else if(type == Lexeme.TYPE_LETTER) {
			//数字与字母混合
			//可为email，车牌号，带X的身份证号码
			
			Pattern pattern = Pattern.compile("^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,5}$");
			//"time12@gmail2.w.com"
			if(pattern.matcher(text).find()){
				//匹配为合法邮箱地址
				return IKEntry.TYPE_EMAIL;
			}
			
			//判断是否车牌号码
			//[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$
			pattern = Pattern.compile("^[A-Z]{1}[A-Z0-9]{5}$");
			if(pattern.matcher(text).find()) {
				//匹配为合法车牌号码
				return IKEntry.TYPE_PLATE_NUM;
			}
			
			//判断是否带X的身份证号码，只有18位的身份证带X
			pattern = Pattern.compile("^[1-9]\\d{9}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(X|x)$");
			if(pattern.matcher(text).find()) {
				//匹配为合法身份证号码
				return IKEntry.TYPE_ID_NUM;
			}
			
			//判断是否手机号码，电话号码,带-
			pattern = Pattern.compile("^\\d{3,4}-\\d{3,4}-\\d{3,4}$");
			
			if(pattern.matcher(text).find()){
				//匹配为手机号码
				return IKEntry.TYPE_PHONE_NUM;
			}
			return IKEntry.TYPE_LETTER;

		} else {
			//其他类型，直接返回
			return type;
		}
	}

	public static void main(String[] args) {
		IKUtil ik = new IKUtil();
		//List<String> list = ik.getWords("例行記者會上有记者问，非法捕撈的外國漁船。现在我们来测试一下简体的功能。这是一个中文分词的例子，你可以直接运行它！IKAnalyer can analysis english text too");
		//List<String> list = ik.getWords(new File("D:/Program Data/Projects/data/"));
		//List<IKEntry> list = ik.getIK(new File("C:/Users/Guibo/Desktop/1.txt"));
		List<IKEntry> list = ik.getIK("http://baidu/com/cn?w=8");
		for(IKEntry en:list){
			System.out.println(en.text+" "+en.type);
		}
//		for(String s:list){
//			System.out.println(s);
//		}
	}
}
