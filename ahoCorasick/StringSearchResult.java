package ahoCorasick;

/**
 * �������
 * 
 *
 */
public class StringSearchResult {
	private int _index;//����λ��
	private String _keyword;//�ؼ���

	public StringSearchResult(int index, String keyword) {
		_index = index;
		_keyword = keyword;
	}

	public int index() {
		return _index;
	}

	public String keyword() {
		return _keyword;
	}
}
