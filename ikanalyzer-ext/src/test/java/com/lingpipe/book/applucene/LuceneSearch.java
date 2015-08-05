package com.lingpipe.book.applucene;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Weight;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class LuceneSearch {

    /*x LuceneSearch.1 */
    public static void main(String[] args) 
        throws ParseException, CorruptIndexException,
               IOException {

        File indexDir = new File("D:/Program Data/Projects/tmp/");
        String query = "互聯網";//"abc@163.com";
        int maxHits = Integer.parseInt("3");
    /*x*/        

        System.out.println("Index Dir=" + indexDir.getCanonicalPath());
        System.out.println("query=" + query);
        System.out.println("max hits=" + maxHits);
        System.out.println("Hits (rank,score,file name)");
        
    /*x LuceneSearch.2 */
        Path path = Paths.get("D:/Program Data/Projects/tmp/");
        Directory fsDir = FSDirectory.open(path);
        IndexReader reader = DirectoryReader.open(fsDir);
        IndexSearcher searcher = new IndexSearcher(reader);

        String dField = "text";
        Analyzer stdAn 
            = new IKAnalyzer();
        QueryParser parser 
            = new QueryParser(dField,stdAn);
        
        Term term = new Term(dField,query);
        System.out.println(reader.docFreq(term));
    /*x*/

    /*x LuceneSearch.3 */
        Query q = parser.parse(query);
        Weight weight;
        
        TopDocs hits = searcher.search(q,maxHits);
        ScoreDoc[] scoreDocs = hits.scoreDocs;
        System.out.println(scoreDocs.length);
        for (int n = 0; n < scoreDocs.length; ++n) {
            ScoreDoc sd = scoreDocs[n];
            float score = sd.score;
            int docId = sd.doc;
            Document d = searcher.doc(docId);
//            String fileName = d.get("file");
//            String area = d.get("area");
            System.out.println(d);
    /*x*/
//            System.out.printf("%3d %4.2f  %s, area:%s\n",
//                              n, score, fileName, area);
        }
        reader.close();
    }

}