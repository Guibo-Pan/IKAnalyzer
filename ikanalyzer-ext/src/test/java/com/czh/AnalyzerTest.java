/**
 * 
 */
package com.czh;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.junit.Ignore;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ky.lucene.IKTokenizerFactory;
import com.ky.util.AnalyzerUtils;

/**
 * <p>
 * <b>AnalyzerTest</b> 是
 * </p>
 *
 * @since 2013-8-16
 * @author czhcc
 * @version $Id: AnalyzerTest.java 12963 2015-05-05 06:08:43Z czh $
 *
 */
public class AnalyzerTest
{
	@Ignore
	@Test
	public void test1() throws Exception
	{
		QueryParser contentquery = new QueryParser("content", new CJKAnalyzer());
		Query parse = contentquery.parse("ABC");
		System.out.println(parse.toString());
	}
	
	@Test
	public void test2() throws Exception
	{
//		String s1 = "2007年3月9日晚上8时多，事主张小平报称其停放在长江路华信花园170号铺面门口的粤DJD877红色光威牌GW125-6摩托车被盗";
//		String s1 = "张小平对这事主张和平解决";
		String s1 = "在當日例行記者會上有記者問，印尼方面２０日炸沉４１艘涉嫌非法捕撈的外國漁船，這些漁船來自中國、越南、泰國和菲律賓等國。中方對此有何評論？";
//		String s1 = "在当日例行记者会上有记者问，印尼方面２０日炸沉４１艘涉嫌非法捕捞的外国渔船，这些渔船来自中国、越南、泰国和菲律宾等国。中方对此有何评论？";
		IKAnalyzer ikAnalyzer = new IKAnalyzer(true);
		AnalyzerUtils.displayTokens(ikAnalyzer, s1);
	}
	
	@Ignore
	@Test
	public void test3() throws Exception
	{
		Map<String, String> param = new HashMap<String, String>();
		param.put("smart", "true");
		IKTokenizerFactory factory = new IKTokenizerFactory(param);
		Tokenizer tokenizer = factory.create();
	}
}
