package simpleProbSeg;

public class WordType {
	public String word;
	public int freq;

	public WordType(String w, int f) {
		word = w;
		freq = f;
	}

	public String toString() {
		return word + ":" + freq;
	}
}
