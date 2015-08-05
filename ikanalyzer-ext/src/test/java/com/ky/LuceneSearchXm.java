/**
 * 
 */
package com.ky;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * <p>
 * <b>LuceneSearchXm</b> 是
 * </p>
 *
 * @since 2013-8-12
 * @author czhcc
 * @version $Id: LuceneSearchXm.java 12517 2013-08-27 00:49:11Z czh $
 *
 */
public class LuceneSearchXm
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		File indexDir = new File("D:/temp/index_ky/");
        String query = "张三";//"abc@163.com";
        int maxHits = Integer.parseInt("8");
    /*x*/        

        System.out.println("Index Dir=" + indexDir.getCanonicalPath());
        System.out.println("query=" + query);
        System.out.println("max hits=" + maxHits);
        System.out.println("Hits (rank,score,file name)");
        
    /*x LuceneSearch.2 */
        Path path = Paths.get("D:/temp/index_ky/");
        Directory fsDir = FSDirectory.open(path);
        IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
        IndexSearcher searcher = new IndexSearcher(reader);
        
        String fiels[] = {"xm","content"};  

		BooleanQuery bq = new BooleanQuery();
		for (int i = 0; i < fiels.length; i++) {

			TermQuery tq = new TermQuery(new Term(fiels[i], query));

			if (fiels[i].equals("xm")) { // 在Name这一个Field需要给大的比重
				tq.setBoost(100.0f);
			} else {
				tq.setBoost(0.0f); // 其他的不需要考滤
			}

			bq.add(tq, BooleanClause.Occur.SHOULD); // 关键字之间是 "或" 的关系
		}
		System.out.println("搜索条件Query:" + bq.toString());
		
		TopDocs hits = searcher.search(bq,maxHits);
        ScoreDoc[] scoreDocs = hits.scoreDocs;

        for (int n = 0; n < scoreDocs.length; ++n) {
            ScoreDoc sd = scoreDocs[n];
            float score = sd.score;
            int docId = sd.doc;
            Document d = searcher.doc(docId);
            String content_display = d.get("content_display");
    /*x*/
            System.out.printf("%3d %4.2f  %s, area:%s\n",
                              n, score, content_display, "");
        }
        reader.close();
	}

}
