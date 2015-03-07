package similarity;

public class LongestCommonSubsequence {

	public static void main(String[] args) {
		String str = //"����Ϊ���������˵�����Ҫ����2014���ҵ�������������ڴ���������Ϊʵ�����Ŀ�긶�����ߴ��ۡ� ";
		"���¼���������������·128��";
		String str2 = //"���ݷ�չ�ĸ�ί����ҵ����Ϣ��������첿����������������Դ�����������������������С��ʼ��ܾ֡�����ᡢ֤��ᷢ�����ļ����йز��ź͸���Ӧ�߶����Ӳ�����ҵ���ܹ�ʣ���ظ��������⣬��ȷ�������Ʋ��ܹ�ʣ���ظ���������ߵ��򣬼�����Ʋ��ܹ�ʣ���ظ����衣";
		"�߼���������·128��";
		
		System.out.println( longestCommonSubsequence(str,str2)) ;
	}
	
	public static String longestCommonSubsequence(String s1, String s2) {
		int[][] num = new int[s1.length() + 1][s2.length() + 1]; // 2D array,
		
		// Actual algorithm
		for (int i = 1; i <= s1.length(); i++)
			for (int j = 1; j <= s2.length(); j++)
				if (s1.charAt(i - 1)==(s2.charAt(j - 1)))
					num[i][j] = 1 + num[i - 1][j - 1];
				else
					num[i][j] = Math.max(num[i - 1][j], num[i][j - 1]);

		//System.out.println("length of LCS = " + num[s1.length][s2.length]);

		int s1position = s1.length(), s2position = s2.length();
		StringBuilder result = new StringBuilder();

		while (s1position != 0 && s2position != 0) {
			if (s1.charAt(s1position - 1)==s2.charAt(s2position - 1)) {
				result.append(s1.charAt(s1position - 1));
				s1position--;
				s2position--;
			} else if (num[s1position][s2position - 1] >= num[s1position - 1][s2position]) {
				s2position--;
			} else {
				s1position--;
			}
		}
		result.reverse();
		return result.toString();
	}
}
