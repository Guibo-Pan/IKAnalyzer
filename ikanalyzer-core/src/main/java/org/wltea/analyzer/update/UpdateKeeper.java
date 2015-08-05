/**
 * UpdateKeeper
 * 
 * <p>Updatekeeper 为单例的守护线程，当被创建时自动监控是否有新添加的文件
 * 发现有新添加文件时通知factory进行更新</p>
 * <p>以文件名判断是否为新的文件，只更新新来的词典</p>
 * 
 * @author Guibo Pan
 * @Date 2015年8月3日
 */
package org.wltea.analyzer.update;

import java.io.IOException;
import java.util.Vector;

/**
 * 创建守护线程，当新添加词典时通知factory更新
 */
public class UpdateKeeper implements Runnable {
	public static interface UpdateJob{
		public void update() throws IOException ;

	}
	
	private static long INTERVAL = 1 * 60 * 1000;
	
	private static UpdateKeeper singleton;
	
	// factory都实现UpdateJob接口，实现update方法
	Vector<UpdateJob> filterFactorys;
	Thread worker;
	
	private UpdateKeeper() {
		filterFactorys = new Vector<UpdateKeeper.UpdateJob>();
		worker = new Thread(this);
		worker.setDaemon(true);
		worker.setName("DictUpdateKeeper");
		worker.start();
	}
	
	public static void setInterval(long inter) {
		INTERVAL = inter;
	}
	
	public static UpdateKeeper getInstance() {
		if(singleton == null){
			synchronized(UpdateKeeper.class){
				if(singleton == null){
					singleton = new UpdateKeeper();
					return singleton;
				}
			}
		}
		return singleton;
	}
	
	//保留所有Factory的引用，以便更新
	public void register(UpdateKeeper.UpdateJob filterFactory) {
		filterFactorys.add(filterFactory);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		System.out.println("Update Time Interval: "+INTERVAL);
		while(true) {
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(!filterFactorys.isEmpty()) {
				for(UpdateJob factory : filterFactorys) {
					try {
						factory.update();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
