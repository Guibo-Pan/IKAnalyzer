/**
 * AbstractUpdater
 * 
 * @author Guibo Pan
 */
package org.wltea.analyzer.update;

import java.io.IOException;

import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;

/**
 * AbstractUpdater
 * 
 * @author Guibo Pan
 * @since 2015年8月4日
 */
public abstract class AbstractUpdater  implements ResourceLoaderAware{
	
	protected ResourceLoader loader = null;
	
	//更新词典
	public abstract boolean update();
	
	@Override
	public void inform(ResourceLoader loader) throws IOException {
		this.loader = loader;
	}
}
