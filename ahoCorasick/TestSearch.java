package ahoCorasick;

public class TestSearch {
	public static void main(String[] args) {
		//String[] keyWords = //{"a", "ab", "bc", "bca", "c", "caa"};
		//{"����","ӡ��","ӡ��������","�ȹ�","�й���������","����","��������","����������"};//{"aa","bb"};
		String [] keyWords={"����","��","����","��γ","�����п�","��������","�����к�","����","����","ӡ��","ӡ��������","�ȹ�","�й���������","��������","����","����"};
		
		StringSearch searchAlg = new StringSearch(keyWords);
		//searchAlg.buildTree();
		//searchAlg.tree();
		
		System.out.println(searchAlg.toString());
		String textToSearch=//"abccab";
			"������ӡ�ȶȹ��й��������г���";
		//"���人����������ӡ�ȶȹ��й���������";//"aa bb cc gg hh ff";
		// Find all matching keywords?
		StringSearchResult[] results=searchAlg.findAll(textToSearch);
		System.out.println("resultsLength="+results.length);
		// Write all results?
		for(StringSearchResult r : results)
		{
			System.out.println("Keyword='"+r.keyword()+"', Index="+r.index());
		}
		
	}
}
