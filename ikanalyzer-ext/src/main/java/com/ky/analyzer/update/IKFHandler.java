/**
 * IKFHandler
 * 用于Solr的文件词典更新
 * 
 * @author Guibo Pan
 * @Date 2015年8月3日
 */
package com.ky.analyzer.update;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.hdfs.server.namenode.FSImageFormat.Loader;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.util.plugin.SolrCoreAware;
import org.wltea.analyzer.dic.Dictionary;

public class IKFHandler extends RequestHandlerBase implements SolrCoreAware {
	
	private SolrResourceLoader Loader = null;

	/* (non-Javadoc)
	 * @see org.apache.solr.util.plugin.SolrCoreAware#inform(org.apache.solr.core.SolrCore)
	 */
	@Override
	public void inform(SolrCore core) {
		Loader = core.getResourceLoader();
	}

	/* (non-Javadoc)
	 * @see org.apache.solr.handler.RequestHandlerBase#handleRequestBody(org.apache.solr.request.SolrQueryRequest, org.apache.solr.response.SolrQueryResponse)
	 */
	@Override
	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp)
			throws Exception {
		System.out.println("准备更新文件词典");
		rsp.setHttpCaching(false);
		
		//获取solr请求的参数
		final SolrParams solrParams = req.getParams();
		
		//返回的结果集
		NamedList<Object> result = new NamedList<Object>();
		
		String dict = solrParams.get("dict");
		if(dict != null && !dict.trim().isEmpty()) {
			String[] dictFiles = dict.split("\\|");
			result.add("file", dict);
			List<InputStream> inputStreams = new LinkedList<InputStream>();
			for(String sdict : dictFiles) {
				if(sdict != null && !sdict.isEmpty()) {
					InputStream is = Loader.openResource(sdict);
					
					if(is != null) {
						inputStreams.add(is);
					}
				}
			}
			if(inputStreams.size() > 0) {
				Dictionary.getSingleton().updateDict(inputStreams);
			}
		}
		rsp.add("result", result);
	}

	/* (non-Javadoc)
	 * @see org.apache.solr.handler.RequestHandlerBase#getDescription()
	 */
	@Override
	public String getDescription() {
		return "";
	}

}
