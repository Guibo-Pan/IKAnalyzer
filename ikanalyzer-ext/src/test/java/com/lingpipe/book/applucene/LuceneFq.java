/**
 * 
 */
package com.lingpipe.book.applucene;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

/**
 * <p>
 * <b>LuceneFq</b> 是
 * </p>
 *
 * @since 2013-2-1
 * @author czhcc
 * @version $Id: LuceneFq.java 12517 2013-08-27 00:49:11Z czh $
 *
 */
public class LuceneFq
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		File indexDir = new File("D:/temp/index_test4/");
		Path path = Paths.get("D:/temp/index_test4/");
		IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
	    IndexSearcher searcher = new IndexSearcher(reader);
	    IndexReader indexReader = searcher.getIndexReader();
	    for (int docNum = 0; docNum<reader.numDocs(); docNum++) {
	    	Fields termVectors = indexReader.getTermVectors(docNum);
	    	String fn = indexReader.document(docNum).get("file");
            if(!"e.txt".equals(fn)){
            	continue;
            }
            Terms terms = termVectors.terms("text");
//            TermsEnum iterator = terms.iterator(TermsEnum.EMPTY);
            /*while(true){
            	BytesRef next = iterator.next();
            	if(next == null){
            		break;
            	}
            	String term = next.utf8ToString();
            	System.out.println(term + "/" + iterator.docFreq());
            }*/
            TermsEnum termsEnum = terms.iterator(null);
            List<ArWord> arWords = new ArrayList<ArWord>();
            while ((termsEnum.next()) != null) {
            	String s = termsEnum.term().utf8ToString();
            	double idf = reader.numDocs()/indexReader.docFreq(new Term("text", termsEnum.term()));
            	double freq = termsEnum.totalTermFreq()*Math.log(idf);
            	ArWord w = new ArWord();
                w.freq = freq;
                w.word = s;
                arWords.add(w);
            }
            Collections.sort(arWords,new Comparator<ArWord>() {

    			public int compare(ArWord o1, ArWord o2) {
    				return o1.freq < o2.freq ? 1 : -1;
    			}
    		});
            
            for(int i=0;i<10;i++){
            	System.out.println(arWords.get(i).word);
            }
            /*boolean found = termsEnum.seekExact(new BytesRef("czh1"), true);
            if (found) {
              // get the document frequency
              System.out.println(termsEnum.docFreq());
              System.out.println(termsEnum.totalTermFreq());
              // enumerate through documents
              DocsEnum docs = termsEnum.docs(null, null);
              // enumerate through documents and positions
              DocsAndPositionsEnum docsAndPositions = termsEnum.docsAndPositions(null, null);
            }*/
	    }
	}

	static class ArWord
	{
		double freq;
		
		String word;
	}
}
