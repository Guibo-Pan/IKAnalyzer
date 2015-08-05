/**
 * 
 */
package com.ky.lucene;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

/**
 * <p>
 * <b>EmailTokenFilterFactory</b> 是
 * </p>
 *
 * @since 2013-2-4
 * @author czhcc
 * @version $Id: EmailTokenFilterFactory.java 12517 2013-08-27 00:49:11Z czh $
 *
 */
public class EmailTokenFilterFactory extends TokenFilterFactory
{

	/**
	 * @param args
	 */
	protected EmailTokenFilterFactory(Map<String, String> args) {
		super(args);
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.util.TokenFilterFactory#create(org.apache.lucene.analysis.TokenStream)
	 */
	@Override
	public TokenStream create(TokenStream input)
	{
		return new EmailTokenFilter(input);
	}

}
