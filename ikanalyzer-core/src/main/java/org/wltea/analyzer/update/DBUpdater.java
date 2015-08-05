/**
 * DBUpdater
 * 
 * @author Guibo Pan
 */
package org.wltea.analyzer.update;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.wltea.analyzer.dic.Dictionary;


/**
 * DBUpdater
 * 
 * @author Guibo Pan
 * @since 2015年8月4日
 */
public class DBUpdater extends AbstractUpdater{
	
	private String conf = null;
	
	private static DBUpdater singleton = null;
	
	//维护已更新的表集合
	private static List<String> dicts = new LinkedList<String>();
	
	//数据库连接
	private Connection connection;
	
	private DBUpdater() {}
	
	public static DBUpdater getInstance() {
		if(singleton == null) {
			synchronized (FSUpdater.class) {
				if(singleton == null) {
					singleton = new DBUpdater();
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
	 * 从properties文件加载配置
	 * 格式为：
	 * driver=com.mysql.jdbc.Driver
	 * database=jdbc:mysql://127.0.0.1/test
	 * table=table1,table2
	 * user=root
	 * password=1234
	 */
	@Override
	public boolean update() {
		System.out.println("Updating from database");
		Properties p = getProperties();
		if(p != null) {
			String driver = p.getProperty("driver");
			try {
				Class.forName(driver).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}//DB驱动,需要把驱动添加到库中
			

			String database = p.getProperty("database");
			String user = p.getProperty("user");
			String password = p.getProperty("password");
			String[] tables = p.getProperty("table").split(",");

			try {
				connection = DriverManager.getConnection(database,user,password);
				Statement stmt = connection.createStatement();
				
				for(String table : tables) {
					if(table == null || table.trim().isEmpty())
						continue;
					
					//如果已加载，则不再加载
					//if(dicts.contains(table.trim()))
					//	continue;
					
					String sql = "select word from " + table + ";";
					ResultSet res = stmt.executeQuery(sql);
					
					System.out.println(res);
					List<String> words = new LinkedList<String>();
					while(res.next()) {
						words.add(res.getString("word"));
						System.out.println(res.getString("word"));
					}
					Dictionary.getSingleton().updateDictList(words);
					
					dicts.add(table.trim());
				}
				
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public Properties getProperties() {
		try {
			if(conf == null)
				return null;
			Properties p = new Properties();
			p.load(loader.openResource(conf));
			
			String db = p.getProperty("database");
			if(db == null || db.trim().isEmpty()) {
				return null;
			}
			
			return p;
		} catch(Exception e) {
			return null;
		}
	}

}
