/**
 * IKTokenizerFactory
 * 
 * @author Guibo Pan
 * @Date 2015年8月3日
 */
package com.ky.analyzer.update;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import org.wltea.analyzer.lucene.IKTokenizer;


/**
 *
 */
public class IKTokenizerFactory extends TokenizerFactory implements
ResourceLoaderAware, UpdateKeeper.UpdateJob{
	private final static String USESMART = "useSmart";
	
	private boolean useSmart = false;
	private ResourceLoader loader;
	
	private String localConf = null;
	private String dbConf = null;
	
	private int interval;
	
	private List<AbstractUpdater> updaters;
	
	/**
	 * @param args schema.xml的配置参数
	 */
	public IKTokenizerFactory(Map<String, String> args) {
		super(args);
		String smart = args.get(USESMART);
		if("true".equals(smart)){
			useSmart = true;
		}
		//读取参数：配置文件名
		localConf = args.get("conf");
		
		//读取参数: 数据库配置
		dbConf = args.get("dbconf");
		
		//读取参数：更新时间
		interval = getInt(args, "interval", 1) * 60 * 1000;
		System.out.println("配置文件为: "+localConf);
		System.out.println("数据库配置文件为: "+dbConf);
		init();
	}

	private void init() {
		updaters = new LinkedList<AbstractUpdater>();
		
		//本地词典更新器
		FSUpdater fsUpdater = FSUpdater.getInstance();
		fsUpdater.setConf(localConf);
		updaters.add(fsUpdater);
		
		//数据库词典更新器
		DBUpdater dbUpdater = DBUpdater.getInstance();
		dbUpdater.setConf(dbConf);
		updaters.add(dbUpdater);
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
	
	/**
	 * 在启动的时候被调用一次，
	 * 调用update函数进行添加词典，并启动守护进程
	 */
	@Override
	public void inform(ResourceLoader loader) {
		this.loader = loader;
		try {
			this.update();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(localConf != null && !localConf.trim().isEmpty()) {
			UpdateKeeper.getInstance().register(this);
			UpdateKeeper.setInterval(interval);
		}
	}

	/** 
	 * 
	 * @see com.ky.analyzer.update.UpdateKeeper.UpdateJob#update()
	 */
	@Override
	public void update() throws IOException {
		System.out.println("Update Dictionary");
		
		for(AbstractUpdater upd : updaters) {
			upd.inform(loader);
			upd.update();
		}
	}
	
	
}