/**
 * 
 */
package com.ky.lucene;

import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import org.wltea.analyzer.lucene.IKTokenizer;

/**
 * <p>
 * <b>IKTokenizerFactory</b> 是在Solr下进行配置
 * <field name="content" type="text_ik" indexed="true" stored="false" multiValued="false" />
 * 
 * <fieldType name="text_ik" class="solr.TextField" >
        <analyzer type="index">
            <tokenizer class="com.ky.lucene.IKTokenizerFactory" smart="true"/>
        </analyzer> 
        <analyzer type="query">
            <tokenizer class="com.ky.lucene.IKTokenizerFactory" smart="true"/>
        </analyzer> 
    </fieldType>
 * </p>
 *
 * @since 2013-2-4
 * @author czhcc
 * @version $Id: IKTokenizerFactory.java 12517 2013-08-27 00:49:11Z czh $
 *
 */
public class IKTokenizerFactory extends TokenizerFactory
{
	private final static String KEY = "smart";
	
	private boolean useSmart = false;

	/**
	 * @param args
	 */
	public IKTokenizerFactory(Map<String, String> args) {
		super(args);
		String smart = args.get(KEY);
		if("true".equals(smart)){
			useSmart = true;
		}
	}

	/**
	 * org.apache.solr.analysis.TokenizerChain
	 * 
	 * @see org.apache.lucene.analysis.util.TokenizerFactory#create(org.apache.lucene.util.AttributeFactory)
	 */
	@Override
	public Tokenizer create(AttributeFactory factory)
	{
		Tokenizer _IKTokenizer = new IKTokenizer(useSmart);
		
		return _IKTokenizer;
	}

}
