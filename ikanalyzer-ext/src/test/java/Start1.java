import com.ky.util.AdWords;

/**
 * 
 */

/**
 * <p>
 * <b>Start1</b> 是
 * </p>
 *
 * @since 2013-2-1
 * @author czhcc
 * @version $Id: Start1.java 12517 2013-08-27 00:49:11Z czh $
 *
 */
public class Start1
{
	
	private void testF1() throws Exception
	{
		String[] ss = AdWords.adWords(new String[] { "D:/temp/index_test4/" },
				"file", "e.txt", "text");
		for (String s : ss) {
			System.out.println(s);
		}
	}
	
	private void testF2() throws Exception
	{
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		
	}

}
