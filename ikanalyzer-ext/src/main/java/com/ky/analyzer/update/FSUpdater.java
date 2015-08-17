/**
 * FSUpdater
 * 
 * @author Guibo Pan
 */
package com.ky.analyzer.update;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.wltea.analyzer.dic.Dictionary;

/**
 * FSUpdater
 * 
 * @author Guibo Pan
 * @since 2015年8月4日
 */
public class FSUpdater extends AbstractUpdater{

	private String conf;
	//维护已更新的文件名集合
	private static List<String> dicts = new ArrayList<String>();
	
	private static FSUpdater singleton = null;
	
	private FSUpdater() {}
	
	public static FSUpdater getInstance() {
		if(singleton == null) {
			synchronized (FSUpdater.class) {
				if(singleton == null) {
					singleton = new FSUpdater();
					return singleton;
				}
			}
		}
		return singleton;
	}
	public void setConf(String conf) {
		this.conf = conf;
	}

	/**
	 * 从properties文件加载词典
	 * 格式为： dict=dict1.txt,dict2.txt
	 */
	@Override
	public boolean update() {
		System.out.println("Updating from filesystem");
		Properties p = getUpdateProperty();
		if(p != null) {
			String[] dicPaths = p.getProperty("dict").split(",");
			List<InputStream> inputStreamList = new LinkedList<InputStream>();
			for(String path : dicPaths) {
				System.out.println("检测到字典路径 "+path);
				
				//已经被更新过，不是新词典
				if(dicts.contains(path)) {
					continue;
				}
				
				if(path != null && !path.isEmpty()) {
					InputStream is = null;
					try {
						is = loader.openResource(path);
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("从 "+path+" 加载词典");
					if(is!=null) {
						inputStreamList.add(is);
						
						//将已更新的词典记录
						dicts.add(path);
					}
				}
				
			}
			if (!inputStreamList.isEmpty()) {
				
				Dictionary.updateDict(inputStreamList);
			}
		}
		return true;
	}
	
	private Properties getUpdateProperty() {
		try {
			if(conf == null)
				return null;
			Properties p = new Properties();
			InputStream confStream = loader.openResource(conf);
			p.load(confStream);
			confStream.close();
			String paths = p.getProperty("dict");
			if(paths == null || paths.trim().isEmpty()) {
				return null;
			}
			return p;
		} catch(Exception e) {
			return null;
		}
	}
}
