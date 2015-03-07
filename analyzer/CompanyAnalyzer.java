/**
     * CompanyAnalyzer.java
     * Version 1.0.0
     * Created on 2013-8-3
     * Copyright your company
     *
     */
package analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.AttributeSource.AttributeFactory;

public class CompanyAnalyzer extends Analyzer {
	@Override
	protected TokenStreamComponents createComponents(String arg0, Reader reader) {
		//调用ComTokenizer切分公司名
		final Tokenizer source = new ComTokenizer(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY, reader);
		return new TokenStreamComponents(source);
	}

}
