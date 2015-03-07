package spell.levenshteinAutomata;

import java.util.ArrayList;

/**
 * 
 * 
 *
 */
public class TestDFA {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		testNFA();
		//testDFA();
		//testMatch();
	}
	
	public static void testNFA() {
		NFA lev = new NFA("food",2);
		
		//System.out.println(lev.getStateNo(0, 0));
		System.out.println(lev.toString());
	}
	
	public static void testMatch(){
		//���ݴ���ʹ���NFA
		NFA lev = new NFA("fod",1);
		//��NFAת����DFA
		DFA dfa = lev.toDFA();
		
		//��ȷ�ʱ�
		Trie<String> stringTrie = new Trie<String>();
		stringTrie.add("food", "food");
		stringTrie.add("hammer", "hammer");
		stringTrie.add("hammock", "hammock");
		stringTrie.add("ipod", "ipod");
		stringTrie.add("iphone", "iphone");
		
		//�������Ƶ���ȷ�Ĵ�
		ArrayList<String> match = dfa.transduce(stringTrie);
		for(String s:match){
			System.out.println(s);
		}
	}
	
	public static void testDFA() {
		//�����༭�����Զ���
		NFA lev = new NFA("foxd",2);
		System.out.println(lev.toString());
		//�����ݼ�����ת����ȷ������״̬��
		DFA dfa = lev.toDFA();
		//System.out.println("dfa");
		//System.out.println(dfa);
		//������food�Ƿ��ܹ�������
		System.out.println(dfa.accept("food"));
		System.out.println(dfa.accept("fooxd"));
		System.out.println(dfa.accept("fooxdjj"));
	}

}
