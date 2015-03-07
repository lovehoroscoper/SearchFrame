package unigramSeg;

public class WordEntry {
	public String word; //词
	public int freq; //词频

	public WordEntry(String w, int f) {
		word = w;
		freq = f;
	}

	public String toString() {
		return word + ":" + freq;
	}
}
