package ahoCorasick;

public class Test {
	public static void main(String[] args) {
		String [] strs={"����","��","����","��γ","�����п�","��������","�����к�","����","����","ӡ��","ӡ��������","�ȹ�","�й���������","��������","����","����"};
		SearchTrie sch=SearchTrie.getInit(strs);
		//SearchTrie sch=SearchTrie.getInstance();
		//���人����
		
		System.out.println(sch.toString());
		//StringSearchResult[] resultStr=sch.findAll("������ӡ�ȶȹ��й��������г���");
		//for(StringSearchResult str : resultStr){
		//	System.out.println(str.index()+"��"+str.keyword());
		//}
	}
}