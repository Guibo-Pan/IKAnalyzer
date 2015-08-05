/**
 * IK的solr handler，用于更新词库
 */
package org.wltea.analyzer.update;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.util.plugin.SolrCoreAware;
import org.wltea.analyzer.dic.Dictionary;

public class IKHandler extends RequestHandlerBase implements SolrCoreAware {
	
	private SolrResourceLoader Loader = null;
	
	@Override
	public void inform(SolrCore core) {
		Loader = core.getResourceLoader();
	}
	
	public String getSource() {
		return "$URL: http:// $";
	}
	
	@Override
	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
			throws Exception {
		rsp.setHttpCaching(false);
		final SolrParams solrParams = req.getParams();
		NamedList<Object> result = new NamedList<Object>();
		result.add("dicPath", new String("测试"));
		
		
		String[] words = solrParams.get("add").split("\\|");
//		for(String s:words){
//			System.out.println(s);
//		}
		//String word = solrParams.get("add");
		result.add("word",words);
		//String[] words = {word};
		if(words.length > 0) {
			System.out.println(words.length+" words to add");
			Dictionary.getSingleton().updateDict(words);
		}
		rsp.add("result",result);
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "";
	}
	
}
