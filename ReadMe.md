#solr动态添加词库  

方法一：  
配置在${collection}/conf下的solrconfig.xml，增加  
	<requestHandler name="/ikupdate" class="com.ky.analyzer.update.IKHandler">
     	<lst name="defaults">
     	</lst>
  	</requestHandler>
  
然后利用URL进行请求  
例如：http://localhost:8080/solr/sample_techproducts_configs/ikupdate?add=石猴  
多个词可用 | 分隔，如 石猴|天蓬  
此时该词就会被添加到词典中。但需要注意的是，这种方法没有办法修改词典文件的内容，服务器重启后新增词条将丢失  
  
实现原理：利用IKHandler拦截/ikupdate请求，并获取add参数  
  
======
方法二：  
通过词库文件添加  
配置在${collection}/conf下的solrconfig.xml，增加  
	<requestHandler name="/fupdate" class="com.ky.analyzer.update.IKFHandler">
     	<lst name="defaults">
			<str name="dict"></str>
     	</lst>
  	</requestHandler>  
词库文件放在solr的资源根目录下，即每个collection的conf目录。  
然后利用URL进行请求  
例如：http://localhost:8080/solr/sample_techproducts_configs/fupdate?dict=test.txt  
  
实现原理：利用IKFHandler拦截/fupdate请求，并获取dict参数  
  
====
<fieldType name="text_ik" class="solr.TextField">
	<analyzer type="index" isMaxWordLength="false" class="com.ky.analyzer.lucene.IKAnalyzer"/>
	<analyzer type="query" isMaxWordLength="true" class="com.ky.analyzer.lucene.IKAnalyzer"/>
</fieldType>  

====  
方法三：  
1. 通过词库文件添加，在启动时进行更新，并由程序定期自动更新(若有添加)，可持续多次添加，默认以文件名为判别，加载后不再加载  
2. 通过数据库导入，在启动时进行更新，并自动定期更新，默认为数据库表名为判别，每次都重新加载  
   
修改时仅需修改配置文件，即可更新词典  
 
在schma.xml把用ik分词的字段修改为text_ik  
并添加  
<fieldType name="text_ik" class="solr.TextField">
	<analyzer type="index" isMaxWordLength="false" >
		<tokenizer class="com.ky.analyzer.update.IKTokenizerFactory" useSmart="false" conf="ik.conf" dbconf="ikdb.conf" interval="1" />
	</analyzer>
	<analyzer type="query" isMaxWordLength="true" >
		<tokenizer class="com.ky.analyzer.update.IKTokenizerFactory" useSmart="false" conf="ik.conf" dbconf="ikdb.conf" interval="1"/>
	</analyzer>
</fieldType>

参数解释：  
conf为本地文件的配置文件  
格式为 dict=test.txt,word.txt  
(Properties格式，以逗号分隔)  
  
dbconf为数据库的配置文件  
格式为  
driver=com.mysql.jdbc.Driver  
database=jdbc:mysql://localhost:3306/dict  
table=hello,hello2  
user=root  
password=root  
(需要将该数据库的jdbc的包添加到依赖)  
  
interval为更新时间，整数  单位：分钟  