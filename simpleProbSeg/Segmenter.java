package simpleProbSeg;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import unigrambigramSeg.WordType;


public class Segmenter {

	private static final SuffixTrie dic = SuffixTrie.getInstance();
	private static final double minValue = Double.NEGATIVE_INFINITY / 2;

	private int[] prevNode; // ���ǰ��ڵ�����
	private double[] prob; // �ڵ��������
	String text;

	public Segmenter (String txt){
		text= txt;
	}
	
	/**
	 * ����Ƶ�ָ�
	 * 
	 * @param txt
	 * @return
	 */
	public List<String> split() {
		// if (StringUtils.isBlank(txt))
		// throw new IllegalArgumentException();
		List<String> words = new ArrayList<String>(); //�зֳ�4�Ĵ�����
		Deque<Integer> path = new ArrayDeque<Integer>(); //��ѽڵ�����
		prevNode = new int[text.length() + 1];// ���ǰ��ڵ�����
		prob = new double[text.length() + 1]; // �ڵ����
		prevNode[0] = 0;

		//��4���ǰ��ʵļ���
		ArrayList<WordType> prevWords = new ArrayList<WordType>();
		
		// ���ÿ��ڵ�����ǰ��ڵ�
		for (int i = 1; i < prevNode.length; i++){
			double maxProb = minValue; // ��ѡ�ڵ����
			int maxNode = 0; // ��ѡ���ǰ��ڵ�

			//�Ӵʵ��в���ǰ��ʵļ���
			dic.matchAll(text, i - 1,prevWords);
			
			//���ǰ��ʼ�����ѡ���ǰ��ڵ�
			for (WordType word : prevWords) {
				double logProb = Math.log(word.freq)
						- Math.log(dic.wordCount);
				// System.out.println(i+" "+word.word);
				// CnToken token = new CnToken(word.word, i - word.word.length(), i,
				// logProb);
				// tokens.add(token);
				int start = i - word.word.length(); // ��ѡǰ��ڵ�
				double nodeProb = prob[start] + logProb;// ��ѡ�ڵ����
				if (nodeProb > maxProb) {// ���������������ǰ��
					maxNode = start;
					maxProb = nodeProb;
					// System.out.println(i+" maxNode:"+maxNode);
				}
			}

			prob[i] = maxProb;// �ڵ����
			prevNode[i] = maxNode;// ���ǰ��ڵ�
		}
		//	getBestPrev(i);
		// for (int nodeId:prevNode) {
		// System.out.println(nodeId);
		// }
		// �Ӻ���ǰ��˷���ǰ��ڵ�
		for (int i = text.length(); i > 0; i = prevNode[i])			
			path.push(i);
		int start = 0;
		for (Integer end : path) {
			words.add(text.substring(start, end));
			start = end;
		}
		return words;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Segmenter seg = new Segmenter("Ѱ�����ǰ��");
		List<String> result = seg.split();
		for (String word:result) {
			System.out.println(word);
		}
	}
}
