/**
 * 
 */
package com.ky.ext;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;

/**
 * <p>
 * <b>VecTextField</b> 是带有向量存储的文本分析字段
 * org.apache.solr.schema.FieldType
 * </p>
 * 
 * @since 2013-2-1
 * @author czhcc
 * @version $Id: VecTextField.java 12517 2013-08-27 00:49:11Z czh $
 * 
 */
public class VecTextField extends Field
{
	/* Indexed, tokenized, not stored. */
	public static final FieldType TYPE_NOT_STORED = new FieldType();

	/* Indexed, tokenized, stored. */
	public static final FieldType TYPE_STORED = new FieldType();

	static {
		TYPE_NOT_STORED.setIndexOptions(IndexOptions.DOCS);
		TYPE_NOT_STORED.setTokenized(true);
		TYPE_NOT_STORED.setStoreTermVectors(true);
		TYPE_NOT_STORED.setStoreTermVectorPositions(true);
		TYPE_NOT_STORED.freeze();

		TYPE_STORED.setIndexOptions(IndexOptions.DOCS);
		TYPE_STORED.setTokenized(true);
		TYPE_STORED.setStored(true);
		TYPE_STORED.setStoreTermVectors(true);
		TYPE_STORED.setStoreTermVectorPositions(true);
		TYPE_STORED.freeze();
	}

	// TODO: add sugar for term vectors...?

	/** Creates a new TextField with Reader value. */
	public VecTextField(String name, Reader reader, Store store) {
		super(name, reader, store == Store.YES ? TYPE_STORED : TYPE_NOT_STORED);
	}

	/** Creates a new TextField with String value. */
	public VecTextField(String name, String value, Store store) {
		super(name, value, store == Store.YES ? TYPE_STORED : TYPE_NOT_STORED);
	}

	/** Creates a new un-stored TextField with TokenStream value. */
	public VecTextField(String name, TokenStream stream) {
		super(name, stream, TYPE_NOT_STORED);
	}
}
