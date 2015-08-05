/**
 * 
 */
package com.ky;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Weight;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * <p>
 * <b>LuceneSearchBoost</b> 是
 * </p>
 *
 * @since 2013-8-2
 * @author czhcc
 * @version $Id: LuceneSearchBoost.java 12517 2013-08-27 00:49:11Z czh $
 *
 */
public class LuceneSearchBoost
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

        String dField = "content";
        Analyzer stdAn = new IKAnalyzer(true);
        QueryParser parser = new QueryParser(dField,stdAn);
        
        Term term = new Term(dField,query);
        System.out.println("===>" + reader.docFreq(term));
    /*x*/

    /*x LuceneSearch.3 */
        Query q = parser.parse(query);
        Weight weight;
        
        TopDocs hits = searcher.search(q,maxHits);
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
