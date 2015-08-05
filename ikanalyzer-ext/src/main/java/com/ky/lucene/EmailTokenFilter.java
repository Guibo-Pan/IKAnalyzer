/**
 * 
 */
package com.ky.lucene;

import java.io.IOException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

/**
 * <p>
 * <b>EmailTokenFilter</b> 是
 * </p>
 *
 * @since 2013-2-4
 * @author czhcc
 * @version $Id: EmailTokenFilter.java 12517 2013-08-27 00:49:11Z czh $
 *
 */
public class EmailTokenFilter extends TokenFilter
{

	/**
	 * email正则表达式
	 */
	private final static String EMAIL_REGEX = "(\\w+(\\.\\w+)*)@(\\w+(\\.\\w+)+)";

	private final CharTermAttribute termAtt;
	
	private final OffsetAttribute offsetAtt;
	
	private final Pattern pattern;
	
	/**
	 * 保留分出的用户名和域名及它们的位置信息
	 */
	private Stack<Email> emailStack = new Stack<EmailTokenFilter.Email>();

	/**
	 * @param input
	 */
	protected EmailTokenFilter(TokenStream input) {
		super(input);
		this.termAtt = addAttribute(CharTermAttribute.class);
	    this.offsetAtt = addAttribute(OffsetAttribute.class);
	    pattern = Pattern.compile(EMAIL_REGEX);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.lucene.analysis.TokenStream#incrementToken()
	 */
	@Override
	public boolean incrementToken() throws IOException {
		if(emailStack.size() > 0)
		{//如果有email信息，则建立新的token
			Email email = emailStack.pop();
			termAtt.setEmpty();
			termAtt.append(email.getValue());
			offsetAtt.setOffset(email.getStart(), email.getEnd());
			return true;
		}
		if (!input.incrementToken())
		{
		      return false;
		}
		
		String term = termAtt.toString();
		Matcher m = pattern.matcher(term);
		if(m.matches())
		{
			String name = m.group(1);
			String domain = m.group(3);
			int start = offsetAtt.startOffset();
			int end = offsetAtt.endOffset();
			Email email = new Email();
			email.setValue(name);
			email.setType("email_username");
			email.setStart(start);
			email.setEnd(start + name.length());
			emailStack.push(email);
			email = new Email();
			email.setValue(domain);
			email.setType("email_domain");
			email.setStart(start + name.length() + 1);
			email.setEnd(end);
			emailStack.push(email);
		}
		return true;
	}

	class Email
	{
		private String type;
		
		private String value;
		
		private int start;
		
		private int end;

		/**
		 * @return the start
		 */
		public int getStart() {
			return start;
		}

		/**
		 * @param start the start to set
		 */
		public void setStart(int start) {
			this.start = start;
		}

		/**
		 * @return the end
		 */
		public int getEnd() {
			return end;
		}

		/**
		 * @param end the end to set
		 */
		public void setEnd(int end) {
			this.end = end;
		}

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
		
	}

}
