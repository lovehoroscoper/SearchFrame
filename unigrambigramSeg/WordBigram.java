package unigrambigramSeg;

public class WordBigram {
	public String left; //左边的词
	public String right; //右边的词

	public WordBigram(String l, String r) {
		left = l;
		right = r;
	}
	
	@Override
	public int hashCode() {
		return left.hashCode() ^ right.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof WordBigram) {
			WordBigram that = (WordBigram) o;
			if (that.left.equals(this.left) && that.right.equals(this.right)) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		return left + "@" + right;
	}
}
