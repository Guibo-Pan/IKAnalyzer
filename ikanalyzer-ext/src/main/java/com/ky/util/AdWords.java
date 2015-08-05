/**
 * 
 */
package com.ky.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

/**
 * <p>
 * <b>AdWords</b> 是
 * </p>
 *
 * @since 2013-2-1
 * @author czhcc
 * @version $Id: AdWords.java 12517 2013-08-27 00:49:11Z czh $
 *
 */
public class AdWords
{
	/**
	 * 获取指定Document的关键字
	 * 使用TFIDF算法，文档数量越多越准确
	 * 
	 * @param paths 索引的目录列表
	 * @param idName Document的关键字段名
	 * @param idValue Document的关键字段值
	 * @param field Document被索引的文本字段
	 * @return 10个关键字
	 * @throws IOException
	 */
	public static String[] adWords(String[] paths, String idName, String idValue, String field) throws IOException
	{
		IndexReader indexReader = createIndexReader(paths);
		String[] result = null;
		for (int docNum = 0; docNum<indexReader.numDocs(); docNum++) {
			String fn = indexReader.document(docNum).get(idName);
            if(!idValue.equals(fn)){
            	continue;
            }
            Fields termVectors = indexReader.getTermVectors(docNum);
            Terms terms = termVectors.terms(field);
            
            TermsEnum termsEnum = terms.iterator(null);
            List<ArWord> arWords = new ArrayList<ArWord>();
            while ((termsEnum.next()) != null) {
            	BytesRef term = termsEnum.term();
            	String s = term.utf8ToString();
            	double idf = indexReader.numDocs()/indexReader.docFreq(new Term(field, term));
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
            
            List<String> list = new ArrayList<String>();
            for(int i=0;i<10;i++){
            	list.add(arWords.get(i).word);
            }
            result = new String[list.size()];
            list.toArray(result);
		}
		
		return result;
	}
	
	private static IndexReader createIndexReader(String[] paths) throws IOException
	{
		IndexReader result = null;
		if(paths.length == 1){
			Path path = Paths.get(paths[0]);
			result = DirectoryReader.open(FSDirectory.open(path));
		} else {
			IndexReader[] readers = new IndexReader[paths.length];
			for(int i=0;i<paths.length;i++){
				Path path = Paths.get(paths[i]);
				readers[i] = DirectoryReader.open(FSDirectory.open(path));
			}
			result = new MultiReader(readers);
		}
		
		return result;
	}
	
	static class ArWord
	{
		double freq;
		
		String word;
	}
}
