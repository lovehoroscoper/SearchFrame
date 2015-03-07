package unigrambigramSeg;

import java.util.HashMap;

public class TestWordBigram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(new WordBigram("中国","北京").hashCode());
		System.out.println(new WordBigram("中国","北京").hashCode());
		
		HashMap<WordBigram, Integer> bigrams = new HashMap<WordBigram, Integer>();
		bigrams.put(new WordBigram("中国","北京"), 10);
		int freq = bigrams.get(new WordBigram("中国","北京"));
		System.out.println(freq);
	}

}
