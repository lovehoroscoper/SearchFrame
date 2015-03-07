package similarity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LCS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "����Ϊ���������˵�����Ҫ����2014���ҵ�������������ڴ���������Ϊʵ�����Ŀ�긶�����ߴ��ۡ� ";
		//"���¼���������������·128��";
		String str2 = "���ݷ�չ�ĸ�ί����ҵ����Ϣ��������첿����������������Դ�����������������������С��ʼ��ܾ֡�����ᡢ֤��ᷢ�����ļ����йز��ź͸���Ӧ�߶����Ӳ�����ҵ���ܹ�ʣ���ظ��������⣬��ȷ�������Ʋ��ܹ�ʣ���ظ���������ߵ��򣬼�����Ʋ��ܹ�ʣ���ظ����衣";
		//"�߼���������·128��";
		
		System.out.println( getSimScore(str,str2)) ;
	}
	
	public static double getSimScore(String doc1,String doc2)
	{
		Character[] list = LCS.getStr(doc1);
		Character[] list2 = LCS.getStr(doc2);

		List<Character> list3 = longestCommonSubsequence(list, list2);
		
	    int lcs = list3.size();
		return( lcs / (double)Math.min(doc1.length(), doc2.length())) ;
	}

	public static Character[] getStr(String str) {
		Character[] list = new Character[str.length()];
		try {
			int begin = 0;
			int end = str.length();

			while (begin < end) {
				char c = str.charAt(begin);
				list[begin] = c;
				begin++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static <E> List<E> longestCommonSubsequence(E[] s1, E[] s2) {
		int[][] num = new int[s1.length + 1][s2.length + 1]; // 2D array,
		
		// Actual algorithm
		for (int i = 1; i <= s1.length; i++)
			for (int j = 1; j <= s2.length; j++)
				if (s1[i - 1].equals(s2[j - 1]))
					num[i][j] = 1 + num[i - 1][j - 1];
				else
					num[i][j] = Math.max(num[i - 1][j], num[i][j - 1]);

		//System.out.println("length of LCS = " + num[s1.length][s2.length]);

		int s1position = s1.length, s2position = s2.length;
		List<E> result = new LinkedList<E>();

		while (s1position != 0 && s2position != 0) {
			if (s1[s1position - 1].equals(s2[s2position - 1])) {
				result.add(s1[s1position - 1]);
				s1position--;
				s2position--;
			} else if (num[s1position][s2position - 1] >= num[s1position - 1][s2position]) {
				s2position--;
			} else {
				s1position--;
			}
		}
		Collections.reverse(result);
		return result;
	}

}