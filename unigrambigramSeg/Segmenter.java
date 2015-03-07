package unigrambigramSeg;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Segmenter {
	int[] prevNode;
	double[] prob;
	WordType[] preCnToken;
	private static final WordType startWord = new WordType("start", 1000);
	private static final double MIN_PROB = Double.NEGATIVE_INFINITY / 2;
	private static final SuffixTrie dic = SuffixTrie.getInstance();
	double lambda1 = 0.3;
	double lambda2 = 0.7;

	public int getBigramFreq(WordType prev, WordType next) {
		// System.out.println(prev.termText +"@"+next.termText);
		return 100;
	}

	// 计算出最大概率的数组

	public ArrayDeque<Integer> bestPath() { // 按节点求最佳前驱
		ArrayDeque<Integer> ret = new ArrayDeque<Integer>();
		for (int i = prevNode.length - 1; i > 0; i = prevNode[i]) // 从右向左取词候选词
		{
			ret.addFirst(i);
		}
		return ret;
	}

	public ArrayDeque<Integer> split(String sentence) {
		int len = sentence.length() + 1;// 字符串长度
		prevNode = new int[len]; //最佳前趋节点数组
		prob = new double[len]; //节点概率数组
		prob[0] = 0;// 节点0的初始概率是1,取log后是0
		preCnToken = new WordType[len]; //最佳前驱词数组
		preCnToken[0] = startWord; //节点0的最佳前驱词是开始词

		ArrayList<WordType> wordMatch = new ArrayList<WordType>();

		for (int i = 1; i < len; ++i) {
			// 计算节点i的最佳前驱节点
			double maxProb = MIN_PROB;
			int maxPrev = -1;
			WordType preToken = null;

			dic.matchAll(sentence, i - 1, wordMatch);// 到词典中查询

			for (WordType t1 : wordMatch) {
				int start = i - t1.word.length();
				// System.out.println("start is :"+start);
				// System.out.println("t1 is :"+t1);
				WordType t2 = preCnToken[start];
				double wordProb = 0;
				// if (t2 != startWord) {
				// System.out.println("t2 is null?"+t2);
				int bigramFreq = getBigramFreq(t2, t1);// 从二元词典找二元频率
				wordProb = lambda1 * t1.freq / dic.wordCount + lambda2
						* (bigramFreq / t2.freq);// 平滑后的二元概率
				// } else {
				// wordProb = t1.freq / dic.wordCount;
				// }

				// System.out.println("wordProb "+wordProb);
				double nodeProb = prob[start] + (Math.log(wordProb));
				// System.out.println("nodeProb "+nodeProb);
				if (nodeProb > maxProb)// 概率最大的算作最佳前趋
				{
					// System.out.println("enter");
					maxPrev = start;
					maxProb = nodeProb;
					preToken = t1;
				}
			}

			prob[i] = maxProb; //记录节点i的概率
			prevNode[i] = maxPrev; //记录节点i的最佳前趋节点
			// System.out.println("preCnToken["+i+"]"+preToken);
			preCnToken[i] = preToken; //节点i的最佳前驱词
		}
		return bestPath();
	}

	public static void main(String[] args) throws IOException {
		Segmenter seg = new Segmenter();

		String sentence = "他很聪明,很活跃";
			//"当前节点为空,说明词典中找不到对应的词,则将单个字符返回";
		// "中国成立了";// "大学生活动中心"; //
		ArrayDeque<Integer> ret = seg.split(sentence);

		int start = 0;
		for (Integer end : ret) {
			System.out.print(sentence.substring(start, end) + " / ");
			start = end;
		}
	}
}
