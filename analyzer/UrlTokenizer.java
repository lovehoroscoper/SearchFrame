/**
     * UrlTokenizer.java
     * Version 1.0.0
     * Created on 2013-8-3
     * Copyright your company
     *
     */
package analyzer;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
/**
 * 
 * @see 
 * @2013-8-3 
 */
public class UrlTokenizer  extends Tokenizer{

	/**
	 * @param factory
	 * @param input
	 */
	protected UrlTokenizer(AttributeFactory factory, Reader input) {
		super(factory, input);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.TokenStream#incrementToken()
	 */
	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

}
