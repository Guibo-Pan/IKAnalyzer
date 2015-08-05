/**
 * 
 */
package com.czh;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.solr.analysis.TokenizerChain;
import org.junit.Test;

import com.ky.lucene.IKTokenizerFactory;
import com.ky.util.AnalyzerUtils;

/**
 * @author czhcc
 *
 */
public class TokenizerFactoryTest
{
	/**
	 * 测试简繁字
	 * 
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception
	{
		Map<String, String> param = new HashMap<String, String>();
		param.put("smart", "true");
		TokenizerFactory tokenizer = new IKTokenizerFactory(param);
		TokenizerChain chain = new TokenizerChain(tokenizer, new TokenFilterFactory[]{});
		String s1 = "在当日例行记者会上有记者问，印尼方面２０日炸沉４１艘涉嫌非法捕捞的外国渔船，这些渔船来自中国、越南、泰国和菲律宾等国。中方对此有何评论？";
		AnalyzerUtils.displayTokens(chain, s1);
	}
}
