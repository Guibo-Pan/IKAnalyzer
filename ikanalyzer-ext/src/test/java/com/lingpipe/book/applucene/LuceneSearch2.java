/**
 * 
 */
package com.lingpipe.book.applucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * <p>
 * <b>LuceneSearch2</b> 是
 * </p>
 *
 * @since 2012-8-8
 * @author czhcc
 * @version $Id: LuceneSearch2.java 12517 2013-08-27 00:49:11Z czh $
 *
 */
public class LuceneSearch2 {
	
	private static IndexReader getReader2() throws IOException
	{
		File indexDir = new File("D:/temp/index_test2/");
		Path path = Paths.get("D:/temp/index_test2/");
		Directory fsDir = FSDirectory.open(path);
        IndexReader reader = DirectoryReader.open(fsDir);
        
        return reader;
	}

	/*x LuceneSearch.1 */
    public static void main(String[] args) 
        throws ParseException, CorruptIndexException,
               IOException {

        File indexDir = new File("D:/temp/index_test1/");
        String query = "USED";
        int maxHits = Integer.parseInt("3");
    /*x*/        

        System.out.println("Index Dir=" + indexDir.getCanonicalPath());
        System.out.println("query=" + query);
        System.out.println("max hits=" + maxHits);
        System.out.println("Hits (rank,score,file name)");
        
    /*x LuceneSearch.2 */
        Path path = Paths.get("D:/temp/index_test1/");
        Directory fsDir = FSDirectory.open(path);
        IndexReader reader = DirectoryReader.open(fsDir);
        IndexReader r2 = getReader2();
        MultiReader r = new MultiReader(reader,r2);
        IndexSearcher searcher = new IndexSearcher(r);

        String dField = "text";
        Analyzer stdAn 
            = new StandardAnalyzer();
        QueryParser parser 
            = new QueryParser(dField,stdAn);
    /*x*/
        
        /*TermQuery subquery = new TermQuery(new Term(dField,query));
        FieldScoreQuery scorefield = new FieldScoreQuery("areaScoe", FieldScoreQuery.Type.BYTE);
        CustomScoreQuery q = new CustomScoreQuery(subquery, scorefield);*/

    /*x LuceneSearch.3 */
        Query q = parser.parse(query);
        
        TopDocs hits = searcher.search(q,maxHits);
        ScoreDoc[] scoreDocs = hits.scoreDocs;

        for (int n = 0; n < scoreDocs.length; ++n) {
            ScoreDoc sd = scoreDocs[n];
            float score = sd.score;
            int docId = sd.doc;
            Document d = searcher.doc(docId);
            String fileName = d.get("file");
            String area = d.get("area");
    /*x*/
            System.out.printf("%3d %4.2f  %s, area:%s\n",
                              n, score, fileName, area);
        }
        reader.close();
    }

}
