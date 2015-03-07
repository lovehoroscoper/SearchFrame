package index;

/** ���������ĸ����ࡣ֧�ֶ��ĵ�����ض����� */
public class ScoreDoc implements Comparable<ScoreDoc> {
	DocumentData doc; //�ĵ���ص���Ϣ�������ĵ���ŵ�
	public float score; //��ʾ����ĵ��Ͳ�ѯ���ж����

	public ScoreDoc(DocumentData d, float b) { //���췽��
		doc = d;
		score = b;
	}
	
	public int compareTo(ScoreDoc other) { // ����ض�����
		// ����ֵ������Ҫ����֤��Ҫ��˳�����
		ScoreDoc o =  other;
		return (o.score == score) ? (0) : ((score > o.score) ? (-1) : (1));
	}

	public String toString() { //��ʽ�����
		return doc.filename + ":\t" + doc.docid + "\n" + score;
	}
}